package com.example.hotel_management_system.dto;

import java.math.BigDecimal;

public class RoomDTO {
    private Long id;
    private String roomNumber;
    private Long categoryId;
    private String categoryName;
    private BigDecimal pricePerNight;
    private Integer floor;
    private String status;

    public RoomDTO() {}

    public RoomDTO(Long id, String roomNumber, Long categoryId, String categoryName, BigDecimal pricePerNight, Integer floor, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.pricePerNight = pricePerNight;
        this.floor = floor;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
