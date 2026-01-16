# Digital Wallet System - Spring Boot

## Description
RESTful API for a digital wallet system supporting user management, wallet operations, P2P transfers, and merchant payments.

## Tech Stack
- Java 17
- Spring Boot 3.x
- PostgreSQL
- Maven
- Swagger/OpenAPI

## Prerequisites
- JDK 17+
- PostgreSQL 14+
- Maven 3.8+

## Setup Instructions

### 1. Clone Repository
git clone <repository-url>
cd digital-wallet-springboot

### 2. Create Database
CREATE DATABASE digital_wallet;
CREATE USER wallet_user WITH PASSWORD 'wallet_password';
GRANT ALL PRIVILEGES ON DATABASE digital_wallet TO wallet_user;

### 3. Configure Application
Update src/main/resources/application.yml with your database credentials

### 4. Run Application
./mvnw spring-boot:run

### 5. Access API
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Base: http://localhost:8080/api/v1

## API Endpoints
- Users: /api/v1/users (CRUD, search)
- Wallets: /api/v1/wallets (balance, add money, limits)
- Transactions: /api/v1/transactions (transfers, payments, history)
- Merchants: /api/v1/merchants (CRUD, payments)

## Project Structure
- controller/: REST controllers
- service/: Business logic
- repository/: Data access
- entity/: JPA entities
- dto/: Request/response DTOs
- exception/: Custom exceptions and handler
- mapper/: Entity-DTO mappers
- config/: OpenAPI config

## Testing
Import postman/Digital_Wallet_API.postman_collection.json into Postman

## Author
Joshua Maina Njomo