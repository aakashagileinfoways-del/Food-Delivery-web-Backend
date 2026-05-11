package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class SubmitReviewRequest {
    private Long orderItemId;

    private Integer foodQualityRating;
    private Integer packagingRating;
    private Integer deliveryTimeRating;

    private String reviewText;
}

