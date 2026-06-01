package com.example.hotel_management_system.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hotel_management_system.dto.BookingRequestDTO;
import com.example.hotel_management_system.model.Guest;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.repository.BillRepository;
import com.example.hotel_management_system.service.BookingService;
import com.example.hotel_management_system.service.GuestService;
import com.example.hotel_management_system.service.HousekeepingService;
import com.example.hotel_management_system.service.InventoryService;
import com.example.hotel_management_system.service.RoomService;
import com.example.hotel_management_system.service.StaffService;

@Controller
public class ViewController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final HousekeepingService housekeepingService;
    private final InventoryService inventoryService;
    private final StaffService staffService;
    private final GuestService guestService;
    private final BillRepository billRepository;

    public ViewController(RoomService roomService,
                          BookingService bookingService,
                          HousekeepingService housekeepingService,
                          InventoryService inventoryService,
                          StaffService staffService,
                          GuestService guestService,
                          BillRepository billRepository) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.housekeepingService = housekeepingService;
        this.inventoryService = inventoryService;
        this.staffService = staffService;
        this.guestService = guestService;
        this.billRepository = billRepository;
    }

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login"; // Maps to templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("categories", roomService.getAllCategories());
        model.addAttribute("housekeepingTasks", housekeepingService.getAllTasks());
        model.addAttribute("inventoryItems", inventoryService.getAllInventory());
        model.addAttribute("lowStockItems", inventoryService.getLowStockItems());
        model.addAttribute("staffMembers", staffService.getAllStaff());
        return "dashboard"; // Maps to templates/dashboard.html
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        var rooms = roomService.getAllRooms();
        var categories = roomService.getAllCategories();
        model.addAttribute("rooms", rooms);
        model.addAttribute("categories", categories);
        model.addAttribute("totalRooms", rooms.size());          
        model.addAttribute("totalCategories", categories.size());
        return "rooms/list";
    }

    @GetMapping("/rooms/new")
    public String newRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("categories", roomService.getAllCategories());
        return "rooms/form";
    }

    @GetMapping("/rooms/{id}/edit")
    public String editRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        model.addAttribute("categories", roomService.getAllCategories());
        return "rooms/form";
    }

    @PostMapping("/rooms")
    public String saveRoom(@ModelAttribute("room") Room room,
                           @RequestParam(required = false) Long categoryId,
                           Model model) {
        try {
            if (room.getId() == null) {
                roomService.createRoom(room, categoryId);
            } else {
                roomService.updateRoom(room.getId(), room, categoryId);
            }
            return "redirect:/rooms";
        } catch (RuntimeException ex) {                 // re-show form on failure
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("categories", roomService.getAllCategories());
            return "rooms/form";
        }
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    @GetMapping({"/bookings/new"})
    public String bookingForm(Model model) {
        model.addAttribute("booking", new BookingRequestDTO());
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("categories", roomService.getAllCategories());
        return "bookings/form";
    }

    @PostMapping("/bookings")
    public String createBooking(@RequestParam Long guestId,
                                @RequestParam Long roomId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                @RequestParam String paymentMethod,
                                Model model) {
        try {
            // Basic date validation (previously done in the browser)
            if (!checkOutDate.isAfter(checkInDate)) {
                throw new IllegalArgumentException("Check-out date must be after the check-in date.");
            }
            if (checkInDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Check-in date cannot be in the past.");
            }
    
            Guest guest = guestService.getGuestById(guestId);
 
            BookingRequestDTO dto = new BookingRequestDTO();
            dto.setGuestName(guest.getFullName());
            dto.setGuestEmail(guest.getEmail());
            dto.setGuestPhone(guest.getPhone());
            dto.setGuestIdNumber(guest.getIdNumber());
            dto.setIdType(guest.getIdType() != null ? guest.getIdType().name() : null);
            dto.setRoomId(roomId);
            dto.setCheckInDate(checkInDate);
            dto.setCheckOutDate(checkOutDate);
            dto.setPaymentMethod(paymentMethod);
 
            bookingService.createBooking(dto);
            return "redirect:/bookings";
            
        } catch (RuntimeException ex) {                 // re-show form with the error
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("categories", roomService.getAllCategories());
            return "bookings/form";
        }
    }

    @GetMapping("/guests")
    public String guests(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        return "guests/list";
    }

    @GetMapping("/guests/new")
    public String newGuestForm(Model model) {
        model.addAttribute("guest", new Guest());
        return "guests/form";
    }

    @GetMapping("/guests/{id}/edit")
    public String editGuestForm(@PathVariable Long id, Model model) {
        model.addAttribute("guest", guestService.getGuestById(id));
        return "guests/form";
    }

    @PostMapping("/guests")
    public String saveGuest(@ModelAttribute("guest") Guest guest, Model model) {
        try {
            if (guest.getId() == null) {
                guestService.createGuest(guest);
            } else {
                guestService.updateGuest(guest.getId(), guest);
            }
            return "redirect:/guests";
        } catch (RuntimeException ex) {                 // re-show form on failure
            model.addAttribute("errorMessage", ex.getMessage());
            return "guests/form";
        }
    }

    @GetMapping("/staff")
    public String staff(Model model) {
        model.addAttribute("staffList", staffService.getAllStaff());
        return "staff/list";
    }

    @GetMapping("/inventory")
    public String inventory(Model model) {
        model.addAttribute("inventory", inventoryService.getAllInventory());
        return "inventory/list";
    }

    @GetMapping("/housekeeping")
    public String housekeeping(Model model) {
        model.addAttribute("housekeepingTasks", housekeepingService.getAllTasks());
        return "housekeeping/board";
    }

    @GetMapping({"/billing", "/billing/list"})
    public String billing(Model model) {
        model.addAttribute("bills", billRepository.findAll());
        return "billing/list";
    }

    @GetMapping({"/food", "/food-orders"})
    public String foodOrders() {
        return "food/orders";
    }
}