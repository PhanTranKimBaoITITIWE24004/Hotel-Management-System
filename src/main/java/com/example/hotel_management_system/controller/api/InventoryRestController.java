package com.example.hotel_management_system.controller.api;

import com.example.hotel_management_system.model.Inventory;
import com.example.hotel_management_system.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {

    private final InventoryService inventoryService;

    public InventoryRestController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventoryItem(@RequestBody Inventory item) {
        Inventory createdItem = inventoryService.createInventoryItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<Inventory> updateInventoryQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer quantity = body.get("quantity");
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity value is required");
        }
        return ResponseEntity.ok(inventoryService.updateInventoryQuantity(id, quantity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventoryItem(@PathVariable Long id, @RequestBody Inventory itemDetails) {
        return ResponseEntity.ok(inventoryService.updateInventoryItem(id, itemDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.ok().build();
    }
}
