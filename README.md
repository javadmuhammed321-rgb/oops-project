# Local Service Booking Platform

College-level OOP microproject: book local services (plumbing, cleaning, electrical), manage providers, payments (Cash / UPI / Card), emergency booking, and a simple admin panel.

**Stack:** Plain Java + JDBC + SQLite · Embedded HTTP server (`com.sun.net.httpserver`) · HTML/CSS/JS frontend

---

## Project structure

```
oops-project/
├── src/com/localservice/
│   ├── Main.java                 # Entry point
│   ├── model/                    # OOP entities (Person, User, ServiceProvider, …)
│   ├── dao/                      # JDBC data access
│   ├── service/                  # Business logic
│   ├── util/                     # DatabaseUtil, JsonUtil
│   └── server/                   # ApiHandler, StaticFileHandler
├── web/                          # Static frontend (Vercel-deployable)
├── data/schema.sql               # Schema + seed data
├── lib/sqlite-jdbc-*.jar         # SQLite JDBC driver
├── vercel.json
└── README.md
```

---

## Prerequisites

- **JDK 8+** (`javac` and `java` on PATH)
- SQLite JDBC jar in `lib/` (already included: `lib/sqlite-jdbc-3.45.1.0.jar`)

Required jars in `lib/` (already included in this project):

- `sqlite-jdbc-3.45.1.0.jar`
- `slf4j-api-2.0.9.jar`
- `slf4j-nop-2.0.9.jar` (no-op logger; sqlite-jdbc needs SLF4J on the classpath)

If jars are missing, download them:

```bash
mkdir -p lib
curl -L -o lib/sqlite-jdbc-3.45.1.0.jar \
  "https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar"
curl -L -o lib/slf4j-api-2.0.9.jar \
  "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar"
curl -L -o lib/slf4j-nop-2.0.9.jar \
  "https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/2.0.9/slf4j-nop-2.0.9.jar"
```

---

## 1) Run Java locally (full demo with DB)

From the project root (`oops-project/`):

```bash
# Compile
mkdir -p out
javac -cp "lib/*" -d out $(find src -name "*.java")

# Run (creates data/localservice.db from schema.sql on first start)
java -cp "out:lib/*" com.localservice.Main
```

On Windows (PowerShell / CMD), use `;` instead of `:` in the classpath:

```bat
javac -cp "lib\*" -d out src\com\localservice\*.java src\com\localservice\model\*.java src\com\localservice\dao\*.java src\com\localservice\service\*.java src\com\localservice\util\*.java src\com\localservice\server\*.java
java -cp "out;lib\*" com.localservice.Main
```

Open: **http://localhost:8080**

The server serves:
- Static UI from `web/`
- JSON API under `/api/*`
- SQLite DB file at `data/localservice.db`

---

## 2) Deploy `web/` to Vercel (UI hosting only)

```bash
# From project root — Vercel serves the static frontend
npx vercel
```

Or connect the repo in the Vercel dashboard. `vercel.json` maps routes to the `web/` folder.

### Important limitation

**Vercel cannot run the Java HTTP server or SQLite.**  
The Vercel deployment is for showcasing the UI. Login, booking, payments, and admin need the local Java server at `http://localhost:8080`.

When the UI is opened from Vercel, API calls target `http://localhost:8080` (see `web/js/api.js`). For a full end-to-end demo, always run Java locally.

---

## Default login credentials

| Role     | Email                     | Password      |
|----------|---------------------------|---------------|
| Admin    | `admin@localservice.com`  | `admin123`    |
| Customer | `rahul@email.com`         | `customer123` |
| Customer | `priya@email.com`         | `customer123` |
| Provider | `amit@plumbing.com`       | `provider123` |
| Provider | `sneha@clean.com`         | `provider123` |
| Provider | `vikram@electric.com`     | `provider123` |

---

## Features

1. Customer & provider registration / login  
2. View available services and providers  
3. Customers book and cancel services  
4. Booking history  
5. Emergency booking (auto-confirmed)  
6. Payments: Cash, UPI, Card (demo processors)  
7. Admin: manage users, providers, bookings, services, payments  
8. SQLite tables: users, service_providers, services, bookings, payments  

---

## OOP concepts → classes

| Concept | Where demonstrated |
|---------|-------------------|
| **Class & Object** | All model classes (`User`, `Service`, `Booking`, …); objects created in services/DAOs |
| **Encapsulation** | Private fields + getters/setters in every model |
| **Abstraction** | Abstract `Person` (`getRole()`); abstract `PaymentProcessor` (`process()`, `getMethodName()`) |
| **Inheritance** | `User extends Person`; `ServiceProvider extends Person`; `Cash/Upi/CardPaymentProcessor extends PaymentProcessor` |
| **Polymorphism** | `Person` references calling overridden `displayInfo()`; `PaymentProcessor` factory selecting Cash/UPI/Card at runtime |
| **Method Overriding** | `displayInfo()`, `getRole()` in `User` and `ServiceProvider`; `process()` in payment processors |
| **Method Overloading** | `Person.greet()`, `greet(title)`, `greet(title, formal)`; `Service.formatPrice()` / `formatPrice(includeDuration)` |

Quick API check of OOP demo: `GET http://localhost:8080/api/demo/oop`

---

## API overview (local Java server)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/login` | Login |
| POST | `/api/register/customer` | Customer signup |
| POST | `/api/register/provider` | Provider signup |
| GET | `/api/services` | List services (`?category=`) |
| GET | `/api/services/emergency` | Emergency services |
| GET | `/api/providers` | Available providers |
| POST | `/api/bookings` | Create booking |
| POST | `/api/bookings/cancel` | Cancel booking |
| GET | `/api/bookings/customer/{id}` | Customer history |
| GET | `/api/bookings/provider/{id}` | Provider bookings |
| POST | `/api/payment` | Pay (CASH/UPI/CARD) |
| GET | `/api/admin/*` | Admin lists & actions |

---

## Resetting the database

```bash
rm -f data/localservice.db
java -cp "out:lib/*" com.localservice.Main
```

Schema and seed data are reapplied from `data/schema.sql`.

---

## Academic notes

- No Spring / Hibernate / microservices — intentional for a beginner OOP course.  
- Passwords stored in plain text for demo simplicity (not production-safe).  
- Payment processors simulate success and generate transaction references.

---

## License

Educational / college microproject use.
