# IT Support Ticket System API
A simple ticket management application for employees to report and track IT issues.\
Built with Spring Boot (Java 17), Oracle SQL, and Java Swing for the UI.\
Supports role-based access, status tracking, audit logging, and search/filtering.

## Overview
The **IT Support Ticket System API** provides a platform for employees to create and track IT support tickets while allowing IT support staff to manage and resolve them. It includes endpoints for **user authentication, ticket management, comments, and audit logs**.

## Setup
### **1. Clone the Repository**
```sh
git clone <repository-url>
cd it-support-ticket-system
```

### **2. Configure Database (H2 In-Memory Database)**
This project uses **H2**, an in-memory database for development and testing.
- No external configuration is needed.
- The database is reset upon each restart.

#### **H2 Console Access**
Visit `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty by default)

### **3. Install Dependencies (Maven)**
```sh
mvn clean install
```

### **4. Run the Application**
```sh
mvn spring-boot:run
```

---

## API Endpoints

### **1. User Management**
#### **Create User**
- **Method:** `POST`
- **URL:** `/users`
- **Description:** Registers a new user (Employee or IT Support).
- **Request Body:**
  ```json
  {
    "username": "tupac",
    "password": "12345RFTE",
    "email": "tupac@shakur.com",
    "role": "EMPLOYEE"
  }
  ```

#### **Authenticate User**
- **Method:** `POST`
- **URL:** `/user/authenticate`
- **Description:** Logs in a user and returns a JWT token.
- **Request Body:**
  ```json
  {
    "username": "tupac",
    "password": "12345RFTE"
  }
  ```

#### **Retrieve User**
- **Method:** `GET`
- **URL:** `/users/{userId}`
- **Authentication:** Bearer token required.

#### **Remove User**
- **Method:** `DELETE`
- **URL:** `/users/{userId}`

---

### **2. Ticket Management**
#### **Create Ticket**
- **Method:** `POST`
- **URL:** `/users/{userId}/tickets`
- **Description:** Allows an employee to create a new support ticket.
- **Request Body:**
  ```json
  {
    "title": "Issue with VPN",
    "description": "Cannot connect to VPN",
    "category": "NETWORK",
    "priority": "HIGH",
    "status": "NEW"
  }
  ```

#### **Retrieve Ticket**
- **Method:** `GET`
- **URL:** `/users/{userId}/tickets/{ticketId}`

#### **Update Ticket**
- **Method:** `PATCH`
- **URL:** `/users/{userId}/tickets/{ticketId}`
- **Description:** Updates ticket details such as status and priority.

#### **Remove Ticket**
- **Method:** `DELETE`
- **URL:** `/users/{userId}/tickets/{ticketId}`

#### **Retrieve All Tickets by Creator**
- **Method:** `GET`
- **URL:** `/users/{userId}/tickets/all`

#### **Search Tickets by Status**
- **Method:** `GET`
- **URL:** `/users/{userId}/tickets/search?status=RESOLVED`

#### **Retrieve Ticket Audit Logs**
- **Method:** `GET`
- **URL:** `/users/{userId}/tickets/{ticketId}/audit-logs`
- **Description:** Retrieves the history of status changes and actions performed on a ticket.

---

### **3. Comment Management**
#### **Create Comment**
- **Method:** `POST`
- **URL:** `/users/{userId}/tickets/{ticketId}/comments`
- **Description:** Adds a comment to a ticket.
- **Request Body:**
  ```json
  {
    "content": "Can you provide more details about the issue?"
  }
  ```

#### **Retrieve Comment**
- **Method:** `GET`
- **URL:** `/users/{userId}/tickets/{ticketId}/comments/{commentId}`

#### **Update Comment**
- **Method:** `PATCH`
- **URL:** `/users/{userId}/tickets/{ticketId}/comments/{commentId}`
- **Request Body:**
  ```json
  {
    "content": "Have you tried restarting the system?"
  }
  ```

#### **Remove Comment**
- **Method:** `DELETE`
- **URL:** `/users/{userId}/tickets/{ticketId}/comments/{commentId}`

---

## Authentication & Security
- The API uses **JWT Bearer Token Authentication** for protected routes.
- **Employees** can only access their own tickets.
- **IT Support** has permissions to view and modify all tickets.
