package com.fooddelivery.repository;

import com.fooddelivery.entity.Order;
import com.fooddelivery.enums.OrderStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
        List<Order> findByUserId(Long userId);

        List<Order> findByCustomerId(Long customerId);

        long countByDeliveryAgent_IdAndStatusIn(Long deliveryAgentId, List<OrderStatus> statuses);



        /////////// admin dashboard queries /////////// 
 long countByStatus(OrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status = 'DELIVERED'
    """)
    Double getTotalRevenue();

    @Query("""
        SELECT COALESCE(AVG(
            TIMESTAMPDIFF(MINUTE, o.orderTime, o.deliveredTime)
        ), 0)
        FROM Order o
        WHERE o.status = 'DELIVERED'
    """)
    Double getAvgDeliveryTime();
}