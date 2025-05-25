# Personal Training Scheduling System

**Personal Training Scheduling System** is a microservices-based application developed as part of the "Software Components" course. The project is designed to manage personal training sessions, including user registration, session scheduling, and automated notifications, all orchestrated through an API gateway.

## Project Overview

The **Personal Training Scheduling System** is divided into three main services: User Service, Training Scheduling Service, and Notification Service. These services work together to provide a seamless experience for managing personal training sessions, ensuring that users can easily schedule, modify, and be notified about their training activities.

### Key Features:
- **User Service**:
  - User registration with activation email.
  - Authentication using JWT tokens.
  - Role-based access control for admins, clients, and gym managers.
  - Profile management for updating user information.

- **Training Scheduling Service**:
  - Management of gym facilities and training sessions.
  - Searching and filtering available training slots.
  - Booking and canceling training sessions.
  - Support for individual and group training sessions.

- **Notification Service**:
  - Sending activation emails, booking confirmations, and reminders.
  - Asynchronous notification handling through a message broker.
  - Archiving sent notifications with filtering options.

- **API Gateway**:
  - Routing and service discovery using Netflix Zuul and Eureka.

### Technologies Used

- **Programming Language**: Java
- **Microservices**: Spring Boot
- **API Gateway**: Netflix Zuul
- **Service Discovery**: Netflix Eureka
- **Messaging**: ActiveMQ
- **Database**: H2 Databse

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven or Gradle
- (Specify the message broker and database setup required)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/nivancev-raf/training-scheduling-system.git
   ```
2. Build the project using Maven or Gradle:
   ```bash
   cd training-scheduling-system
   mvn clean install
   ```
3. Start the services (example for the user service):
   ```bash
   java -jar user-service/target/user-service.jar
   ```
4. Configure and run the API Gateway:
   ```bash
   java -jar api-gateway/target/api-gateway.jar
   ```

## Documentation

The complete documentation for the **Personal Training Scheduling System** project, including the technical and functional requirements, is available in the following PDF document:
- [Training Scheduling System Documentation](https://github.com/nivancev-raf/training-scheduling-system/blob/main/SK-drugi%20projekat%20(2023).pdf)

## Contact
For any questions or feedback, please reach out to luka.zarkovo29@gmail.com.
