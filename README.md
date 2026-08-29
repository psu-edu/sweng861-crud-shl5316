# sweng861-crud-shl5316

# Project Overview
```
Name:               Samuel Lee
Course Name:        SWENG 861 – Software Construction
Project Idea:       Task & Project Tracker API (Jira-lite)
Description:        A robust backend service for managing users, projects, 
                    tasks, and comments featuring role-based workflows 
                    (To Do, In Progress, Done). Designed to demonstrate 
                    clean architecture, complex relational mappings, and 
                    comprehensive input validation.
```

Repository layout (restructured):

- /agile-tracker    -> Spring Boot backend (Maven)
- /frontend         -> Static frontend (index.html + scripts.js)
- /README.md        -> This file

How to run

1) Backend (Spring Boot - Maven)
   mvn -f agile-tracker spring-boot:run

2) Frontend (static)
   Open frontend/index.html in a browser (or serve with a static server, e.g. `npx http-server frontend`)

Notes
- CORS is enabled for local dev (http://localhost:3000 and http://localhost:8000).
- Legacy /backend folder removed during restructure.
