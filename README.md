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
---
### Authentication Flow
The user clicks 'Log in with Google' and grants profile access. Google then redirects back to our backend callback with
an authorization code. The server trades this code for access tokens, synchronizes the user record in the database, 
and establishes an authenticated server session (cookie) for the user.

#### Simple Flow Diagram:
Client → Login button → Google Identity Provider → User Consent → Redirect to Backend Callback → Backend 
→ Token Exchange → DB User Sync → Authenticated Session → Protected API
---
### Protected Endpoint
**Endpoint:** `GET /api/hello`

`SecurityConfig` enforces authentication for all `/api/**` routes. Unauthenticated requests are intercepted by a custom entry point and immediately rejected with a `401 Unauthorized` status. 
The controller reads the authenticated user's session via `@AuthenticationPrincipal OAuth2User`. This then safely passes the user's Google profile directly into the controller logic to produce a response.

### OWASP Practices Applied:
* **Secure Token Storage:** Tokens are stored securely in the backend, not in the client.
* **HTTPS Enforcement:** All communications are over HTTPS to protect data in transit.
* **Input Validation:** All user inputs are validated to prevent injection attacks.
* **Authentication:** The application uses OAuth 2.0 and OpenID Connect for secure authentication.

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

### 6. Postgres Database
The backend uses Postgres and can be run in Docker:
```bash
docker run -d --name campus-scheduler-db -e POSTGRES_DB=campus_scheduler -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:16-alpine 
```

### 7. Quick Verification
With backend on 8080:
```bash
curl -sS http://localhost:8080/health
# expected: { "status": "ok" }
```

Docker verification:
```bash
docker ps
# expected: Table showing campus-scheduler-db running
```

Finally, verify the frontend is running:

Open `http://localhost:8000`, sign in with Google, and confirm `/api/user` shows as authenticated.


