package com.example.hotel_management_system.service;

import com.example.hotel_management_system.dto.RoomDTO;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.RoomCategory;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<RoomDTO> getAllRooms();
    Room getRoomById(Long id);
    RoomDTO getRoomDTOById(Long id);
    Room createRoom(Room room, Long categoryId);
    Room updateRoom(Long id, Room roomDetails, Long categoryId);
    void updateRoomStatus(Long id, Room.RoomStatus status);
    List<RoomCategory> getAllCategories();
    RoomCategory getCategoryById(Long id);
    RoomCategory createCategory(RoomCategory category);
    List<RoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);
    RoomCategory updateCategory(Long id, RoomCategory categoryDetails);
    void deleteCategory(Long id);
}
