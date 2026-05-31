package com.example.hotel_management_system.controller.api;

import com.example.hotel_management_system.dto.BookingResponseDTO;
import com.example.hotel_management_system.model.Guest;
import com.example.hotel_management_system.service.BookingService;
import com.example.hotel_management_system.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestRestController {

    private final GuestService guestService;
    private final BookingService bookingService;

    public GuestRestController(GuestService guestService, BookingService bookingService) {
        this.guestService = guestService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getGuestBookings(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestId(id));
    }

    @PostMapping
    public ResponseEntity<Guest> createGuest(@RequestBody Guest guest) {
        Guest createdGuest = guestService.createGuest(guest);
        return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @RequestBody Guest guestDetails) {
        return ResponseEntity.ok(guestService.updateGuest(id, guestDetails));
    }
}
