package com.example.hotel_management_system.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel_management_system.model.Booking;
import com.example.hotel_management_system.model.Booking.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // find all bookings for a specific guest
    List<Booking> findByGuestId(Long guestId);

    // find all bookings by status
    List<Booking> findByStatus(BookingStatus status);

    // check if a room is already booked for given dates (for conflict check)
    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status != 'CHECKED_OUT'
        AND b.checkInDate < :checkOut
        AND b.checkOutDate > :checkIn
    """)
    boolean isRoomBooked(
        @Param("roomId")   Long roomId,
        @Param("checkIn")  LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );
}
