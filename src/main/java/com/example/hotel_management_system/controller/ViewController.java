package com.example.hotel_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.hotel_management_system.repository.BillRepository;
import com.example.hotel_management_system.service.BookingService;
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
    private final BillRepository billRepository;

    public ViewController(RoomService roomService,
                          BookingService bookingService,
                          HousekeepingService housekeepingService,
                          InventoryService inventoryService,
                          StaffService staffService,
                          BillRepository billRepository) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.housekeepingService = housekeepingService;
        this.inventoryService = inventoryService;
        this.staffService = staffService;
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
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("categories", roomService.getAllCategories());
        return "rooms/list";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    @GetMapping({"/bookings/new", "/bookings/form"})
    public String bookingForm(Model model) {
        model.addAttribute("booking", new com.example.hotel_management_system.dto.BookingRequestDTO());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("categories", roomService.getAllCategories());
        return "bookings/form";
    }

    @GetMapping("/guests")
    public String guests() {
        return "guests/list";
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
