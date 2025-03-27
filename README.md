# IT Support Ticket System API
A simple ticket management application for employees to report and track IT issues.\
The project focuses on **Spring Security**, particularly **Role-Based Access Control (RBAC)**, ensuring secure API access based on user roles.\
Built with Spring Boot (Java 17), Oracle SQL (not yet implemented), and Java Swing (not yet implemented) for the UI.\
Supports role-based access, status tracking, audit logging, and search/filtering.

## Overview
The **IT Support Ticket System API** provides a platform for employees to create and track IT support tickets while allowing IT support staff to manage and resolve them. It includes endpoints for **user authentication, ticket management, comments, and audit logs**.

## Setup
### **1. Clone the Repository**
```sh
git clone git@github.com:alfahami/it-support-ticket-system.git
cd it-support-ticket-system
```

### **2. Configure Database (H2 In-Memory Database)**
This project uses **H2**, an in-memory database for development and testing.

#### **H2 Console Access**
Visit `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:ticketing`
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

## Features
- **User Management** (Create, Authenticate, Retrieve, Remove)
- **Ticket Management** (Create, Retrieve, Update, Delete, Audit Log)
- **Comment Management** (Create, Retrieve, Update, Delete)
- **Audit Logging** for Tickets and Comments

## Roles & Access Control
The system enforces access based on the following roles:
- **EMPLOYEE**: Can create and manage their own tickets and comments.
- **IT_SUPPORT**: Can manage all tickets and audit logs.

## Authentication
- Uses **JWT Bearer Tokens** for authentication.
- To access secured endpoints, first authenticate and include the token in the **Authorization Header**.

## How to Use
1. **Import Collection** into Postman.
2. **Create a User** using the `Create User` request.
3. **Authenticate User** to obtain a JWT token.
4. **Use JWT Token** for secured requests by adding it to the `Authorization` header.
5. **Test API Endpoints** based on the role permissions.

## API Endpoints
### User
- `POST /users` - Create a new user.
- `POST /user/authenticate` - Authenticate and obtain a JWT token.
- `GET /users/{id}` - Retrieve a user (requires authentication).
- `DELETE /users/{id}` - Remove a user (requires authentication).

### Ticket
- `POST /users/{id}/tickets` - Create a support ticket.
- `GET /users/{id}/tickets/{ticketId}` - Retrieve a ticket.
- `PATCH /users/{id}/tickets/{ticketId}/info` - Update ticket information by employee only.
- `PATCH /users/{id}/tickets/{ticketId}/status` - Update ticket status by it support only
- `DELETE /users/{id}/tickets/{ticketId}` - Remove a ticket.
- `GET /users/{id}/tickets/{ticketId}/audit-logs` - View audit logs for a ticket.

### Comments
- `POST /users/{id}/tickets/{ticketId}/comments` - Add a comment.
- `GET /users/{id}/tickets/{ticketId}/comments/{commentId}` - Retrieve a comment.
- `PATCH /users/{id}/tickets/{ticketId}/comments/{commentId}` - Update a comment.
- `DELETE /users/{id}/tickets/{ticketId}/comments/{commentId}` - Remove a comment.
- `GET /users/{id}/tickets/{ticketId}/comments/{commentId}/audit-logs` - View audit logs for a comment.

## Requirements
- **Postman** installed
- **Spring Boot Backend** running at `http://localhost:8080`
- **Valid JWT Token** for secured endpoints

## Notes
- Ensure the **Spring Boot application** is running before testing.
- Role-based access restrictions apply to various endpoints.
- Modify `Authorization` headers to test different roles.

---

## Full API documentation with examples
<details>
  <summary>Click to expand for detailed API documentation</summary>

### Authentication

