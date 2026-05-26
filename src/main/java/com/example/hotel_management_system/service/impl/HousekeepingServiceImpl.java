package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.HousekeepingLog;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.Staff;
import com.example.hotel_management_system.repository.HousekeepingLogRepository;
import com.example.hotel_management_system.repository.RoomRepository;
import com.example.hotel_management_system.repository.StaffRepository;
import com.example.hotel_management_system.service.HousekeepingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class HousekeepingServiceImpl implements HousekeepingService {

    private final HousekeepingLogRepository housekeepingLogRepository;
    private final RoomRepository roomRepository;
    private final StaffRepository staffRepository;

    public HousekeepingServiceImpl(HousekeepingLogRepository housekeepingLogRepository,
                                   RoomRepository roomRepository,
                                   StaffRepository staffRepository) {
        this.housekeepingLogRepository = housekeepingLogRepository;
        this.roomRepository = roomRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public HousekeepingLog assignTask(Long roomId, Long staffId, HousekeepingLog.TaskType taskType, String notes) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + staffId));

        HousekeepingLog log = new HousekeepingLog();
        log.setRoom(room);
        log.setStaff(staff);
        log.setTaskType(taskType);
        log.setStatus(HousekeepingLog.TaskStatus.PENDING);
        log.setNotes(notes);

        // Update Room Status
        if (taskType == HousekeepingLog.TaskType.CLEANING) {
            room.setStatus(Room.RoomStatus.CLEANING);
        } else if (taskType == HousekeepingLog.TaskType.MAINTENANCE) {
            room.setStatus(Room.RoomStatus.MAINTENANCE);
        }
        roomRepository.save(room);

        return housekeepingLogRepository.save(log);
    }

    @Override
    public HousekeepingLog updateTaskStatus(Long logId, HousekeepingLog.TaskStatus status, String notes) {
        HousekeepingLog log = housekeepingLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Housekeeping log not found with ID: " + logId));

        log.setStatus(status);
        if (notes != null && !notes.isBlank()) {
            log.setNotes(log.getNotes() + "\nUpdate: " + notes);
        }

        if (status == HousekeepingLog.TaskStatus.DONE) {
            log.setCompletedAt(LocalDateTime.now());
            // Restore Room Status to AVAILABLE
            Room room = log.getRoom();
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);
        } else if (status == HousekeepingLog.TaskStatus.IN_PROGRESS) {
            // Keep room status as CLEANING/MAINTENANCE
            Room room = log.getRoom();
            if (log.getTaskType() == HousekeepingLog.TaskType.CLEANING && room.getStatus() != Room.RoomStatus.CLEANING) {
                room.setStatus(Room.RoomStatus.CLEANING);
                roomRepository.save(room);
            }
        }

        return housekeepingLogRepository.save(log);
    }

    @Override
    public List<HousekeepingLog> getLogsByRoomId(Long roomId) {
        return housekeepingLogRepository.findByRoomId(roomId);
    }

    @Override
    public List<HousekeepingLog> getLogsByStaffId(Long staffId) {
        return housekeepingLogRepository.findByStaffId(staffId);
    }

    @Override
    public List<HousekeepingLog> getPendingTasks() {
        return housekeepingLogRepository.findByStatus(HousekeepingLog.TaskStatus.PENDING);
    }

    @Override
    public List<HousekeepingLog> getAllTasks() {
        return housekeepingLogRepository.findAll();
    }
}
