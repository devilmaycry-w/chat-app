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


⚠️ Known Issues
Error: 403 Forbidden when loading previous chat histories for specific users.

Impact: Users are unable to view older messages unless part of the active session.
Planned Fix: Investigate token validation, backend authorization logic, and frontend request alignment.

》》Future Enhancements :

▪︎ Add file-sharing capabilities within chats.
▪︎ Implement search functionality for messages.
▪︎ Include user presence indicators (online/offline status).
▪︎ Resolve chat history retrieval error (403 Forbidden).


Let me know if there’s anything you’d like to add or tweak! 🚀