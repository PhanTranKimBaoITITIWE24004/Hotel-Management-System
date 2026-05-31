package com.example.hotel_management_system.controller.api;

<<<<<<< HEAD
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

=======
>>>>>>> ea14cc387d9e4cdde4e20a80bb223d7c1d68f6cd
import com.example.hotel_management_system.dto.RoomDTO;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.RoomCategory;
import com.example.hotel_management_system.service.RoomService;
<<<<<<< HEAD
=======
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
>>>>>>> ea14cc387d9e4cdde4e20a80bb223d7c1d68f6cd

@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomDTOById(id));
    }

<<<<<<< HEAD
    @PostMapping("/new")
=======
    @PostMapping
>>>>>>> ea14cc387d9e4cdde4e20a80bb223d7c1d68f6cd
    public ResponseEntity<Room> createRoom(@RequestBody Room room, @RequestParam Long categoryId) {
        Room createdRoom = roomService.createRoom(room, categoryId);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails, @RequestParam Long categoryId) {
        Room updatedRoom = roomService.updateRoom(id, roomDetails, categoryId);
        return ResponseEntity.ok(updatedRoom);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateRoomStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        if (statusStr == null) {
            throw new IllegalArgumentException("Status value is required");
        }
        Room.RoomStatus status = Room.RoomStatus.valueOf(statusStr.toUpperCase());
        roomService.updateRoomStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return ResponseEntity.ok(roomService.getAvailableRooms(checkIn, checkOut));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<RoomCategory>> getAllCategories() {
        return ResponseEntity.ok(roomService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<RoomCategory> createCategory(@RequestBody RoomCategory category) {
        RoomCategory created = roomService.createCategory(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<RoomCategory> updateCategory(@PathVariable Long id, @RequestBody RoomCategory categoryDetails) {
        RoomCategory updated = roomService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        roomService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
