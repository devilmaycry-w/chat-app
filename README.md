# Chat-App

## Overview
Chat-App is a real-time messaging application built using Spring Boot and WebSocket. 
The project provides secure and private messaging features with user authentication powered by JWT. 
Designed to handle both public and private chats, the app serves static frontend assets alongside backend APIs.

---

## Features
- **User Authentication**: Registration and login with secure JWT token-based authentication.
- **Real-Time Messaging**: WebSocket integration for instant chat experiences.
- **Secure Private Chats**: Each conversation is accessible only to authorized users.
- **Chat History**: Stores and retrieves messages for active sessions.
- **Scalable Architecture**: Built with Spring Boot and MySQL.

---

## Tech Stack
- **Backend**:
  - Spring Boot
  - Spring Security
  - WebSocket
  - JWT Authentication
  - MySQL Database
- **Frontend**:
  - HTML, CSS, JavaScript
  - Fetch API for API calls

---

## How to Run Locally
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/devilmaycry-w/chat-app.git
   cd chat-app

2. Backend Setup:

Navigate to the backend directory (/chatapp).
Run the following command to start the Spring Boot application:

```bash
mvn spring-boot:run
```

-> Ensure MySQL is installed and the application.properties file contains valid database credentials.

3. Frontend Access:

-> Open src/main/resources/static/index.html in your browser.

