package com.example.hotel_management_system.config;

import com.example.hotel_management_system.model.Inventory;
import com.example.hotel_management_system.model.Room;
import com.example.hotel_management_system.model.RoomCategory;
import com.example.hotel_management_system.model.Staff;
import com.example.hotel_management_system.repository.InventoryRepository;
import com.example.hotel_management_system.repository.RoomCategoryRepository;
import com.example.hotel_management_system.repository.RoomRepository;
import com.example.hotel_management_system.repository.StaffRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(StaffRepository staffRepository,
                          RoomCategoryRepository roomCategoryRepository,
                          RoomRepository roomRepository,
                          InventoryRepository inventoryRepository,
                          PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.roomCategoryRepository = roomCategoryRepository;
        this.roomRepository = roomRepository;
        this.inventoryRepository = inventoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Staff
        if (staffRepository.count() == 0) {
            Staff admin = new Staff();
            admin.setFullName("LuxeStay Administrator");
            admin.setEmail("admin@luxestay.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Staff.StaffRole.ADMIN);
            admin.setPhone("0987654321");
            admin.setStatus(Staff.StaffStatus.ACTIVE);
            staffRepository.save(admin);

            Staff receptionist = new Staff();
            receptionist.setFullName("John Receptionist");
            receptionist.setEmail("reception@luxestay.com");
            receptionist.setPasswordHash(passwordEncoder.encode("reception123"));
            receptionist.setRole(Staff.StaffRole.RECEPTIONIST);
            receptionist.setPhone("0987654322");
            receptionist.setStatus(Staff.StaffStatus.ACTIVE);
            staffRepository.save(receptionist);

            Staff housekeeper = new Staff();
            housekeeper.setFullName("Mary Housekeeper");
            housekeeper.setEmail("housekeeper@luxestay.com");
            housekeeper.setPasswordHash(passwordEncoder.encode("clean123"));
            housekeeper.setRole(Staff.StaffRole.HOUSEKEEPING);
            housekeeper.setPhone("0987654323");
            housekeeper.setStatus(Staff.StaffStatus.ACTIVE);
            staffRepository.save(housekeeper);
        }

        // Seed Room Categories
        if (roomCategoryRepository.count() == 0) {
            RoomCategory standard = new RoomCategory();
            standard.setName("Standard");
            standard.setDescription("Cozy standard room with a queen-size bed.");
            standard.setPricePerNight(BigDecimal.valueOf(50.00));
            standard.setMaxOccupancy(2);

            RoomCategory deluxe = new RoomCategory();
            deluxe.setName("Deluxe");
            deluxe.setDescription("Spacious room with a king-size bed and city view.");
            deluxe.setPricePerNight(BigDecimal.valueOf(100.00));
            deluxe.setMaxOccupancy(2);

            RoomCategory suite = new RoomCategory();
            suite.setName("Suite");
            suite.setDescription("Premium suite with a separate living room and luxury amenities.");
            suite.setPricePerNight(BigDecimal.valueOf(250.00));
            suite.setMaxOccupancy(4);

            roomCategoryRepository.saveAll(List.of(standard, deluxe, suite));
        }

        // Seed Rooms
        if (roomRepository.count() == 0) {
            List<RoomCategory> categories = roomCategoryRepository.findAll();
            RoomCategory standard = categories.stream().filter(c -> c.getName().equals("Standard")).findFirst().orElse(null);
            RoomCategory deluxe = categories.stream().filter(c -> c.getName().equals("Deluxe")).findFirst().orElse(null);
            RoomCategory suite = categories.stream().filter(c -> c.getName().equals("Suite")).findFirst().orElse(null);

            if (standard != null && deluxe != null && suite != null) {
                // Floor 1 (Standard)
                createRoom("101", standard, 1);
                createRoom("102", standard, 1);
                createRoom("103", standard, 1);

                // Floor 2 (Deluxe)
                createRoom("201", deluxe, 2);
                createRoom("202", deluxe, 2);

                // Floor 3 (Suite)
                createRoom("301", suite, 3);
            }
        }

        // Seed Inventory
        if (inventoryRepository.count() == 0) {
            createInventoryItem("Bath Towels", Inventory.ItemCategory.LINEN, 50, 10, "pieces");
            createInventoryItem("Bed Sheets", Inventory.ItemCategory.LINEN, 30, 8, "pieces");
            createInventoryItem("Shampoo Bottles", Inventory.ItemCategory.TOILETRIES, 5, 15, "bottles"); // Low stock threshold trigger
            createInventoryItem("Detergent", Inventory.ItemCategory.CLEANING, 12, 5, "kg");
        }
    }

    private void createRoom(String roomNumber, RoomCategory category, Integer floor) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setCategory(category);
        room.setFloor(floor);
        room.setStatus(Room.RoomStatus.AVAILABLE);
        roomRepository.save(room);
    }

    private void createInventoryItem(String itemName, Inventory.ItemCategory category, Integer quantity, Integer lowThreshold, String unit) {
        Inventory item = new Inventory();
        item.setItemName(itemName);
        item.setCategory(category);
        item.setQuantity(quantity);
        item.setLowStockThreshold(lowThreshold);
        item.setUnit(unit);
        inventoryRepository.save(item);
    }
}
