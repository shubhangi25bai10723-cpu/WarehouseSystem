# WareFlow – Warehouse Management System (WMS)

## Overview

WareFlow is a full-stack Warehouse Management System built as a college/enterprise-style project. It provides end-to-end tracking of products, suppliers, warehouses, and stock movement — from goods receiving (inward) to goods dispatch (outward) — along with purchase order management, low-stock/capacity alerts, and reporting.



## Features

- **Dashboard & Analytics** — inventory valuation, stock on hand, warehouse utilization %, active supplier count, monthly inward vs. outward trends
- **Product Management** — CRUD with unique SKU enforcement, unit pricing, reorder thresholds, warehouse bin assignment, and supplier linkage
- **Warehouse Management** — multiple warehouse/facility locations with real-time capacity tracking (occupied vs. available space)
- **Stock Inward (Goods Receiving)** — records incoming shipments, increases product stock and warehouse occupancy, rejects shipments that exceed available space
- **Stock Outward (Goods Dispatch)** — records outgoing shipments, decreases product stock, rejects dispatches that exceed available stock
- **Purchase Orders** — multi-state workflow (`PENDING → APPROVED → RECEIVED → CANCELLED`) with one-click receipt into inventory
- **Alerts** — automatic out-of-stock, low-stock, and warehouse capacity (>85% utilization) notifications
- **Reports & Exports** — inventory valuation, warehouse utilization, and stock movement history, with CSV export
- **In-app Source Code Viewer** — browse backend Controller/Service/Repository/Entity/DTO source directly in the UI

## Technologies / Tools Used

**Backend**
- Java 17
- Spring Boot 3.2.3 (Web, Data JPA, Validation, Security)
- Hibernate ORM
- MySQL 8.0 (InnoDB)
- Apache Maven

**Frontend**
- React 19 + TypeScript
- Vite 6
- Tailwind CSS
- Lucide React (icons)

## Steps to Install & Run the Project

### Prerequisites
- JDK 17 or higher (`java -version`)
- Apache Maven 3.8+ (`mvn -version`)
- MySQL 8.0 (or MariaDB) running on port 3306
- Node.js 18+ and npm (`node -v`)

### 1. Clone the repository
```bash
git clone <your-repo-url>
cd wareflow
```

### 2. Set up the database
```sql
CREATE DATABASE warehouse_management;
```
Schema and seed data are applied automatically by Spring Boot on startup.

### 3. Configure database credentials
Edit `backend/src/main/resources/application.properties` if your MySQL username/password differ from the defaults (`root` / `root`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/warehouse_management?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

### 4. Run the backend (Spring Boot API)
```bash
cd backend
mvn clean spring-boot:run
```
The API starts on `http://localhost:8080`.

### 5. Run the frontend (React)
In a new terminal, from the project root:
```bash
npm install
npm run dev
```
Open `http://localhost:3000` in your browser.

## Instructions for Testing

This project does not currently ship an automated test suite; verify functionality manually as follows:

1. **Backend health check** — with the backend running, confirm the API responds:
   ```bash
   curl http://localhost:8080/api/dashboard/summary
   ```
2. **Run the backend's own test task** (executes any tests present and validates the build):
   ```bash
   cd backend
   mvn test
   ```
3. **Manual end-to-end walkthrough** (with both backend and frontend running):
   - Add a supplier under **Suppliers**.
   - Add a warehouse under **Warehouse Management** and note its capacity.
   - Add a product under **Products**, linking it to the supplier.
   - Use **Stock Inward** to receive units into the warehouse — verify the product quantity and warehouse occupancy increase, and that a shipment exceeding available space is rejected.
   - Use **Stock Outward** to dispatch units — verify stock decreases, and that dispatching more than available stock is rejected.
   - Create a **Purchase Order**, transition it through its statuses, and confirm receiving it logs a matching inward movement.
   - Lower a product's stock below its reorder threshold and confirm it appears under **Alerts**.
   - Check the **Dashboard** and **Reports** views reflect the updated totals, and confirm CSV export works.
4. **API testing (optional)** — use Postman, Insomnia, or `curl` against the endpoints listed in the backend README to exercise create/update/delete flows directly.
