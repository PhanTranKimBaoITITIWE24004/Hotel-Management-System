package com.example.hotel_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel_management_system.model.HousekeepingLog;
import com.example.hotel_management_system.model.HousekeepingLog.TaskStatus;
import com.example.hotel_management_system.model.HousekeepingLog.TaskType;

public interface HousekeepingLogRepository extends JpaRepository<HousekeepingLog, Long> {

    // All tasks assigned to one staff member
    List<HousekeepingLog> findByStaffId(Long staffId);

    // All tasks for one room (history of what was done)
    List<HousekeepingLog> findByRoomId(Long roomId);

    // Tasks filtered by status — used for the housekeeping dashboard board
    List<HousekeepingLog> findByStatus(TaskStatus status);

    // All pending tasks for a specific staff member
    List<HousekeepingLog> findByStaffIdAndStatus(Long staffId, TaskStatus status);

    // All tasks of a given type for a room
    // e.g. last time room 101 had CLEANING done
    List<HousekeepingLog> findByRoomIdAndTaskType(Long roomId, TaskType taskType);

    // Count tasks by status (used for dashboard stats)
    @Query("SELECT COUNT(h) FROM HousekeepingLog h WHERE h.status = :status")
    long countByStatus(@Param("status") TaskStatus status);
}
