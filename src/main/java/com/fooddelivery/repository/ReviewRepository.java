package com.fooddelivery.repository;

import com.fooddelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByCustomer_IdAndOrderItem_Id(Long customerId, Long orderItemId);

    @Query("select avg(r.overallScore) from Review r where r.restaurant.id = :restaurantId")
    Double findAvgOverallScoreByRestaurantId(@Param("restaurantId") Long restaurantId);
}

