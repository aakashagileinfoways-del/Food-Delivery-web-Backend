package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {

       private Double totalRevenue;
    private Double avgDeliveryTime;

    private Long totalOrders;
    private Long activeRestaurants;

    private Long pendingOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;

}