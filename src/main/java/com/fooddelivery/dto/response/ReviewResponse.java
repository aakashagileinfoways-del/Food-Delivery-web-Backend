package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long orderItemId;
    private Long restaurantId;

    private Integer foodQualityRating;
    private Integer packagingRating;
    private Integer deliveryTimeRating;

    private String reviewText;
    private Double overallScore;
    private LocalDateTime createdAt;
}

