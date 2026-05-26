package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Guest;
import com.example.hotel_management_system.repository.GuestRepository;
import com.example.hotel_management_system.service.GuestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest getOrCreateGuest(String fullName, String email, String phone, String idNumber) {
        // Try to find by email first
        Optional<Guest> existingByEmail = guestRepository.findByEmail(email);
        if (existingByEmail.isPresent()) {
            return existingByEmail.get();
        }

        // Try to find by id number
        Optional<Guest> existingById = guestRepository.findByIdNumber(idNumber);
        if (existingById.isPresent()) {
            return existingById.get();
        }

        // If not found, create new guest
        Guest newGuest = new Guest();
        newGuest.setFullName(fullName);
        newGuest.setEmail(email);
        newGuest.setPhone(phone);
        newGuest.setIdNumber(idNumber);
        newGuest.setIdType(Guest.IdType.NATIONAL_ID); // Defaulting to National ID
        return guestRepository.save(newGuest);
    }

    @Override
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with ID: " + id));
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest updateGuest(Long id, Guest guestDetails) {
        Guest guest = getGuestById(id);
        guest.setFullName(guestDetails.getFullName());
        guest.setEmail(guestDetails.getEmail());
        guest.setPhone(guestDetails.getPhone());
        guest.setIdNumber(guestDetails.getIdNumber());
        if (guestDetails.getIdType() != null) {
            guest.setIdType(guestDetails.getIdType());
        }
        guest.setAddress(guestDetails.getAddress());
        return guestRepository.save(guest);
    }
}