#### User Registration
- **Endpoint:** `POST /users`
- **Description:** Create a new user account
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string",
    "role": "EMPLOYEE" // Possible roles: EMPLOYEE, IT_SUPPORT.
  }
  ```

#### User Authentication
- **Endpoint:** `POST /user/authenticate`
- **Description:** Authenticate a user and receive a JWT token
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response:** Returns a Bearer token for subsequent authenticated requests

### User Management

#### Retrieve User
- **Endpoint:** `GET /users/{userId}`
- **Description:** Retrieve user details by ID
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the user to retrieve

#### Remove User
- **Endpoint:** `DELETE /users/{userId}`
- **Description:** Remove a user account
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the user to remove

#### Retrieve User's Tickets
- **Endpoint:** `GET /users/{userId}/tickets/all`
- **Description:** Retrieve all tickets created by a specific user
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the user whose tickets are to be retrieved

### Ticket Management

#### Create Ticket
- **Endpoint:** `POST /users/{userId}/tickets`
- **Description:** Create a new support ticket
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
- **Request Body:**
  ```json
  {
    "title": "string",
    "description": "string",
    "category": "NETWORK", // Possible categories: NETWORK, SOFTWARE, HARDWARE, etc.
    "priority": "LOW", // Possible priorities: LOW, MEDIUM, HIGH
    "status": "NEW" // Possible statuses: NEW, IN_PROGRESS, RESOLVED, CLOSED
  }
  ```

#### Retrieve Ticket
- **Endpoint:** `GET /users/{userId}/tickets/{ticketId}`
- **Description:** Retrieve a specific ticket
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to retrieve

#### Update Ticket Information
- **Endpoint:** `PATCH /users/{userId}/tickets/{ticketId}/info`
- **Description:** Update ticket details
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to update
- **Request Body:** Same as ticket creation, with fields to update

#### Update Ticket Status
- **Endpoint:** `PATCH /users/{userId}/tickets/{ticketId}/status`
- **Description:** Update ticket status
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to update
- **Request Body:**
  ```json
  {
    "status": "RESOLVED" // New status
  }
  ```

#### Remove Ticket
- **Endpoint:** `DELETE /users/{userId}/tickets/{ticketId}`
- **Description:** Delete a specific ticket
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to remove

#### Search Ticket by ID and Status
- **Endpoint:** `GET /users/{userId}/tickets/{ticketId}/search`
- **Description:** Search for a ticket with specific status
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to search
- **Query Parameters:**
  - `status`: Status to filter the ticket (e.g., "NEW")

#### Ticket Audit Logs
- **Endpoint:** `GET /users/{userId}/tickets/{ticketId}/audit-logs`
- **Description:** Retrieve audit logs for a specific ticket
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the ticket creator
  - `ticketId`: ID of the ticket to retrieve logs for

### Comment Management

#### Create Comment
- **Endpoint:** `POST /users/{userId}/tickets/{ticketId}/comments`
- **Description:** Add a comment to a ticket
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the comment creator
  - `ticketId`: ID of the ticket to comment on
- **Request Body:**
  ```json
  {
    "content": "string" // Comment text
  }
  ```

#### Update Comment
- **Endpoint:** `PATCH /users/{userId}/tickets/{ticketId}/comments/{commentId}`
- **Description:** Update an existing comment
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the comment creator
  - `ticketId`: ID of the ticket containing the comment
  - `commentId`: ID of the comment to update
- **Request Body:**
  ```json
  {
    "content": "string" // New comment text
  }
  ```

#### Retrieve Comment
- **Endpoint:** `GET /users/{userId}/tickets/{ticketId}/comments/{commentId}`
- **Description:** Retrieve a specific comment
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the comment creator
  - `ticketId`: ID of the ticket containing the comment
  - `commentId`: ID of the comment to retrieve

#### Remove Comment
- **Endpoint:** `DELETE /users/{userId}/tickets/{ticketId}/comments/{commentId}`
- **Description:** Delete a specific comment
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the comment creator
  - `ticketId`: ID of the ticket containing the comment
  - `commentId`: ID of the comment to remove

#### Comment Audit Logs
- **Endpoint:** `GET /users/{userId}/tickets/{ticketId}/comments/{commentId}/audit-logs`
- **Description:** Retrieve audit logs for a specific comment
- **Authentication:** Bearer Token Required
- **Path Parameters:**
  - `userId`: ID of the comment creator
  - `ticketId`: ID of the ticket containing the comment
  - `commentId`: ID of the comment to retrieve logs for

## Notes
- All endpoints require JWT authentication
- Role-Based Access Control (RBAC) is implemented
- Audit logging is available for tickets and comments
- Base URL: `http://localhost:8080`
</details>

## 💡 **Contribute!**  
Feel free to reach out for improvements in design and code quality.  
You’re welcome to create PRs to add new functionalities!


## License
This project is licensed under the **MIT License**.

