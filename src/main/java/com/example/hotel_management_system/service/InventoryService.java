package com.example.hotel_management_system.service;

import com.example.hotel_management_system.model.Inventory;
import java.util.List;

public interface InventoryService {
    List<Inventory> getAllInventory();
    Inventory getInventoryById(Long id);
    Inventory createInventoryItem(Inventory item);
    Inventory updateInventoryQuantity(Long id, Integer quantity);
    List<Inventory> getLowStockItems();
    Inventory updateInventoryItem(Long id, Inventory itemDetails);
    void deleteInventoryItem(Long id);
}
