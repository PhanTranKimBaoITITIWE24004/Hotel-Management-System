package com.example.hotel_management_system.repository;

import com.example.hotel_management_system.model.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
}
