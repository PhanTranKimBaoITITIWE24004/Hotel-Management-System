package com.example.hotel_management_system.service;

import com.example.hotel_management_system.model.Guest;
import java.util.List;

public interface GuestService {
    Guest getOrCreateGuest(String fullName, String email, String phone, String idType, String idNumber);
    Guest getGuestById(Long id);
    List<Guest> getAllGuests();
    Guest updateGuest(Long id, Guest guestDetails);
    Guest createGuest(Guest guest);
}
