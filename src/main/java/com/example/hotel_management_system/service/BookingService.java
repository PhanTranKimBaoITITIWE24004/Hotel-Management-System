package com.example.hotel_management_system.service;

import com.example.hotel_management_system.dto.BookingRequestDTO;
import com.example.hotel_management_system.dto.BookingResponseDTO;
import com.example.hotel_management_system.model.Booking;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request);
    BookingResponseDTO getBookingDTOById(Long id);
    Booking getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO checkIn(Long id);
    BookingResponseDTO checkOut(Long id);
    BookingResponseDTO cancelBooking(Long id);
    List<BookingResponseDTO> getBookingsByGuestId(Long guestId);
}
