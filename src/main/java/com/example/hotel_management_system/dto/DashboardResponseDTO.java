package com.example.hotel_management_system.dto;

import com.example.hotel_management_system.model.Inventory;
import java.util.List;
import java.util.Map;

public class DashboardResponseDTO {
    private Double totalRevenue;
    private Double occupancyRate;
    private Map<String, Long> taskCounts;
    private List<Inventory> lowStockSummary;

    public DashboardResponseDTO() {}

    public DashboardResponseDTO(Double totalRevenue, Double occupancyRate, Map<String, Long> taskCounts, List<Inventory> lowStockSummary) {
        this.totalRevenue = totalRevenue;
        this.occupancyRate = occupancyRate;
        this.taskCounts = taskCounts;
        this.lowStockSummary = lowStockSummary;
    }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(Double occupancyRate) { this.occupancyRate = occupancyRate; }

    public Map<String, Long> getTaskCounts() { return taskCounts; }
    public void setTaskCounts(Map<String, Long> taskCounts) { this.taskCounts = taskCounts; }

    public List<Inventory> getLowStockSummary() { return lowStockSummary; }
    public void setLowStockSummary(List<Inventory> lowStockSummary) { this.lowStockSummary = lowStockSummary; }
}
