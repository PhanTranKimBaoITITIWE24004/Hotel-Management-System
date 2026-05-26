package com.example.hotel_management_system.service;

import com.example.hotel_management_system.model.FoodOrder;
import java.math.BigDecimal;
import java.util.List;

public interface FoodOrderService {
    FoodOrder createFoodOrder(Long bookingId, String itemName, Integer quantity, BigDecimal unitPrice);
    List<FoodOrder> getFoodOrdersByBookingId(Long bookingId);
    List<FoodOrder> getPendingFoodOrders();
    FoodOrder updateOrderStatus(Long orderId, FoodOrder.OrderStatus status);
}
