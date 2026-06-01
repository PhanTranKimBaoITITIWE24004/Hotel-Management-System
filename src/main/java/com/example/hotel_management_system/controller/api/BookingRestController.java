package com.example.hotel_management_system.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel_management_system.dto.BookingRequestDTO;
import com.example.hotel_management_system.dto.BookingResponseDTO;
import com.example.hotel_management_system.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingRestController {

    private final BookingService bookingService;

    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(@RequestParam(required = false) Long guestId) {
        if (guestId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByGuestId(guestId));
        }
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingDTOById(id));
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<BookingResponseDTO> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<BookingResponseDTO> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkOut(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
