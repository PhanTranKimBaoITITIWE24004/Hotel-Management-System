package com.example.hotel_management_system.controller.api;

import com.example.hotel_management_system.model.Bill;
import com.example.hotel_management_system.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillRestController {

    private final BillService billService;

    public BillRestController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Bill> getBillByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(billService.getBillByBookingId(bookingId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Bill> payBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.payBill(id));
    }
}
