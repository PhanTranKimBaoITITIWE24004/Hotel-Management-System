package com.example.hotel_management_system.controller.api;

import com.example.hotel_management_system.model.HousekeepingLog;
import com.example.hotel_management_system.service.HousekeepingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/housekeeping")
public class HousekeepingRestController {

    private final HousekeepingService housekeepingService;

    public HousekeepingRestController(HousekeepingService housekeepingService) {
        this.housekeepingService = housekeepingService;
    }

    @PostMapping("/assign")
    public ResponseEntity<HousekeepingLog> assignTask(@RequestBody Map<String, Object> body) {
        Long roomId = Long.valueOf(body.get("roomId").toString());
        Long staffId = Long.valueOf(body.get("staffId").toString());
        String taskTypeStr = body.get("taskType").toString();
        String notes = body.get("notes") != null ? body.get("notes").toString() : "";

        HousekeepingLog.TaskType taskType = HousekeepingLog.TaskType.valueOf(taskTypeStr.toUpperCase());
        HousekeepingLog log = housekeepingService.assignTask(roomId, staffId, taskType, notes);
        return new ResponseEntity<>(log, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<HousekeepingLog> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        String notes = body.get("notes");
        if (statusStr == null) {
            throw new IllegalArgumentException("Status value is required");
        }
        HousekeepingLog.TaskStatus status = HousekeepingLog.TaskStatus.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(housekeepingService.updateTaskStatus(id, status, notes));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<HousekeepingLog>> getPendingTasks() {
        return ResponseEntity.ok(housekeepingService.getPendingTasks());
    }

    @GetMapping
    public ResponseEntity<List<HousekeepingLog>> getAllTasks() {
        return ResponseEntity.ok(housekeepingService.getAllTasks());
    }
}
