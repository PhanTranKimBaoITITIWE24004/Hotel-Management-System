package com.example.hotel_management_system.service;

import java.util.Optional;

import com.example.hotel_management_system.model.Bill;
import com.example.hotel_management_system.model.Booking;

public interface BillService {
    Bill getBillByBookingId(Long bookingId);
    Optional<Bill> findBillByBookingId(Long bookingId);
    Bill createBillForBooking(Booking booking);
    Bill payBill(Long billId);
    Bill getBillById(Long id);
}
