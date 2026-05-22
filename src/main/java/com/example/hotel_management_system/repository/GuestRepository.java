package com.example.hotel_management.repository;


import com.example.hotel_management.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    List<Guest> findByFullNameContainingIgnoreCase(String name);

    boolean existsByIdNumber(String idNumber);
}