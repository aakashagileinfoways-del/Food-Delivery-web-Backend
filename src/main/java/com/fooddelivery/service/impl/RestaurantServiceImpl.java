package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.RestaurantStatus;
import com.fooddelivery.repository.MenuRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setAddress(request.getAddress());
        restaurant.setOwner(owner);
          restaurant.setStatus(RestaurantStatus.PENDING);
        restaurantRepository.save(restaurant);

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getAddress(),
            restaurant.getStatus().name(),
                restaurant.getRating(),
                owner.getName(),
                0  
        );
    }

@Override
public List<RestaurantResponse> getAllRestaurants() {

    return restaurantRepository.findAll()
            .stream()
            .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
            .filter(Restaurant::isActive)
            .map(r -> {

                int itemCount = menuRepository.countByRestaurantId(r.getId());

                return new RestaurantResponse(
                        r.getId(),
                        r.getName(),
                        r.getCuisineType(),
                        r.getAddress(),
                        r.getStatus().name(),
                        r.getRating(),
                        r.getOwner().getName(),
                        itemCount   // ✅ NEW
                );
            })
            .toList();
}
    

    @Override
    public RestaurantResponse updateStatus(Long id, RestaurantStatus status) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurant.setStatus(status);
        restaurantRepository.save(restaurant);

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getAddress(),
                restaurant.getStatus().name(),
                restaurant.getRating(),
                restaurant.getOwner().getName(),
                0
        );
    }
 @Override
public List<RestaurantResponse> searchRestaurants(String cuisine, Double rating) {
    String normalizedCuisine = (cuisine == null || cuisine.isBlank()) ? null : cuisine;

    return restaurantRepository.findAll().stream()
            .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
            .filter(Restaurant::isActive)
            .filter(r -> normalizedCuisine == null
                    || (r.getCuisineType() != null && r.getCuisineType().equalsIgnoreCase(normalizedCuisine)))
            .filter(r -> rating == null || (r.getRating() != null && r.getRating() >= rating))
            .map(r -> new RestaurantResponse(
                    r.getId(),
                    r.getName(),
                    r.getCuisineType(),
                    r.getAddress(),
                    r.getStatus().name(),
                    r.getRating(),
                    r.getOwner().getName(),
                    r.getMenuItems() != null ? r.getMenuItems().size() : 0
            ))
            .toList();
}

}