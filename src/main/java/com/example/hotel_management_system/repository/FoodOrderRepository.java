package com.example.hotel_management.repository;

import com.example.hotel_management.model.FoodOrder;
import com.example.hotel_management.model.FoodOrder.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    // All food orders for one booking (used when generating the bill)
    List<FoodOrder> findByBookingId(Long bookingId);

    // All orders with a specific status (e.g. show kitchen what's PENDING)
    List<FoodOrder> findByStatus(OrderStatus status);

    // All orders for a specific booking AND status
    List<FoodOrder> findByBookingIdAndStatus(Long bookingId, OrderStatus status);

    // Sum of all food charges for a booking (used by billing service)
    @Query("""
        SELECT COALESCE(SUM(f.unitPrice * f.quantity), 0)
        FROM FoodOrder f
        WHERE f.booking.id = :bookingId
    """)
    BigDecimal getTotalFoodChargesByBooking(@Param("bookingId") Long bookingId);
}
