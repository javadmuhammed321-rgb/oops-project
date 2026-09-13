package com.localservice;

import com.localservice.server.ApiHandler;
import com.localservice.server.StaticFileHandler;
import com.localservice.util.DatabaseUtil;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

/**
 * Entry point — starts SQLite init + embedded HTTP server.
 * Serves API at /api/* and static UI from web/.
 */
public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Local Service Booking Platform ===");
        DatabaseUtil.initialize();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api", new ApiHandler());
        server.createContext("/", new StaticFileHandler("web"));
        server.setExecutor(null);
        server.start();

        System.out.println("Server running at http://localhost:" + PORT);
        System.out.println("Open the URL in your browser to use the app.");
        System.out.println("Default admin: admin@localservice.com / admin123");
        System.out.println("Sample customer: rahul@email.com / customer123");
        System.out.println("Sample provider: amit@plumbing.com / provider123");
        System.out.println("Press Ctrl+C to stop.");
    }
}
