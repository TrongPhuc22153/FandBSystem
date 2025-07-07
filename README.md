# FandBSystem

FandBSystem is a comprehensive restaurant management platform designed to streamline food and beverage operations. It provides an intuitive web interface for customers and staff, supporting features such as menu browsing, order placement, table reservations, payment processing, and real-time notifications. The system is built with a Spring Boot backend, a React frontend, and uses PostgreSQL for data storage. Deployment is simplified with Docker and Docker Compose.

## Features

- **User Authentication & Authorization:** Secure login, registration, and role-based access control for customers, employees, and admins.
- **Product & Category Management:** Add, edit, and manage food and beverage items and categories.
- **Order Management:** Place, update, and track dine-in and online orders with real-time status updates.
- **Table Reservation:** Reserve tables, manage table occupancy, and view availability.
- **Cart & Checkout:** Shopping cart functionality with checkout process, including payment integration (PayPal, etc.).
- **Payment Management:** Multiple payment methods, payment processing, and refund handling.
- **Notification System:** Real-time notifications for order status, promotions, and system alerts.
- **Reporting & Analytics:** Generate sales, order, and customer reports for business insights.
- **User Profile & Address Book:** Manage user profiles, shipping addresses, and preferences.
- **Responsive UI:** Modern, mobile-friendly design for seamless experience across devices.


## Technologies Used

- **Backend:** Java, Spring Boot, Spring Security, JPA/Hibernate, Maven
- **Frontend:** React, React Router, Context API, CSS
- **Database:** PostgreSQL
- **Authentication:** JWT (JSON Web Token)
- **Containerization:** Docker, Docker Compose
- **Web Server (Frontend):** Nginx
- **Payment Integration:** PayPal API
- **Image Uploading:** Cloudinary
- **Email Service:** SMTP (Gmail or compatible)

## Project Structure

- `backend/` — Spring Boot REST API for business logic and data management
- `frontend/` — React application for the user interface
- `dockercompose/` — Docker Compose files for local and host deployments

## Prerequisites

- [Docker](https://www.docker.com/)
- [Node.js](https://nodejs.org/) (for local frontend development)
- [Java 17+](https://adoptopenjdk.net/) (for local backend development)

## Getting Started

### 1. Clone the repository

```sh
git clone <your-repo-url>
cd FandBSystem
```

### 2. Running with Docker Compose

You can run the entire stack using Docker Compose:

```sh
cd dockercompose/local
# For PowerShell:
docker-compose up --build
```
This will build and start both backend and frontend containers.

### 3. Running Backend Locally

```sh
cd backend
./mvnw spring-boot:run
```
The backend will be available at `http://localhost:8080` by default.

### 4. Running Frontend Locally

```sh
cd frontend
npm install
npm start
```
The frontend will be available at `http://localhost:3000` by default.


## Configuration

- Backend configuration: `backend/src/main/resources/application.properties`
- Frontend configuration: `frontend/src/`

### Required Environment Variables

Before running the backend server, ensure the following environment variables are set (or provided in your Docker Compose/service configuration):

- `DB_HOST` — PostgreSQL database host (e.g., `localhost` or container name)
- `DB_PORT` — PostgreSQL port (default: `5432`)
- `DB_NAME` — Database name
- `DB_USERNAME` — Database user
- `DB_PASSWORD` — Database password
- `JWT_SECRET` — Secret key for JWT authentication
- `PAYPAL_CLIENT_ID` — PayPal API client ID (for payment integration)
- `PAYPAL_CLIENT_SECRET` — PayPal API client secret
- `CLOUDINARY_NAME` — Cloudinary cloud name (for image uploading)
- `CLOUDINARY_API_KEY` — Cloudinary API key
- `CLOUDINARY_API_SECRET` — Cloudinary API secret
- `MAIL_USERNAME` — Email address used to send system emails
- `MAIL_PASSWORD` — Application password or SMTP password for the email account

These can be set in your environment, `.env` file, or as part of your Docker Compose service definitions. The backend will not start without the required database, authentication, image upload, and mail variables.

## Useful Commands

- Build backend: `./mvnw clean package` (or `mvnw.cmd` on Windows)
- Build frontend: `npm run build`
- Stop Docker Compose: `docker-compose down`