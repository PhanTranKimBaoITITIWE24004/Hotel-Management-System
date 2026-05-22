package com.example.hotel_management.repository;

import com.example.hotel_management.model.Inventory;
import com.example.hotel_management.model.Inventory.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Find by exact item name
    Optional<Inventory> findByItemName(String itemName);

    // All items in a category (e.g. all LINEN items)
    List<Inventory> findByCategory(ItemCategory category);

    // All items that are running low and need restocking
    @Query("""
        SELECT i FROM Inventory i
        WHERE i.quantity <= i.lowStockThreshold
        ORDER BY i.quantity ASC
    """)
    List<Inventory> findLowStockItems();

    // Search by name (partial match, case-insensitive)
    List<Inventory> findByItemNameContainingIgnoreCase(String keyword);
}
