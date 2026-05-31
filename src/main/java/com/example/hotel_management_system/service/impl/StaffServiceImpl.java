package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Staff;
import com.example.hotel_management_system.repository.StaffRepository;
import com.example.hotel_management_system.service.StaffService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Staff createStaff(Staff staff) {
        if (staffRepository.existsByEmail(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + staff.getEmail());
        }
        // Encode password
        staff.setPasswordHash(passwordEncoder.encode(staff.getPasswordHash()));
        staff.setStatus(Staff.StaffStatus.ACTIVE);
        return staffRepository.save(staff);
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));
    }

    @Override
    public Staff getStaffByEmail(String email) {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with email: " + email));
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Staff updateStaff(Long id, Staff staffDetails) {
        Staff staff = getStaffById(id);

        if (!staff.getEmail().equals(staffDetails.getEmail()) && staffRepository.existsByEmail(staffDetails.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + staffDetails.getEmail());
        }

        staff.setFullName(staffDetails.getFullName());
        staff.setEmail(staffDetails.getEmail());
        staff.setPhone(staffDetails.getPhone());
        if (staffDetails.getRole() != null) {
            staff.setRole(staffDetails.getRole());
        }
        if (staffDetails.getStatus() != null) {
            staff.setStatus(staffDetails.getStatus());
        }

        // If a new password is provided, re-encode and save
        if (staffDetails.getPasswordHash() != null && !staffDetails.getPasswordHash().isBlank()) {
            staff.setPasswordHash(passwordEncoder.encode(staffDetails.getPasswordHash()));
        }

        return staffRepository.save(staff);
    }

    @Override
    public void deactivateStaff(Long id) {
        Staff staff = getStaffById(id);
        staff.setStatus(Staff.StaffStatus.INACTIVE);
        staffRepository.save(staff);
    }
}
