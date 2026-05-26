package com.example.hotel_management_system.security;

import com.example.hotel_management_system.model.Staff;
import com.example.hotel_management_system.repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public CustomUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Staff member not found with email: " + email));

        if (staff.getStatus() == Staff.StaffStatus.INACTIVE) {
            throw new UsernameNotFoundException("Staff member account is inactive: " + email);
        }

        return User.withUsername(staff.getEmail())
                .password(staff.getPasswordHash())
                .roles(staff.getRole().name())
                .build();
    }
}
