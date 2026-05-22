package com.example.hotel_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "item_name", nullable = false, length = 150, unique = true)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity = 0;

    // Alert staff when stock falls below this number
    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 10;

    @Column(length = 50)
    private String unit;  // e.g. "pieces", "bottles", "kg"

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ItemCategory {
        LINEN,          // bed sheets, towels, pillow covers
        TOILETRIES,     // soap, shampoo, toothbrush kits
        FOOD_BEVERAGE,  // kitchen stock
        CLEANING,       // detergents, mops
        MAINTENANCE     // light bulbs, tools
    }

    // Helper: check if restock is needed
    @Transient
    public boolean isLowStock() {
        return this.quantity <= this.lowStockThreshold;
    }
}
