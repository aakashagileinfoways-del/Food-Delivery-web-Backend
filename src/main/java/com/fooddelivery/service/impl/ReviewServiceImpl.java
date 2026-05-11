package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.SubmitReviewRequest;
import com.fooddelivery.dto.response.ReviewResponse;
import com.fooddelivery.entity.OrderItem;
import com.fooddelivery.entity.Review;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public ReviewResponse submitReview(SubmitReviewRequest request, String customerEmail) {
        if (request.getOrderItemId() == null) {
            throw new RuntimeException("orderItemId is required");
        }

        int f = validateRating(request.getFoodQualityRating(), "foodQualityRating");
        int p = validateRating(request.getPackagingRating(), "packagingRating");
        int d = validateRating(request.getDeliveryTimeRating(), "deliveryTimeRating");

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        if (orderItem.getOrder() == null || orderItem.getOrder().getCustomer() == null) {
            throw new RuntimeException("Order item is not linked to a valid order");
        }

        if (!orderItem.getOrder().getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("You can only review items from your own orders");
        }

        if (orderItem.getOrder().getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("You can only submit reviews after delivery");
        }

        Restaurant restaurant = orderItem.getMenu() != null ? orderItem.getMenu().getRestaurant() : null;
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found for the order item");
        }

        boolean alreadyReviewed = reviewRepository.existsByCustomer_IdAndOrderItem_Id(customer.getId(), orderItem.getId());
        if (alreadyReviewed) {
            throw new RuntimeException("You have already reviewed this item");
        }

        Review review = new Review();
        review.setCustomer(customer);
        review.setOrderItem(orderItem);
        review.setRestaurant(restaurant);
        review.setFoodQualityRating(f);
        review.setPackagingRating(p);
        review.setDeliveryTimeRating(d);
        review.setReviewText(request.getReviewText());
        review.setOverallScore((f + p + d) / 3.0);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Update restaurant rating based on average overallScore.
        Double avg = reviewRepository.findAvgOverallScoreByRestaurantId(restaurant.getId());
        if (avg != null) {
            restaurant.setRating(avg);
            restaurantRepository.save(restaurant);
        }

        return new ReviewResponse(
                saved.getId(),
                orderItem.getId(),
                restaurant.getId(),
                saved.getFoodQualityRating(),
                saved.getPackagingRating(),
                saved.getDeliveryTimeRating(),
                saved.getReviewText(),
                saved.getOverallScore(),
                saved.getCreatedAt()
        );
    }

    private int validateRating(Integer rating, String fieldName) {
        if (rating == null) {
            throw new RuntimeException(fieldName + " is required");
        }
        if (rating < 1 || rating > 5) {
            throw new RuntimeException(fieldName + " must be between 1 and 5");
        }
        return rating;
    }
}

