package com.example.hotel_management_system.service.impl;

import com.example.hotel_management_system.exception.ResourceNotFoundException;
import com.example.hotel_management_system.model.Bill;
import com.example.hotel_management_system.model.Booking;
import com.example.hotel_management_system.model.FoodOrder;
import com.example.hotel_management_system.repository.BillRepository;
import com.example.hotel_management_system.repository.BookingRepository;
import com.example.hotel_management_system.repository.FoodOrderRepository;
import com.example.hotel_management_system.service.BillService;
import com.example.hotel_management_system.service.FoodOrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class FoodOrderServiceImpl implements FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;
    private final BillService billService;

    public FoodOrderServiceImpl(FoodOrderRepository foodOrderRepository,
                                BookingRepository bookingRepository,
                                BillRepository billRepository,
                                @Lazy BillService billService) {
        this.foodOrderRepository = foodOrderRepository;
        this.bookingRepository = bookingRepository;
        this.billRepository = billRepository;
        this.billService = billService;
    }

    @Override
    public FoodOrder createFoodOrder(Long bookingId, String itemName, Integer quantity, BigDecimal unitPrice) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        FoodOrder order = new FoodOrder();
        order.setBooking(booking);
        order.setItemName(itemName);
        order.setQuantity(quantity);
        order.setUnitPrice(unitPrice);
        order.setStatus(FoodOrder.OrderStatus.PENDING);

        FoodOrder savedOrder = foodOrderRepository.save(order);

        // If a bill has already been created (e.g. check-out in progress or generated), update it
        billRepository.findByBookingId(bookingId).ifPresent(bill -> billService.createBillForBooking(booking));

        return savedOrder;
    }

    @Override
    public List<FoodOrder> getFoodOrdersByBookingId(Long bookingId) {
        return foodOrderRepository.findByBookingId(bookingId);
    }

    @Override
    public List<FoodOrder> getPendingFoodOrders() {
        return foodOrderRepository.findByStatus(FoodOrder.OrderStatus.PENDING);
    }

    @Override
    public FoodOrder updateOrderStatus(Long orderId, FoodOrder.OrderStatus status) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Food order not found with ID: " + orderId));
        order.setStatus(status);
        FoodOrder savedOrder = foodOrderRepository.save(order);

        // Recalculate bill if it exists
        billRepository.findByBookingId(order.getBooking().getId()).ifPresent(bill -> billService.createBillForBooking(order.getBooking()));

        return savedOrder;
    }
}
