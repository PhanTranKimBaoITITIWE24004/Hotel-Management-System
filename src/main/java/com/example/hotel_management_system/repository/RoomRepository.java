package com.example.hotel_management.repository;

import com.example.hotel_management.model.Room;
import com.example.hotel_management.model.Room.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {

    // Spring generates this SQL automatically from the method name
    List<Room> findByStatus(RoomStatus status);

    List<Room> findByCategoryId(Long categoryId);

    // For complex queries you write JPQL (Java-style SQL)
    @Query("""
        SELECT r FROM Room r
        WHERE r.status = 'AVAILABLE'
        AND r.id NOT IN (
            SELECT b.room.id FROM Booking b
            WHERE b.status != 'CHECKED_OUT'
            AND b.checkInDate < :checkOut
            AND b.checkOutDate > :checkIn
        )
    """)
    List<Room> findAvailableRooms(
        @Param("checkIn")  LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );
}
