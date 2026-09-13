package com.localservice.server;

import com.localservice.model.*;
import com.localservice.service.*;
import com.localservice.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Routes /api/* JSON requests to service layer.
 */
public class ApiHandler implements HttpHandler {
    private final AuthService authService = new AuthService();
    private final BookingService bookingService = new BookingService();
    private final CatalogService catalogService = new CatalogService();
    private final AdminService adminService = new AdminService();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        addCors(ex);
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }

        String path = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();
        String body = readBody(ex);

        try {
            String response;
            int code = 200;

            if (path.equals("/api/login") && "POST".equals(method)) {
                response = handleLogin(body);
            } else if (path.equals("/api/register/customer") && "POST".equals(method)) {
                response = handleRegisterCustomer(body);
            } else if (path.equals("/api/register/provider") && "POST".equals(method)) {
                response = handleRegisterProvider(body);
            } else if (path.equals("/api/services") && "GET".equals(method)) {
                String cat = queryParam(ex, "category");
                List<Service> list = (cat != null && !cat.isEmpty())
                        ? catalogService.byCategory(cat)
                        : catalogService.availableServices();
                response = JsonUtil.ok("Services", "services", JsonUtil.servicesToJson(list));
            } else if (path.equals("/api/services/emergency") && "GET".equals(method)) {
                List<Service> list = catalogService.emergencyServices();
                response = JsonUtil.ok("Emergency services", "services", JsonUtil.servicesToJson(list));
            } else if (path.equals("/api/providers") && "GET".equals(method)) {
                List<ServiceProvider> list = catalogService.availableProviders();
                response = JsonUtil.ok("Providers", "providers", JsonUtil.providersToJson(list));
            } else if (path.equals("/api/bookings") && "POST".equals(method)) {
                response = handleCreateBooking(body);
            } else if (path.equals("/api/bookings/cancel") && "POST".equals(method)) {
                response = handleCancelBooking(body);
            } else if (path.startsWith("/api/bookings/customer/") && "GET".equals(method)) {
                int cid = Integer.parseInt(path.substring("/api/bookings/customer/".length()));
                List<Booking> list = bookingService.historyForCustomer(cid);
                response = JsonUtil.ok("History", "bookings", JsonUtil.bookingsToJson(list));
            } else if (path.startsWith("/api/bookings/provider/") && "GET".equals(method)) {
                int pid = Integer.parseInt(path.substring("/api/bookings/provider/".length()));
                List<Booking> list = bookingService.forProvider(pid);
                response = JsonUtil.ok("Provider bookings", "bookings", JsonUtil.bookingsToJson(list));
            } else if (path.equals("/api/payment") && "POST".equals(method)) {
                response = handlePayment(body);
            } else if (path.startsWith("/api/payment/") && "GET".equals(method)) {
                int bid = Integer.parseInt(path.substring("/api/payment/".length()));
                Payment p = bookingService.getPayment(bid);
                response = JsonUtil.ok("Payment", "payment", JsonUtil.paymentToJson(p));
            } else if (path.equals("/api/admin/users") && "GET".equals(method)) {
                response = JsonUtil.ok("Users", "users", JsonUtil.usersToJson(adminService.allUsers()));
            } else if (path.equals("/api/admin/providers") && "GET".equals(method)) {
                response = JsonUtil.ok("Providers", "providers", JsonUtil.providersToJson(adminService.allProviders()));
            } else if (path.equals("/api/admin/bookings") && "GET".equals(method)) {
                response = JsonUtil.ok("Bookings", "bookings", JsonUtil.bookingsToJson(adminService.allBookings()));
            } else if (path.equals("/api/admin/services") && "GET".equals(method)) {
                response = JsonUtil.ok("Services", "services", JsonUtil.servicesToJson(adminService.allServices()));
            } else if (path.equals("/api/admin/payments") && "GET".equals(method)) {
                response = JsonUtil.ok("Payments", "payments", JsonUtil.paymentsToJson(bookingService.allPayments()));
            } else if (path.equals("/api/admin/user/delete") && "POST".equals(method)) {
                int id = JsonUtil.extractInt(body, "id", 0);
                boolean ok = adminService.deleteUser(id);
                response = ok ? JsonUtil.ok("User deleted") : JsonUtil.error("Delete failed");
            } else if (path.equals("/api/admin/booking/status") && "POST".equals(method)) {
                int id = JsonUtil.extractInt(body, "id", 0);
                String status = JsonUtil.extractString(body, "status");
                boolean ok = adminService.updateBookingStatus(id, status);
                response = ok ? JsonUtil.ok("Status updated") : JsonUtil.error("Update failed");
            } else if (path.equals("/api/admin/provider/availability") && "POST".equals(method)) {
                int id = JsonUtil.extractInt(body, "providerId", 0);
                boolean avail = JsonUtil.extractBool(body, "available", true);
                boolean ok = adminService.setProviderAvailable(id, avail);
                response = ok ? JsonUtil.ok("Availability updated") : JsonUtil.error("Update failed");
            } else if (path.equals("/api/admin/service/add") && "POST".equals(method)) {
                int providerId = JsonUtil.extractInt(body, "providerId", 0);
                String name = JsonUtil.extractString(body, "name");
                String desc = JsonUtil.extractString(body, "description");
                String cat = JsonUtil.extractString(body, "category");
                double price = JsonUtil.extractDouble(body, "price", 0);
                int dur = JsonUtil.extractInt(body, "durationMinutes", 60);
                int sid = adminService.addService(providerId, name, desc, cat, price, dur);
                response = JsonUtil.ok("Service added", "serviceId", String.valueOf(sid));
            } else if (path.equals("/api/admin/service/delete") && "POST".equals(method)) {
                int id = JsonUtil.extractInt(body, "id", 0);
                boolean ok = adminService.deleteService(id);
                response = ok ? JsonUtil.ok("Service deleted") : JsonUtil.error("Delete failed");
            } else if (path.equals("/api/provider/service/add") && "POST".equals(method)) {
                int providerId = JsonUtil.extractInt(body, "providerId", 0);
                String name = JsonUtil.extractString(body, "name");
                String desc = JsonUtil.extractString(body, "description");
                String cat = JsonUtil.extractString(body, "category");
                double price = JsonUtil.extractDouble(body, "price", 0);
                int dur = JsonUtil.extractInt(body, "durationMinutes", 60);
                int sid = catalogService.addServiceForProvider(providerId, name, desc, cat, price, dur);
                response = JsonUtil.ok("Service added", "serviceId", String.valueOf(sid));
            } else if (path.startsWith("/api/provider/services/") && "GET".equals(method)) {
                int pid = Integer.parseInt(path.substring("/api/provider/services/".length()));
                List<Service> list = catalogService.byProvider(pid);
                response = JsonUtil.ok("Services", "services", JsonUtil.servicesToJson(list));
            } else if (path.equals("/api/demo/oop") && "GET".equals(method)) {
                response = demoOop();
            } else {
                code = 404;
                response = JsonUtil.error("Not found: " + path);
            }

            sendJson(ex, code, response);
        } catch (Exception e) {
            sendJson(ex, 400, JsonUtil.error(e.getMessage() != null ? e.getMessage() : "Error"));
        }
    }

    private String handleLogin(String body) throws Exception {
        String email = JsonUtil.extractString(body, "email");
        String password = JsonUtil.extractString(body, "password");
        Person person = authService.login(email, password);
        if (person == null) return JsonUtil.error("Invalid email or password");

        // Polymorphism: displayInfo() depends on runtime type
        String info = person.displayInfo();
        if (person instanceof ServiceProvider) {
            ServiceProvider sp = (ServiceProvider) person;
            return JsonUtil.ok("Login successful", "user", JsonUtil.providerToJson(sp))
                    .replaceFirst("\\}$", ",\"displayInfo\":" + JsonUtil.quote(info) + "}");
        } else {
            User u = (User) person;
            return JsonUtil.ok("Login successful", "user", JsonUtil.userToJson(u))
                    .replaceFirst("\\}$", ",\"displayInfo\":" + JsonUtil.quote(info) + "}");
        }
    }

    private String handleRegisterCustomer(String body) throws Exception {
        User u = authService.registerCustomer(
                JsonUtil.extractString(body, "name"),
                JsonUtil.extractString(body, "email"),
                JsonUtil.extractString(body, "password"),
                JsonUtil.extractString(body, "phone"),
                JsonUtil.extractString(body, "address")
        );
        return JsonUtil.ok("Customer registered", "user", JsonUtil.userToJson(u));
    }

    private String handleRegisterProvider(String body) throws Exception {
        ServiceProvider sp = authService.registerProvider(
                JsonUtil.extractString(body, "name"),
                JsonUtil.extractString(body, "email"),
                JsonUtil.extractString(body, "password"),
                JsonUtil.extractString(body, "phone"),
                JsonUtil.extractString(body, "businessName"),
                JsonUtil.extractString(body, "description")
        );
        return JsonUtil.ok("Provider registered", "user", JsonUtil.providerToJson(sp));
    }

    private String handleCreateBooking(String body) throws Exception {
        int customerId = JsonUtil.extractInt(body, "customerId", 0);
        int serviceId = JsonUtil.extractInt(body, "serviceId", 0);
        String date = JsonUtil.extractString(body, "bookingDate");
        String time = JsonUtil.extractString(body, "bookingTime");
        boolean emergency = JsonUtil.extractBool(body, "emergency", false);
        String notes = JsonUtil.extractString(body, "notes");
        Booking b = bookingService.createBooking(customerId, serviceId, date, time, emergency, notes);
        return JsonUtil.ok("Booking created", "booking", JsonUtil.bookingToJson(b));
    }

    private String handleCancelBooking(String body) throws Exception {
        int bookingId = JsonUtil.extractInt(body, "bookingId", 0);
        int customerId = JsonUtil.extractInt(body, "customerId", 0);
        boolean ok = bookingService.cancelBooking(bookingId, customerId);
        return ok ? JsonUtil.ok("Booking cancelled") : JsonUtil.error("Cancel failed");
    }

    private String handlePayment(String body) throws Exception {
        int bookingId = JsonUtil.extractInt(body, "bookingId", 0);
        String method = JsonUtil.extractString(body, "method");
        Payment p = bookingService.processPayment(bookingId, method);
        return JsonUtil.ok("Payment successful", "payment", JsonUtil.paymentToJson(p));
    }

    /** Demonstrates OOP concepts via API for viva/demo. */
    private String demoOop() {
        Person customer = new User(0, "Demo Customer", "demo@test.com", "x", "111", "CUSTOMER", "City");
        Person provider = new ServiceProvider(0, "Demo Prov", "p@test.com", "x", "222",
                1, "Demo Biz", "Desc", 4.5, true);

        // Overloading
        String g1 = customer.greet();
        String g2 = customer.greet("Mr.");
        String g3 = customer.greet("Mr.", true);

        // Polymorphism / overriding
        String d1 = customer.displayInfo();
        String d2 = provider.displayInfo();

        // Abstraction + polymorphism on payment
        PaymentProcessor cash = new CashPaymentProcessor(100);
        PaymentProcessor upi = new UpiPaymentProcessor(100);
        PaymentProcessor card = new CardPaymentProcessor(100);

        StringBuilder sb = new StringBuilder();
        sb.append("{\"success\":true,\"oop\":{");
        sb.append("\"abstraction\":\"Person and PaymentProcessor are abstract classes\",");
        sb.append("\"inheritance\":\"User and ServiceProvider extend Person\",");
        sb.append("\"encapsulation\":\"Private fields with getters/setters in all models\",");
        sb.append("\"polymorphism\":{");
        sb.append("\"customerDisplay\":").append(JsonUtil.quote(d1)).append(",");
        sb.append("\"providerDisplay\":").append(JsonUtil.quote(d2)).append(",");
        sb.append("\"paymentMethods\":[")
                .append(JsonUtil.quote(cash.getMethodName() + "->" + cash.process())).append(",")
                .append(JsonUtil.quote(upi.getMethodName() + "->" + upi.process())).append(",")
                .append(JsonUtil.quote(card.getMethodName() + "->" + card.process()))
                .append("]},");
        sb.append("\"overloading\":{");
        sb.append("\"greet\":").append(JsonUtil.quote(g1)).append(",");
        sb.append("\"greetTitle\":").append(JsonUtil.quote(g2)).append(",");
        sb.append("\"greetFormal\":").append(JsonUtil.quote(g3));
        sb.append("},");
        sb.append("\"overriding\":\"displayInfo() and getRole() overridden in User and ServiceProvider\"");
        sb.append("}}");
        return sb.toString();
    }

    private void addCors(HttpExchange ex) {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
            return bos.toString(StandardCharsets.UTF_8.name());
        }
    }

    private String queryParam(HttpExchange ex, String key) {
        String q = ex.getRequestURI().getRawQuery();
        if (q == null) return null;
        for (String part : q.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                try {
                    return java.net.URLDecoder.decode(kv[1], "UTF-8");
                } catch (Exception e) {
                    return kv[1];
                }
            }
        }
        return null;
    }
}
