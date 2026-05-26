package com.example.hotel_management_system.service;

import com.example.hotel_management_system.model.HousekeepingLog;
import java.util.List;

public interface HousekeepingService {
    HousekeepingLog assignTask(Long roomId, Long staffId, HousekeepingLog.TaskType taskType, String notes);
    HousekeepingLog updateTaskStatus(Long logId, HousekeepingLog.TaskStatus status, String notes);
    List<HousekeepingLog> getLogsByRoomId(Long roomId);
    List<HousekeepingLog> getLogsByStaffId(Long staffId);
    List<HousekeepingLog> getPendingTasks();
    List<HousekeepingLog> getAllTasks();
}
