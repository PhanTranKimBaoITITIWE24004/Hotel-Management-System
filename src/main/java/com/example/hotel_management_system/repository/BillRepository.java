package com.example.hotel_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel_management_system.model.Bill;
import com.example.hotel_management_system.model.Bill.BillStatus;

public interface BillRepository extends JpaRepository<Bill, Long> {

    // Find the bill for a specific booking
    Optional<Bill> findByBookingId(Long bookingId);

    // Find all unpaid or paid bills
    List<Bill> findByStatus(BillStatus status);

    // Total revenue from all paid bills
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.status = 'PAID'")
    Double getTotalRevenue();

    // Revenue for a specific month and year
    @Query("""
        SELECT SUM(b.totalAmount) FROM Bill b
        WHERE b.status = 'PAID'
        AND MONTH(b.paidAt) = :month
        AND YEAR(b.paidAt)  = :year
    """)
    Double getRevenueByMonth(
        @Param("month") int month,
        @Param("year")  int year
    );

    // Check if a booking already has a generated bill
    boolean existsByBookingId(Long bookingId);
}
