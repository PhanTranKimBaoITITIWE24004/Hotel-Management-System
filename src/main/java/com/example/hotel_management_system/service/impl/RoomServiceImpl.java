package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.dto.RoomDTO;
import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.RoomCategory;
import com.example.hotel_management_system.repository.RoomCategoryRepository;
import com.example.hotel_management_system.repository.RoomRepository;
import com.example.hotel_management_system.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomCategoryRepository roomCategoryRepository;

    public RoomServiceImpl(RoomRepository roomRepository, RoomCategoryRepository roomCategoryRepository) {
        this.roomRepository = roomRepository;
        this.roomCategoryRepository = roomCategoryRepository;
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
    }

    @Override
    public RoomDTO getRoomDTOById(Long id) {
        return convertToDTO(getRoomById(id));
    }

    @Override
    public Room createRoom(Room room, Long categoryId) {
        RoomCategory category = getCategoryById(categoryId);
        room.setCategory(category);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room roomDetails, Long categoryId) {
        Room room = getRoomById(id);
        RoomCategory category = getCategoryById(categoryId);
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setCategory(category);
        room.setFloor(roomDetails.getFloor());
        if (roomDetails.getStatus() != null) {
            room.setStatus(roomDetails.getStatus());
        }
        return roomRepository.save(room);
    }

    @Override
    public void updateRoomStatus(Long id, Room.RoomStatus status) {
        Room room = getRoomById(id);
        room.setStatus(status);
        roomRepository.save(room);
    }

    @Override
    public List<RoomCategory> getAllCategories() {
        return roomCategoryRepository.findAll();
    }

    @Override
    public RoomCategory getCategoryById(Long id) {
        return roomCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room Category not found with ID: " + id));
    }

    @Override
    public RoomCategory createCategory(RoomCategory category) {
        return roomCategoryRepository.save(category);
    }

    @Override
    public List<RoomDTO> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomCategory updateCategory(Long id, RoomCategory categoryDetails) {
        RoomCategory category = getCategoryById(id);
        category.setName(categoryDetails.getName());
        category.setPricePerNight(categoryDetails.getPricePerNight());
        category.setDescription(categoryDetails.getDescription());
        if (categoryDetails.getMaxOccupancy() != null) {
            category.setMaxOccupancy(categoryDetails.getMaxOccupancy());
        }
        return roomCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        RoomCategory category = getCategoryById(id);
        roomCategoryRepository.delete(category);
    }

    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getCategory() != null ? room.getCategory().getId() : null,
                room.getCategory() != null ? room.getCategory().getName() : null,
                room.getCategory() != null ? room.getCategory().getPricePerNight() : null,
                room.getFloor(),
                room.getStatus() != null ? room.getStatus().name() : null
        );
    }
}
