package com.example.hotel_management_system.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel_management_system.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByIdNumber(String idNumber);

    List<Guest> findByFullNameContainingIgnoreCase(String name);

    boolean existsByIdNumber(String idNumber);
}