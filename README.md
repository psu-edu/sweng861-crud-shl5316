# sweng861-crud-shl5316

## Project Overview

* **Name:** Samuel Lee
* **Course Name:** SWENG 861 – Software Construction
* **Project Idea:** Task & Project Tracker API (Jira-lite)
* **Description:** A robust backend service for managing users, projects, tasks, and comments featuring role-based workflows (To Do, In Progress, Done). Designed to demonstrate clean architecture, complex relational mappings, and comprehensive input validation.

---

## Repository Layout

* `/backend` — Spring Boot backend (Maven)
* `/frontend` — Static frontend

## Tech Stack

* **Frontend:** Static HTML5, CSS3, and Vanilla JavaScript
* **Backend:** Java 21 with Spring Boot and Spring Web 
* **Build System:** Apache Maven managed via `pom.xml` alongside the Maven wrapper scripts
* **Cloud Storage:** Amazon S3

---
## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine using Git:
```bash
git clone https://github.com/{{your-username}}/sweng861-crud-shl5316.git
cd sweng861-crud-shl5316
```

### 2. Prerequisites
- Java 21+ installed and JAVA_HOME set (verify with `java -version`).
- Node/npm (optional) if you want a simple static server for the frontend (`npx http-server`).

### 3. Build & Run — macOS / Linux
Make the Maven wrapper executable (one-time):
```bash
chmod +x backend/mvnw
```
Build the backend and run tests:
```bash
./backend/mvnw clean install
```
Run the app:
```bash
./backend/mvnw spring-boot:run
```
Build without tests:
```bash
./backend/mvnw clean install -DskipTests
```
Run the packaged jar after building:
```bash
java -jar backend/target/*.jar
```

### 4. Build & Run — Windows (PowerShell or CMD)
Use the included Windows wrapper (no global Maven required):
```powershell
backend\mvnw.cmd clean install
backend\mvnw.cmd spring-boot:run
```
Or run the packaged jar after build:
```powershell
java -jar backend\target\*.jar
```

### 5. Frontend (cross-platform)
Open the static frontend directly in a browser:
- Open `frontend/index.html` in your browser (double-click), or
Serve it with a simple static server (recommended):
```bash
npx http-server frontend -p 8000
# then open http://localhost:8000
```

### 6. Quick verification
- Backend health: with the backend running:
```bash
curl -sS http://localhost:8080/health
# expected: { "status": "ok" }
curl -sS http://localhost:8080/api/hello
# expected: { "message": "Hello, World!" }
```
- Frontend image: serve frontend and open `http://localhost:8000` (or your S3 static site URL) and confirm the uploaded image loads.

### Notes
- Prefer using the Maven wrapper included in `backend/` to ensure reproducible builds across machines.
- If you prefer system Maven, replace `./backend/mvnw` with `mvn -f backend` (Windows: `mvn -f backend`).
- If serving the frontend from S3 (HTTPS), ensure your backend CORS and S3 CORS include the deployed origin to avoid browser CORS errors.
