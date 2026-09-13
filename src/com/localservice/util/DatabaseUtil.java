package com.localservice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Database utility — manages SQLite connection and schema init.
 */
public class DatabaseUtil {
    private static final String DB_DIR = "data";
    private static final String DB_FILE = "localservice.db";
    private static final String SCHEMA_FILE = "data/schema.sql";
    private static String jdbcUrl;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            Path dir = Paths.get(DB_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            jdbcUrl = "jdbc:sqlite:" + DB_DIR + File.separator + DB_FILE;
        } catch (Exception e) {
            throw new RuntimeException("Failed to init DB util: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl);
    }

    /** Initialize schema and seed data if DB is new or empty. */
    public static void initialize() {
        File db = new File(DB_DIR, DB_FILE);
        boolean exists = db.exists() && db.length() > 0;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            if (!exists) {
                System.out.println("Initializing database from schema.sql ...");
                runSchema(stmt);
                System.out.println("Database initialized with seed data.");
            } else {
                // Ensure tables exist even if file was empty/corrupt
                runSchema(stmt);
                System.out.println("Database ready: " + db.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("DB init failed: " + e.getMessage(), e);
        }
    }

    private static void runSchema(Statement stmt) throws Exception {
        File schema = new File(SCHEMA_FILE);
        if (!schema.exists()) {
            throw new RuntimeException("schema.sql not found at " + schema.getAbsolutePath());
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(schema))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue;
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sb.toString().trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                    sb.setLength(0);
                }
            }
            if (sb.length() > 0) {
                String sql = sb.toString().trim();
                if (!sql.isEmpty()) stmt.execute(sql);
            }
        }
    }
}
