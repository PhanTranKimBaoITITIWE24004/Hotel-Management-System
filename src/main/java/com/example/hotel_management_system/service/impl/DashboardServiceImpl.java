package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.dto.DashboardResponseDTO;
import com.example.hotel_management_system.model.HousekeepingLog;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.repository.BillRepository;
import com.example.hotel_management_system.repository.RoomRepository;
import com.example.hotel_management_system.repository.HousekeepingLogRepository;
import com.example.hotel_management_system.repository.InventoryRepository;
import com.example.hotel_management_system.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final HousekeepingLogRepository housekeepingLogRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardServiceImpl(BillRepository billRepository,
                                RoomRepository roomRepository,
                                HousekeepingLogRepository housekeepingLogRepository,
                                InventoryRepository inventoryRepository) {
        this.billRepository = billRepository;
        this.roomRepository = roomRepository;
        this.housekeepingLogRepository = housekeepingLogRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public DashboardResponseDTO getDashboardStats() {
        // 1. Total Revenue
        Double revenue = billRepository.getTotalRevenue();
        if (revenue == null) {
            revenue = 0.0;
        }

        // 2. Occupancy Rate
        long totalRooms = roomRepository.count();
        double occupancyRate = 0.0;
        if (totalRooms > 0) {
            long occupiedRooms = roomRepository.findByStatus(Room.RoomStatus.OCCUPIED).size();
            occupancyRate = ((double) occupiedRooms / totalRooms) * 100.0;
        }

        // 3. Task Counts by status
        Map<String, Long> taskCounts = new HashMap<>();
        for (HousekeepingLog.TaskStatus status : HousekeepingLog.TaskStatus.values()) {
            taskCounts.put(status.name(), housekeepingLogRepository.countByStatus(status));
        }

        // 4. Low-stock summary
        return new DashboardResponseDTO(
                revenue,
                occupancyRate,
                taskCounts,
                inventoryRepository.findLowStockItems()
        );
    }
}
