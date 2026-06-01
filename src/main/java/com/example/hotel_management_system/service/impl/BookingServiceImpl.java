package com.example.hotel_management_system.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel_management_system.dto.BookingRequestDTO;
import com.example.hotel_management_system.dto.BookingResponseDTO;
import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Booking;
import com.example.hotel_management_system.model.Guest;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.repository.BookingRepository;
import com.example.hotel_management_system.service.BillService;
import com.example.hotel_management_system.service.BookingService;
import com.example.hotel_management_system.service.GuestService;
import com.example.hotel_management_system.service.RoomService;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final GuestService guestService;
    private final RoomService roomService;
    private final BillService billService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              GuestService guestService,
                              RoomService roomService,
                              @Lazy BillService billService) {
        this.bookingRepository = bookingRepository;
        this.guestService = guestService;
        this.roomService = roomService;
        this.billService = billService;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Room room = roomService.getRoomById(request.getRoomId());
        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not available for booking (Current status: " + room.getStatus() + ")");
        }

        // Check if room is already booked for the given dates
        if (bookingRepository.isRoomBooked(request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is already booked during the selected dates: " 
                    + request.getCheckInDate() + " to " + request.getCheckOutDate());
        }

        // Retrieve or register guest
        Guest guest = guestService.getOrCreateGuest(
                request.getGuestName(),
                request.getGuestEmail(),
                request.getGuestPhone(),
                request.getIdType(),
                request.getGuestIdNumber()
        );

        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setPaymentMethod(Booking.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        booking.setStatus(Booking.BookingStatus.RESERVED);

        Booking savedBooking = bookingRepository.save(booking);
        return convertToDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO getBookingDTOById(Long id) {
        return convertToDTO(getBookingById(id));
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO checkIn(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() != Booking.BookingStatus.RESERVED) {
            throw new IllegalStateException("Cannot check-in booking in status: " + booking.getStatus());
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_IN);
        booking.setActualCheckIn(LocalDateTime.now());

        // Update room status to occupied
        roomService.updateRoomStatus(booking.getRoom().getId(), Room.RoomStatus.OCCUPIED);

        Booking savedBooking = bookingRepository.save(booking);
        return convertToDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO checkOut(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() != Booking.BookingStatus.CHECKED_IN) {
            throw new IllegalStateException("Cannot check-out booking in status: " + booking.getStatus());
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_OUT);
        booking.setActualCheckOut(LocalDateTime.now());

        // Update room status to cleaning
        roomService.updateRoomStatus(booking.getRoom().getId(), Room.RoomStatus.CLEANING);

        Booking savedBooking = bookingRepository.save(booking);

        // Generate the final invoice
        billService.createBillForBooking(savedBooking);

        return convertToDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() != Booking.BookingStatus.RESERVED) {
            throw new IllegalStateException("Only RESERVED bookings can be cancelled.");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);
        return convertToDTO(savedBooking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuestId(Long guestId) {
        return bookingRepository.findByGuestId(guestId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setGuestId(booking.getGuest().getId());
        dto.setGuestName(booking.getGuest().getFullName());
        dto.setGuestEmail(booking.getGuest().getEmail());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setRoomCategory(booking.getRoom().getCategory().getName());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setActualCheckIn(booking.getActualCheckIn());
        dto.setActualCheckOut(booking.getActualCheckOut());
        dto.setPaymentMethod(booking.getPaymentMethod().name());
        dto.setStatus(booking.getStatus().name());

        // Safely check for the bill without throwing an exception
        billService.findBillByBookingId(booking.getId()).ifPresent(bill -> {
            dto.setRoomCharges(bill.getRoomCharges());
            dto.setFoodCharges(bill.getFoodCharges());
            dto.setTotalAmount(bill.getTotalAmount());
            dto.setBillStatus(bill.getStatus().name());
        });

        return dto;
    }
}
