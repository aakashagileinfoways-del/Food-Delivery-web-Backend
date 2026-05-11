package com.fooddelivery.service;

import com.fooddelivery.dto.request.SubmitReviewRequest;
import com.fooddelivery.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse submitReview(SubmitReviewRequest request, String customerEmail);
}

