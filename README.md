# Project PaymentChain

This project is a Microservices - CRUD application built using Java 21, Spring Boot, Spring Security, Docker, Spring Cloud Admin, PostgreSQL, and API Gateway. The application is designed to provide a secure and scalable solution, utilizing modern technologies to manage transactions and customers.

## Microservices

- **Customer
- **Product
- **Transactions

## Technologies Used

- **Java 21**: The latest version of Java, offering advanced features and performance improvements.
- **Spring Boot**: A framework that simplifies the setup and development of Java applications, particularly for microservices.
- **Spring Security**: A powerful and customizable authentication and access control framework for Java applications.
- **Docker**: Used for containerizing the application, making it easier to deploy and scale across different environments.
- **Spring Cloud Admin**: Provides a user interface for managing Spring Boot applications deployed on the cloud.
- **PostgreSQL**: A powerful, open-source relational database used to store application data.
- **API Gateway**: Handles routing and authentication for the services.

## Prerequisites

- **Java 21**: Ensure you have Java 21 installed.
- **Docker**: Docker is required to run the application containers.
- **PostgreSQL**: Set up PostgreSQL to manage the database.
- **Spring Cloud Admin**: Used for monitoring Spring Boot applications.

## Features

- **CRUD Operations**: Create, Read, Update, and Delete operations on customer and transaction data.
- **API Gateway**: Acts as a gateway for routing requests to the backend services.
- **Security**: Authentication and authorization using Spring Security to ensure secure access to the application.
- **Dockerized**: The entire application is containerized for ease of deployment.
- **Spring Cloud Admin Integration**: Allows monitoring and managing the services in a distributed system.

## Getting Started

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
https://github.com/rvega1204/spring-boot-paymentchain.git
cd project-name
```
### 2. Setup PostgreSQL
Ensure PostgreSQL is installed and running. You can use Docker to quickly set up PostgreSQL:

```bash
docker run --name postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
```

### 3. Configuration
Update the application.properties file with your PostgreSQL connection settings:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.
