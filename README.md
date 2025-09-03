# MTG-Tournament-App
---
⚠ **Important: The app is in development mode there can be crashes or some features could not work properly or at all. This disclaimer will disappear when we will be pleased with the product.**

---
Welcome to the MTG Tournament App, a full-stack application designed to manage Magic The Gathering tournaments. This application features a Java Spring Boot backend, a Next.js frontend, and uses Keycloak for authentication, all containerized with Docker.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
  - [Keycloak Setup](#keycloak-setup)
  - [Admin Panel](#admin-panel)
  - [Player View](#player-view)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Future Work](#future-work)

## Features

- **Tournament Management:** Create, update, start, and finish tournaments.
- **Player Management:** Admins can manage players, and players can join open tournaments.
- **Round Robin Pairing:** Automatically generate all "everyone vs. everyone" matches for a tournament stage.
- **Real-time Match View:** Synchronized match state between players using WebSockets.
- **Achievement Tracking:** Define and track custom achievements for each tournament.
- **Scoreboard:** Live-updating scoreboard with points and achievement stats.
- **Secure Authentication:** User authentication and role management handled by Keycloak.

## Tech Stack

**Backend:**
- Java 21+
- Spring Boot 3.x
- Spring Security (OAuth2 / JWT)
- Spring Data JPA (Hibernate)
- Spring WebSocket (with STOMP)
- PostgreSQL

**Frontend:**
- Next.js 14+ (App Router)
- React 18+
- TypeScript
- Ant Design (for UI components)
- Tailwind CSS (for styling)
- Axios (for HTTP requests)
- StompJS & SockJS (for WebSocket communication)

**Authentication:**
- Keycloak

**Infrastructure:**
- Docker & Docker Compose

## Architecture

The application is composed of several containerized services managed by Docker Compose:
- `frontend`: The Next.js web application.
- `backend-1`: The Spring Boot REST API and WebSocket server.
- `db-1`: PostgreSQL database for the main application.
- `keycloak`: The Keycloak authentication server.
- `keycloak-db`: MySQL database dedicated to Keycloak.

All services communicate over a shared Docker network.

⚠ **Important:** At the moment there is a problem to initialize frontend service but this issue has the highest priority to fix so this message should disappear in no time.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop/) installed and running.
- [Node.js](https://nodejs.org/) and npm (for potential local frontend debugging).
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (for potential local backend debugging).
- A web browser (Chrome or Firefox recommended).

### Configuration

1.  **Clone the repository:**
    ```bash
    git clone https://your-repo-url.com/mtg-tournament-app.git
    cd mtg-tournament-app
    ```

2.  **Frontend Environment Variables:**
    Create a `.env.local` file in the `frontend/` directory by copying the example:
    ```bash
    cp frontend/.env.local.example frontend/.env.local
    ```
    Open `frontend/.env.local` and set the `NEXTAUTH_SECRET`. You can generate a secret using the command: `openssl rand -base64 32`.

3.  **Backend Environment Variables:**
    The backend configuration is managed in `backend/src/main/resources/application.yml`. No changes are required for the default Docker setup.

### Running the Application

The entire stack can be launched with a single command from the project's root directory.

1.  **Build and Start All Services:**
    ```bash
    docker-compose up --build
    ```
    - The `--build` flag ensures that your `frontend` and `backend` images are rebuilt if there are any code changes.
    - To run in the background, add the `-d` flag: `docker-compose up --build -d`.

2.  **Accessing the Services:**
    - **Frontend App:** [http://localhost:3000](http://localhost:3000)
    - **Backend API (Swagger UI):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - **Keycloak Admin Console:** [http://localhost:8443](http://localhost:8443)
      - **Username:** `admin`
      - **Password:** `admin`

3.  **Stopping the Application:**
    To stop all services, press `Ctrl + C` in the terminal or run:
    ```bash
    docker-compose down
    ```
    To stop services and **delete all data** (including databases), run:
    ```bash
    docker-compose down -v
    ```

## Usage

### Keycloak Setup

Upon first launch, you will need to configure Keycloak:
1.  Log in to the Keycloak Admin Console (`admin`/`admin`).
2.  Go to the "Credentials" tab for the `frontend-web` client and copy the **Client secret**. Paste this value into the `KEYCLOAK_SECRET` variable in `frontend/.env.local`.
3.  Optionaly add test users. Remember to give them roles `ADMIN` od `USER`

All other important setup info is loades from `realm-export.json` file. But feel frea to change it just remember to update other parts of the app that depends on keycloak.

### Admin Panel
- As an `ADMIN` user, you can create new tournaments, start them (which generates matches), manage players, and finalize results.

### Player View
- As a `USER`, you can view tournaments, join them before they start, and participate in matches. The match view is updated in real-time.

## API Endpoints

The backend exposes a REST API for managing application data. The full specification is available via Swagger UI at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

Key endpoints include:
- `GET /api/tournaments`: Get a list of all tournaments.
- `POST /api/tournaments`: Create a new tournament.
- `POST /api/tournaments/{id}/join`: Join a tournament.
- `POST /api/tournaments/{id}/start`: Start a tournament and generate matches.
- `GET /api/matches/{id}`: Get details for a specific match.
- `PUT /api/matches/{id}/results`: Update match results.

## Project Structure
```
├── backend/ # Spring Boot Application
│ ├── src/
│ └── pom.xml
├── frontend/ # Next.js Application
│ ├── app/
│ ├── components/
│ └── package.json
└── docker-compose.yml # Main Docker Compose file
```
## Future Work

- [ ] Fix issue with creating frontend docker image.
- [ ] Implement a Swiss-style pairing system for later rounds.
- [ ] Add a user profile page (`/my-profile`).
- [ ] Develop a more detailed, real-time UI for the match view (`StatsPanel`).
- [ ] Write unit and integration tests for the backend.
- [ ] Add end-to-end tests with Cypress or Playwright.
