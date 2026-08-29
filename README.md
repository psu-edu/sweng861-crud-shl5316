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
git clone [https://github.com/{{your-username}}/sweng861-crud-shl5316.git](https://github.com/{{your-username}}/sweng861-crud-shl5316.git)
cd sweng861-crud-shl5316
```
### 2. Build the Project
Run the following command to build the project:
```bash
mvn clean install
```

### 3. Run the Application
Backend (Spring Boot - Maven)
```bash
mvn -f agile-tracker spring-boot:run
```
Frontend (static)

- Open frontend/index.html in a browser
- serve with a static server, e.g. `npx http-server frontend`

