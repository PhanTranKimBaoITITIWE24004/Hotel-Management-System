package com.example.hotel_management_system.controller.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel_management_system.model.FoodOrder;
import com.example.hotel_management_system.service.FoodOrderService;

@RestController
@RequestMapping("/api/food-orders")
public class FoodOrderRestController {

    private final FoodOrderService foodOrderService;

    public FoodOrderRestController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @PostMapping
    public ResponseEntity<FoodOrder> createFoodOrder(@RequestBody Map<String, Object> body) {
        Long bookingId = Long.valueOf(body.get("bookingId").toString());
        String itemName = body.get("itemName").toString();
        Integer quantity = Integer.valueOf(body.get("quantity").toString());
        BigDecimal unitPrice = new BigDecimal(body.get("unitPrice").toString());

        FoodOrder order = foodOrderService.createFoodOrder(bookingId, itemName, quantity, unitPrice);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<FoodOrder>> getFoodOrdersByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(foodOrderService.getFoodOrdersByBookingId(bookingId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FoodOrder>> getPendingFoodOrders() {
        return ResponseEntity.ok(foodOrderService.getPendingFoodOrders());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FoodOrder> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        if (statusStr == null) {
            throw new IllegalArgumentException("Status value is required");
        }
        FoodOrder.OrderStatus status = FoodOrder.OrderStatus.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(foodOrderService.updateOrderStatus(id, status));
    }
}