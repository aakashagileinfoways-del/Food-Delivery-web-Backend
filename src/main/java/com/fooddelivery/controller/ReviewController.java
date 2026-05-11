package com.fooddelivery.controller;

import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.dto.request.SubmitReviewRequest;
import com.fooddelivery.dto.response.ReviewResponse;
import com.fooddelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<ReviewResponse> submitReview(
            @RequestBody SubmitReviewRequest request,
            Authentication authentication
    ) {
        String customerEmail = authentication.getName();
        ReviewResponse response = reviewService.submitReview(request, customerEmail);
        return new ApiResponse<>(
                true,
                200,
                "Review submitted successfully",
                response
        );
    }
}

