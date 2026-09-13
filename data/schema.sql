-- Local Service Booking Platform Schema

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    phone TEXT,
    role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'PROVIDER', 'ADMIN')),
    address TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS service_providers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE,
    business_name TEXT NOT NULL,
    description TEXT,
    rating REAL DEFAULT 0.0,
    available INTEGER DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS services (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    provider_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    category TEXT NOT NULL,
    price REAL NOT NULL,
    duration_minutes INTEGER DEFAULT 60,
    active INTEGER DEFAULT 1,
    FOREIGN KEY (provider_id) REFERENCES service_providers(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    service_id INTEGER NOT NULL,
    provider_id INTEGER NOT NULL,
    booking_date TEXT NOT NULL,
    booking_time TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED')),
    is_emergency INTEGER DEFAULT 0,
    notes TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (service_id) REFERENCES services(id),
    FOREIGN KEY (provider_id) REFERENCES service_providers(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    booking_id INTEGER NOT NULL UNIQUE,
    amount REAL NOT NULL,
    method TEXT NOT NULL CHECK(method IN ('CASH', 'UPI', 'CARD')),
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    transaction_ref TEXT,
    paid_at TEXT,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Seed: Admin user (password: admin123)
INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (1, 'Admin User', 'admin@localservice.com', 'admin123', '9999999999', 'ADMIN', 'Admin Office');

-- Seed: Sample customers (password: customer123)
INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (2, 'Rahul Sharma', 'rahul@email.com', 'customer123', '9876543210', 'CUSTOMER', '12 MG Road, Delhi');

INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (3, 'Priya Patel', 'priya@email.com', 'customer123', '9876543211', 'CUSTOMER', '45 Park Street, Mumbai');

-- Seed: Sample providers (password: provider123)
INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (4, 'Amit Kumar', 'amit@plumbing.com', 'provider123', '9876543220', 'PROVIDER', 'Sector 15, Noida');

INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (5, 'Sneha Reddy', 'sneha@clean.com', 'provider123', '9876543221', 'PROVIDER', 'Koramangala, Bangalore');

INSERT OR IGNORE INTO users (id, name, email, password, phone, role, address)
VALUES (6, 'Vikram Singh', 'vikram@electric.com', 'provider123', '9876543222', 'PROVIDER', 'Andheri West, Mumbai');

INSERT OR IGNORE INTO service_providers (id, user_id, business_name, description, rating, available)
VALUES (1, 4, 'Amit Plumbing Services', 'Expert plumbing repairs and installations', 4.5, 1);

INSERT OR IGNORE INTO service_providers (id, user_id, business_name, description, rating, available)
VALUES (2, 5, 'Sneha Home Cleaning', 'Professional home and office cleaning', 4.8, 1);

INSERT OR IGNORE INTO service_providers (id, user_id, business_name, description, rating, available)
VALUES (3, 6, 'Vikram Electrical Works', 'Licensed electrician for all electrical needs', 4.2, 1);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (1, 1, 'Pipe Leak Repair', 'Fix leaking pipes and taps', 'Plumbing', 499.00, 60);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (2, 1, 'Drain Cleaning', 'Clear clogged drains and sinks', 'Plumbing', 399.00, 45);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (3, 2, 'Full Home Cleaning', 'Deep cleaning of entire home', 'Cleaning', 1499.00, 180);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (4, 2, 'Kitchen Cleaning', 'Thorough kitchen deep clean', 'Cleaning', 699.00, 90);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (5, 3, 'Wiring Repair', 'Fix electrical wiring issues', 'Electrical', 599.00, 90);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (6, 3, 'Fan Installation', 'Install ceiling or wall fans', 'Electrical', 349.00, 45);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (7, 1, 'Emergency Plumbing', '24/7 emergency plumbing response', 'Plumbing', 999.00, 60);

INSERT OR IGNORE INTO services (id, provider_id, name, description, category, price, duration_minutes)
VALUES (8, 3, 'Emergency Electrical', 'Urgent electrical fault fixing', 'Electrical', 899.00, 60);
