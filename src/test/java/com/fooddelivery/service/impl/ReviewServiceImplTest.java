package com.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fooddelivery.repository.ReviewRepository;

public class ReviewServiceImplTest {
@InjectMocks
    private ReviewServiceImpl reviewService;
   @Mock
    private ReviewRepository reviewRepository;
      
    @Test
    void testSubmitReview() {
        

    }
}
