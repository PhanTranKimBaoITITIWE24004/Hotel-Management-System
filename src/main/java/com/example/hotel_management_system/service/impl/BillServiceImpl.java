package com.example.hotel_management_system.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Bill;
import com.example.hotel_management_system.model.Booking;
import com.example.hotel_management_system.repository.BillRepository;
import com.example.hotel_management_system.repository.FoodOrderRepository;
import com.example.hotel_management_system.service.BillService;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final FoodOrderRepository foodOrderRepository;

    public BillServiceImpl(BillRepository billRepository, FoodOrderRepository foodOrderRepository) {
        this.billRepository = billRepository;
        this.foodOrderRepository = foodOrderRepository;
    }

    @Override
    public Bill getBillByBookingId(Long bookingId) {
        return billRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found for Booking ID: " + bookingId));
    }

    @Override
    public Bill createBillForBooking(Booking booking) {
        // Calculate nights spent
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (days <= 0) {
            days = 1; // charge at least 1 night
        }

        BigDecimal pricePerNight = booking.getRoom().getCategory().getPricePerNight();
        BigDecimal roomCharges = pricePerNight.multiply(BigDecimal.valueOf(days));

        // Get total food charges
        BigDecimal foodCharges = foodOrderRepository.getTotalFoodChargesByBooking(booking.getId());
        if (foodCharges == null) {
            foodCharges = BigDecimal.ZERO;
        }

        // Check if bill already exists
        Bill bill = billRepository.findByBookingId(booking.getId()).orElse(null);
        if (bill == null) {
            bill = new Bill();
            bill.setBooking(booking);
        }

        bill.setRoomCharges(roomCharges);
        bill.setFoodCharges(foodCharges);
        bill.calculateTotal();
        bill.setStatus(Bill.BillStatus.UNPAID);

        return billRepository.save(bill);
    }

    @Override
    public Bill payBill(Long billId) {
        Bill bill = getBillById(billId);
        bill.setStatus(Bill.BillStatus.PAID);
        bill.setPaidAt(LocalDateTime.now());
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + id));
    }

    @Override
    public Optional<Bill> findBillByBookingId(Long bookingId) {
        return billRepository.findByBookingId(bookingId);
    }
}
