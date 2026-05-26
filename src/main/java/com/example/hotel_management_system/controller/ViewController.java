package com.example.hotel_management_system.controller;

import com.example.hotel_management_system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final HousekeepingService housekeepingService;
    private final InventoryService inventoryService;
    private final StaffService staffService;

    public ViewController(RoomService roomService,
                          BookingService bookingService,
                          HousekeepingService housekeepingService,
                          InventoryService inventoryService,
                          StaffService staffService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.housekeepingService = housekeepingService;
        this.inventoryService = inventoryService;
        this.staffService = staffService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "index"; // Maps to templates/index.html
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
}
