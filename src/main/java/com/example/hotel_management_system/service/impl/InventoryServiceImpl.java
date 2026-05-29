package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Inventory;
import com.example.hotel_management_system.repository.InventoryRepository;
import com.example.hotel_management_system.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + id));
    }

    @Override
    public Inventory createInventoryItem(Inventory item) {
        return inventoryRepository.save(item);
    }

    @Override
    public Inventory updateInventoryQuantity(Long id, Integer quantity) {
        Inventory item = getInventoryById(id);
        item.setQuantity(quantity);
        return inventoryRepository.save(item);
    }

    @Override
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findAll().stream()
                .filter(Inventory::isLowStock)
                .collect(Collectors.toList());
    }

    @Override
    public Inventory updateInventoryItem(Long id, Inventory itemDetails) {
        Inventory item = getInventoryById(id);
        item.setItemName(itemDetails.getItemName());
        item.setCategory(itemDetails.getCategory());
        item.setQuantity(itemDetails.getQuantity());
        item.setLowStockThreshold(itemDetails.getLowStockThreshold());
        item.setUnit(itemDetails.getUnit());
        return inventoryRepository.save(item);
    }

    @Override
    public void deleteInventoryItem(Long id) {
        Inventory item = getInventoryById(id);
        inventoryRepository.delete(item);
    }
}
