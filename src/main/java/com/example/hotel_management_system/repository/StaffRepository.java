package com.example.hotel_management.repository;

import com.example.hotel_management.model.Staff;
import com.example.hotel_management.model.Staff.StaffRole;
import com.example.hotel_management.model.Staff.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Used by Spring Security during login — finds staff by email
    Optional<Staff> findByEmail(String email);

    // Find all active staff with a given role
    // e.g. findByRoleAndStatus(HOUSEKEEPING, ACTIVE)
    List<Staff> findByRoleAndStatus(StaffRole role, StaffStatus status);

    // Check if an email is already registered (used during registration)
    boolean existsByEmail(String email);

    // List all staff by role (e.g. show all receptionists)
    List<Staff> findByRole(StaffRole role);
}
