# FinWise AI - Expense Tracker API

An AI-powered expense tracking REST API built with Java, Spring Boot, Spring Security, JWT Authentication, and MySQL.

## Features

- 🔐 JWT-based Authentication & Authorization
- 💰 Full CRUD operations for expenses and categories
- 🤖 AI-powered expense categorization (keyword-based intelligent matching)
- 📊 Monthly spending summary with category-wise breakdown
- 💡 Smart spending insights (month-over-month comparison, top spending category)
- 🔒 Ownership-based access control (users can only access their own data)
- 📖 Interactive API documentation via Swagger/OpenAPI

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 4.1
- **Security:** Spring Security + JWT
- **Database:** MySQL, Spring Data JPA / Hibernate
- **Documentation:** Swagger / OpenAPI
- **Build Tool:** Maven

## API Endpoints

### Auth
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

### Expenses
- `POST /api/expenses` - Create a new expense
- `GET /api/expenses` - Get all expenses for logged-in user
- `GET /api/expenses/{id}` - Get expense by ID
- `PUT /api/expenses/{id}` - Update an expense
- `DELETE /api/expenses/{id}` - Delete an expense

### Categories
- `POST /api/categories` - Create a category
- `GET /api/categories` - Get all categories

### AI Features
- `POST /api/ai/categorize` - Auto-suggest category based on description
- `GET /api/ai/monthly-summary?month={m}&year={y}` - Get monthly spending summary
- `GET /api/ai/insights` - Get AI-generated spending insights

## Setup Instructions

1. Clone the repository
```bash
git clone <your-repo-url>
```

2. Create a MySQL database
```sql
CREATE DATABASE finwiseai_db;
```

3. Copy `application.properties.example` to `application.properties` and add your MySQL credentials

4. Run the application
```bash
mvn spring-boot:run
```

5. Access Swagger documentation at
   http://localhost:8080/swagger-ui.html

## Author
Ayush