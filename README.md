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

## Authentication Strategy
**Option A** -  Social Login (B2C) was selected for Week 2 Assignment. The decision was to leverage OIDC with Google as
the application can authenticate the user and ask for permission to view/edit their calendar at the exact same time.
This decision was driven by the need for a user-friendly and quick sign-in feature without managing passwords. 
In addition, my personal project will be building a campus scheduler service, aligning with the suggestion that social
login is best for simple student-facing tools.  

#### Short description of authentication flow:
The user clicks 'Log in with Google' and grants profile access. Google then redirects back to our backend callback 
with an authorization code, which the server trades for tokens, synchronizes the user record, and issues a session JWT.

#### Simple flow diagram:
Client → Login button → Google Identity Provider → User Consent → Redirect to Backend Callback → Backend 
→ Token exchange → DB User Sync → Protected API


---
## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine using Git:
```bash
git clone https://github.com/psu-edu/sweng861-crud-shl5316.git
cd sweng861-crud-shl5316
```

### 2. Prerequisites
- Java 21+ installed and JAVA_HOME set (verify with `java -version`).
- Node/npm for the frontend (`npx http-server`).

### 3. Build & Run — macOS / Linux
Make the Maven wrapper executable (one-time):
```bash
cd backend/
chmod +x ./mvnw
```
Build the backend and run tests:
```bash
./mvnw clean install
```
Run the app:
```bash
./mvnw spring-boot:run
```
Build without tests:
```bash
./mvnw clean install -DskipTests
```
Run the packaged jar after building:
```bash
java -jar backend/target/*.jar
```

### 4. Build & Run — Windows (PowerShell or CMD)
Use the included Windows wrapper (no global Maven required):
```powershell
cd backend
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```
Or run the packaged jar after build:
```powershell
java -jar .\target\*.jar
```

### 5. Frontend (cross-platform)
The UI lives in `/frontend` only. Run it separately from the API (Google login redirects back here, not to port 8080):
```bash
npx http-server frontend -p 8000
# then open http://localhost:8000
```
Optional: `FRONTEND_URL` (default `http://localhost:8000`) controls the post-login and logout redirect. Google Cloud Console should still use `http://localhost:8080/login/oauth2/code/google` as the OAuth callback.

Opening `frontend/index.html` as a file:// URL will not work for login (cookies and CORS).

### 6. Quick verification
With backend on 8080 and frontend on 8000:
```bash
curl -sS http://localhost:8080/health
# expected: { "status": "ok" }
```
Open `http://localhost:8000`, sign in with Google, and confirm `/api/user` shows as authenticated.

### Notes
- Prefer using the Maven wrapper included in `backend/` to ensure reproducible builds across machines.
- If you prefer system Maven, replace `./backend/mvnw` with `mvn -f backend` (Windows: `mvn -f backend`).
- If serving the frontend from S3 (HTTPS), ensure your backend CORS and S3 CORS include the deployed origin to avoid browser CORS errors.
