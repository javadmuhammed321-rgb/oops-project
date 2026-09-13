package com.localservice.util;

import com.localservice.model.*;
import java.util.List;

/**
 * Minimal JSON helpers — no external libraries (college-friendly).
 */
public class JsonUtil {

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String quote(String s) {
        return "\"" + escape(s) + "\"";
    }

    public static String userToJson(User u) {
        if (u == null) return "null";
        return "{"
                + "\"id\":" + u.getId() + ","
                + "\"name\":" + quote(u.getName()) + ","
                + "\"email\":" + quote(u.getEmail()) + ","
                + "\"phone\":" + quote(u.getPhone()) + ","
                + "\"role\":" + quote(u.getRole()) + ","
                + "\"address\":" + quote(u.getAddress())
                + "}";
    }

    public static String providerToJson(ServiceProvider p) {
        if (p == null) return "null";
        return "{"
                + "\"id\":" + p.getId() + ","
                + "\"providerId\":" + p.getProviderId() + ","
                + "\"name\":" + quote(p.getName()) + ","
                + "\"email\":" + quote(p.getEmail()) + ","
                + "\"phone\":" + quote(p.getPhone()) + ","
                + "\"role\":" + quote(p.getRole()) + ","
                + "\"businessName\":" + quote(p.getBusinessName()) + ","
                + "\"description\":" + quote(p.getDescription()) + ","
                + "\"rating\":" + p.getRating() + ","
                + "\"available\":" + p.isAvailable()
                + "}";
    }

    public static String serviceToJson(Service s) {
        if (s == null) return "null";
        return "{"
                + "\"id\":" + s.getId() + ","
                + "\"providerId\":" + s.getProviderId() + ","
                + "\"name\":" + quote(s.getName()) + ","
                + "\"description\":" + quote(s.getDescription()) + ","
                + "\"category\":" + quote(s.getCategory()) + ","
                + "\"price\":" + s.getPrice() + ","
                + "\"durationMinutes\":" + s.getDurationMinutes() + ","
                + "\"active\":" + s.isActive() + ","
                + "\"providerName\":" + quote(s.getProviderName() != null ? s.getProviderName() : "")
                + "}";
    }

    public static String bookingToJson(Booking b) {
        if (b == null) return "null";
        return "{"
                + "\"id\":" + b.getId() + ","
                + "\"customerId\":" + b.getCustomerId() + ","
                + "\"serviceId\":" + b.getServiceId() + ","
                + "\"providerId\":" + b.getProviderId() + ","
                + "\"bookingDate\":" + quote(b.getBookingDate()) + ","
                + "\"bookingTime\":" + quote(b.getBookingTime()) + ","
                + "\"status\":" + quote(b.getStatus()) + ","
                + "\"emergency\":" + b.isEmergency() + ","
                + "\"notes\":" + quote(b.getNotes() != null ? b.getNotes() : "") + ","
                + "\"createdAt\":" + quote(b.getCreatedAt() != null ? b.getCreatedAt() : "") + ","
                + "\"customerName\":" + quote(b.getCustomerName() != null ? b.getCustomerName() : "") + ","
                + "\"serviceName\":" + quote(b.getServiceName() != null ? b.getServiceName() : "") + ","
                + "\"providerName\":" + quote(b.getProviderName() != null ? b.getProviderName() : "") + ","
                + "\"servicePrice\":" + b.getServicePrice()
                + "}";
    }

    public static String paymentToJson(Payment p) {
        if (p == null) return "null";
        return "{"
                + "\"id\":" + p.getId() + ","
                + "\"bookingId\":" + p.getBookingId() + ","
                + "\"amount\":" + p.getAmount() + ","
                + "\"method\":" + quote(p.getMethod()) + ","
                + "\"status\":" + quote(p.getStatus()) + ","
                + "\"transactionRef\":" + quote(p.getTransactionRef() != null ? p.getTransactionRef() : "") + ","
                + "\"paidAt\":" + quote(p.getPaidAt() != null ? p.getPaidAt() : "")
                + "}";
    }

    public static String usersToJson(List<User> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(userToJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    public static String providersToJson(List<ServiceProvider> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(providerToJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    public static String servicesToJson(List<Service> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(serviceToJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    public static String bookingsToJson(List<Booking> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(bookingToJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    public static String paymentsToJson(List<Payment> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(paymentToJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    public static String ok(String message) {
        return "{\"success\":true,\"message\":" + quote(message) + "}";
    }

    public static String ok(String message, String dataKey, String dataJson) {
        return "{\"success\":true,\"message\":" + quote(message) + ",\"" + dataKey + "\":" + dataJson + "}";
    }

    public static String error(String message) {
        return "{\"success\":false,\"message\":" + quote(message) + "}";
    }

    /** Very simple JSON field extractor for request bodies. */
    public static String extractString(String json, String key) {
        if (json == null) return null;
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start >= json.length()) return null;
        if (json.charAt(start) == '"') {
            start++;
            StringBuilder sb = new StringBuilder();
            while (start < json.length()) {
                char c = json.charAt(start);
                if (c == '\\' && start + 1 < json.length()) {
                    sb.append(json.charAt(start + 1));
                    start += 2;
                    continue;
                }
                if (c == '"') break;
                sb.append(c);
                start++;
            }
            return sb.toString();
        }
        // number or boolean or null
        int end = start;
        while (end < json.length() && ",}] \t\n\r".indexOf(json.charAt(end)) < 0) end++;
        String val = json.substring(start, end).trim();
        if ("null".equals(val)) return null;
        return val;
    }

    public static int extractInt(String json, String key, int def) {
        String v = extractString(json, key);
        if (v == null || v.isEmpty()) return def;
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return def; }
    }

    public static double extractDouble(String json, String key, double def) {
        String v = extractString(json, key);
        if (v == null || v.isEmpty()) return def;
        try { return Double.parseDouble(v); } catch (NumberFormatException e) { return def; }
    }

    public static boolean extractBool(String json, String key, boolean def) {
        String v = extractString(json, key);
        if (v == null) return def;
        return "true".equalsIgnoreCase(v) || "1".equals(v);
    }
}
