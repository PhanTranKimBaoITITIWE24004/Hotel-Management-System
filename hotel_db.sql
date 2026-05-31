USE hotel_db;

-- 1. Insert Guests
INSERT INTO guests (full_name, email, phone, id_type, id_number, address) VALUES
('Nguyen Van A', 'nguyenvana@example.com', '0901234567', 'NATIONAL_ID', '079090123456', 'Ho Chi Minh City, VN'),
('Tran Thi B', 'tranthib@example.com', '0912345678', 'PASSPORT', 'B1234567', 'Hanoi, VN'),
('Le Van C', 'levanc@example.com', '0923456789', 'DRIVING_LICENSE', '891234567', 'Da Nang, VN');

-- 2. Insert Room Categories
INSERT INTO room_categories (name, description, price_per_night, max_occupancy) VALUES
('Standard', 'Cozy standard room with a queen-size bed.', 50.00, 2),
('Deluxe', 'Spacious room with a king-size bed and city view.', 100.00, 2),
('Suite', 'Premium suite with a separate living room and luxury amenities.', 250.00, 4);

-- 3. Insert Rooms
INSERT INTO rooms (room_number, category_id, floor, status) VALUES
('101', 1, 1, 'AVAILABLE'),
('102', 1, 1, 'CLEANING'),
('103', 1, 1, 'MAINTENANCE'),
('201', 2, 2, 'OCCUPIED'),
('202', 2, 2, 'AVAILABLE'),
('301', 3, 3, 'OCCUPIED');

-- 4. Insert Staff 
-- Note: password_hash uses a placeholder representation of a BCrypt hash for 'password123'
INSERT INTO staff (full_name, email, password_hash, role, phone, status, created_at) VALUES
('System Admin', 'admin@luxestay.com', '$2a$12$KUfR2hF0Je6bLVO55o6GJerdOpHTmYR8V0QqBZCzh0nXR6tSP9Gvu', 'ADMIN', '0987654321', 'ACTIVE', NOW()),
('Receptionist Minh', 'minh.reception@luxestay.com', '$2a$12$KUfR2hF0Je6bLVO55o6GJerdOpHTmYR8V0QqBZCzh0nXR6tSP9Gvu', 'RECEPTIONIST', '0987654322', 'ACTIVE', NOW()),
('Housekeeper Hoa', 'hoa.hk@luxestay.com', '$2a$12$KUfR2hF0Je6bLVO55o6GJerdOpHTmYR8V0QqBZCzh0nXR6tSP9Gvu', 'HOUSEKEEPING', '0987654323', 'ACTIVE', NOW()),
('Chef Tuan', 'tuan.food@luxestay.com', '$2a$12$KUfR2hF0Je6bLVO55o6GJerdOpHTmYR8V0QqBZCzh0nXR6tSP9Gvu', 'FOOD_SERVICE', '0987654324', 'ACTIVE', NOW());

-- 5. Insert Bookings
INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, actual_check_in, actual_check_out, payment_method, status) VALUES
(1, 4, '2026-06-01', '2026-06-05', '2026-06-01 14:00:00', NULL, 'ONLINE', 'CHECKED_IN'),
(2, 6, '2026-05-28', '2026-05-30', '2026-05-28 15:30:00', '2026-05-30 11:00:00', 'CASH', 'CHECKED_OUT'),
(3, 5, '2026-06-10', '2026-06-15', NULL, NULL, 'ONLINE', 'RESERVED');

-- 6. Insert Food Orders
INSERT INTO food_orders (booking_id, item_name, quantity, unit_price, status, ordered_at) VALUES
(1, 'Room Service Breakfast', 2, 15.00, 'DELIVERED', '2026-06-02 08:00:00'),
(1, 'Iced Coffee', 1, 5.00, 'PREPARING', NOW()),
(2, 'Seafood Dinner Set', 1, 45.00, 'DELIVERED', '2026-05-28 19:30:00');

-- 7. Insert Bills
-- Booking 2 is CHECKED_OUT, so it has a final bill. Booking 1 is ongoing, so the bill is UNPAID.
INSERT INTO bills (booking_id, room_charges, food_charges, total_amount, status, paid_at, created_at) VALUES
(2, 500.00, 45.00, 545.00, 'PAID', '2026-05-30 11:05:00', '2026-05-30 11:00:00'),
(1, 400.00, 35.00, 435.00, 'UNPAID', NULL, NOW());

-- 8. Insert Housekeeping Logs
INSERT INTO housekeeping_logs (room_id, staff_id, task_type, status, notes, completed_at, created_at) VALUES
(2, 3, 'CLEANING', 'IN_PROGRESS', 'Guest checked out early, needs deep cleaning', NULL, NOW()),
(3, 3, 'MAINTENANCE', 'PENDING', 'Air conditioning unit leaking', NULL, NOW()),
(6, 3, 'INSPECTION', 'DONE', 'Room ready for next guest', '2026-05-30 14:00:00', '2026-05-30 13:00:00');

-- 9. Insert Inventory
INSERT INTO inventory (item_name, category, quantity, low_stock_threshold, unit, updated_at) VALUES
('Bath Towels', 'LINEN', 45, 10, 'pieces', NOW()),
('Bed Sheets', 'LINEN', 25, 8, 'pieces', NOW()),
('Shampoo Bottles', 'TOILETRIES', 4, 15, 'bottles', NOW()), -- Triggers low stock alert
('Detergent', 'CLEANING', 12, 5, 'kg', NOW()),
('Light Bulbs', 'MAINTENANCE', 3, 10, 'pieces', NOW()); -- Triggers low stock alert
