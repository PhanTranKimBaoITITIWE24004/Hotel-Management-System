package com.example.hotel_management_system.controller.api;

import com.example.hotel_management_system.dto.RoomDTO;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.RoomCategory;
import com.example.hotel_management_system.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping
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

    @GetMapping("/categories")
    public ResponseEntity<List<RoomCategory>> getAllCategories() {
        return ResponseEntity.ok(roomService.getAllCategories());
    }
}
