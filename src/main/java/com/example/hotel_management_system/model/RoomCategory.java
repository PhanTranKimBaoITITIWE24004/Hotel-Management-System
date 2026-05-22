package com.example.hotel_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data                    // Lombok: auto-generates getters, setters, toString
@Entity                  // tells Spring: make a table for this class
@Table(name = "room_categories")
public class RoomCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-increment
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;           // e.g. "Standard", "Deluxe", "Suite"

    private String description;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy = 2;
}