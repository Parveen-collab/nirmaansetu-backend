<div align="center">

# 🏗️ NirmaanSetu

### Building India's Digital Construction Ecosystem

A scalable full-stack platform connecting skilled workers, employers, suppliers, and customers through a unified digital marketplace for the construction industry.

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-success?style=for-the-badge&logo=springboot)]()
[![Next.js](https://img.shields.io/badge/Next.js-16-black?style=for-the-badge&logo=nextdotjs)]()
[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)]()
[![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql)]()
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis)]()
[![JWT](https://img.shields.io/badge/JWT-Secure_Authentication-black?style=for-the-badge&logo=jsonwebtokens)]()
[![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker)]()

[🌐 Live Demo](https://nirmaan-setu.vercel.app/) •
📖 API Documentation •
🚀 Backend •
💻 Frontend

</div>

---

# 📚 Table of Contents

- [Overview](#-overview)
- [Problem Statement](#-problem-statement)
- [Solution](#-solution)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [User Roles](#-user-roles)
- [Security](#-security)
- [Performance](#-performance)
- [API Documentation](#-api-documentation)
- [Installation](#-installation)
- [Screenshots](#-screenshots)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)

---

# 📖 Overview

**NirmaanSetu** is a scalable construction ecosystem platform designed to digitize and simplify the way construction professionals connect, collaborate, and conduct business.

The platform serves as a centralized marketplace where skilled workers, contractors, builders, suppliers, and customers can interact through a secure and efficient digital ecosystem.

Unlike traditional fragmented solutions, NirmaanSetu brings workforce hiring, supplier discovery, communication, and construction service management together in a single platform.

The project follows a modern full-stack architecture using **Spring Boot**, **Next.js**, **MySQL**, **Redis**, and **JWT Authentication**, making it suitable for scalable cloud deployment and future enterprise expansion.

---

# ❗ Problem Statement

The construction industry remains one of the least digitized sectors despite employing millions of workers and involving numerous stakeholders.

Several challenges continue to impact productivity and project efficiency:

- Skilled workers struggle to find consistent employment opportunities.
- Contractors spend significant time searching for verified labor.
- Construction material suppliers have limited digital visibility.
- Customers lack a centralized platform to discover trusted workers and suppliers.
- Communication between stakeholders is fragmented.
- Manual coordination often leads to delays, higher operational costs, and reduced transparency.

Existing platforms typically solve only a single problem—such as job listings or e-commerce—without providing an integrated ecosystem tailored to the construction industry.

---

# 💡 Solution

NirmaanSetu addresses these challenges by providing a unified digital platform where multiple stakeholders can seamlessly collaborate.

The platform enables:

- Worker discovery and hiring
- Construction job management
- Supplier marketplace
- Secure authentication
- Role-based dashboards
- Notifications
- Digital business visibility
- Scalable backend infrastructure

By bringing these services together, NirmaanSetu simplifies the construction workflow while improving accessibility, transparency, and operational efficiency.

---

# ✨ Key Features

| Feature | Description |
|----------|-------------|
| 🔐 Secure Authentication | JWT-based authentication with OTP verification and refresh token support |
| 👷 Employee Portal | Create professional profiles, discover jobs, and manage applications |
| 🏢 Employer Portal | Post jobs, manage hiring, and connect with skilled workers |
| 🏪 Supplier Marketplace | Showcase construction materials and connect with customers |
| 👥 Multi-Role Access | Dedicated dashboards for Employees, Employers, Suppliers, and Administrators |
| 📢 Notification System | Real-time notifications for platform activities |
| 📊 Dashboard & Analytics | Role-specific dashboards with business insights |
| ⚡ Redis Integration | High-performance caching for improved response times |
| 📜 API Documentation | Interactive OpenAPI/Swagger documentation |
| 📈 Monitoring | Application monitoring using Spring Boot Actuator and ELK stack |
| 🔒 Role-Based Authorization | Secure access control for protected resources |
| 🐳 Docker Support | Containerized development and deployment |

---

# 🛠️ Technology Stack

## Frontend

| Technology | Purpose |
|------------|---------|
| Next.js 16 | React Framework |
| React 19 | UI Library |
| TypeScript | Type Safety |
| Tailwind CSS | Styling |
| Material UI | UI Components |

---

## Backend

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming Language |
| Spring Boot 3.5 | Backend Framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database Access |
| Hibernate | ORM |
| Maven | Build Tool |

---

## Database & Storage

| Technology | Purpose |
|------------|---------|
| MySQL | Primary Database |
| Redis | Caching & Performance |

---

## DevOps & Monitoring

| Technology | Purpose |
|------------|---------|
| Docker | Containerization |
| Docker Compose | Local Infrastructure |
| Elasticsearch | Search & Logging |
| Logstash | Log Processing |
| Kibana | Visualization |
| Prometheus | Monitoring |
| Spring Boot Actuator | Health Checks & Metrics |

---

## Authentication & Integrations

| Technology | Purpose |
|------------|---------|
| JWT | Stateless Authentication |
| Twilio | OTP Verification |
| Swagger/OpenAPI | API Documentation |

---

# 🏛️ System Architecture

NirmaanSetu follows a modern three-tier architecture consisting of a responsive frontend, a secure RESTful backend, and a scalable data layer. Each component has a clearly defined responsibility, making the application easier to maintain, extend, and deploy.

```text
                           Users
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
   Employees             Employers            Suppliers
        │                     │                     │
        └─────────────── Web Browser ───────────────┘
                              │
                     Next.js + React Frontend
                              │
                    HTTPS / REST APIs (JWT)
                              │
                Spring Boot Backend (Java 21)
                              │
      ┌─────────────┬──────────┼──────────────┬─────────────┐
      │             │          │              │             │
 Authentication   Business    Redis       Twilio OTP     Scheduler
 & Authorization   Logic      Cache          Service        Jobs
      │
      ▼
   MySQL Database
      │
      ▼
 ELK Stack + Prometheus
```

---

## 🧩 Architecture Overview

### Frontend Layer

The frontend provides an intuitive and responsive user experience for all platform users. It communicates with the backend exclusively through secure REST APIs and handles authentication, routing, state management, and UI rendering.

**Responsibilities**

- Responsive user interface
- Authentication flow
- Dashboard rendering
- Form validation
- API integration
- Role-based navigation

**Technologies**

- Next.js 16
- React 19
- TypeScript
- Tailwind CSS
- Material UI

---

### Backend Layer

The backend is responsible for implementing the platform's business logic and exposing secure REST APIs for all client applications.

Core responsibilities include:

- Authentication & Authorization
- User Management
- Job Management
- Supplier Management
- Notification Management
- OTP Verification
- Dashboard APIs
- File Handling
- Scheduled Tasks

The backend follows a layered architecture consisting of Controllers, Services, Repositories, DTOs, and Entities.

---

### Data Layer

Application data is stored in MySQL using Spring Data JPA and Hibernate.

Redis is used to improve performance through caching and temporary data storage.

The data layer manages:

- Users
- Roles
- Jobs
- Applications
- Suppliers
- Products
- Notifications
- OTP Requests

---

# 📂 Project Structure

```text
backend/
│
├── src/
│   ├── main/
│   │
│   ├── java/
│   │   └── com/nirmaansetu/
│   │
│   │       ├── config/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       ├── dto/
│   │       ├── mapper/
│   │       ├── security/
│   │       ├── scheduler/
│   │       ├── util/
│   │       ├── exception/
│   │       └── validation/
│   │
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

# 👥 User Roles

The platform supports multiple user roles with dedicated dashboards and permissions.

| Role | Responsibilities |
|------|------------------|
| 👷 Employee | Create profile, search jobs, apply for work, manage personal information |
| 🏢 Employer | Post jobs, manage applicants, hire workers, manage projects |
| 🏪 Supplier | Publish construction materials, manage inventory, receive customer inquiries |
| 👨‍💼 Administrator | Manage users, verify accounts, monitor platform activity, configure the system |

---

# 🧩 Core Modules

The backend is organized into independent business modules, making the application easier to maintain and scale.

| Module | Purpose |
|---------|---------|
| Authentication | Login, Registration, JWT, Refresh Token, OTP |
| User Management | Employee, Employer, Supplier profiles |
| Job Management | Job creation, updates, applications |
| Marketplace | Material listings and supplier management |
| Dashboard | Role-based analytics and statistics |
| Notification | Platform notifications |
| File Management | Image and document handling |
| Scheduler | Background maintenance tasks |
| Security | Authorization, authentication, access control |

---

# 🗄️ Database Design

The application uses a relational database designed to support multiple user roles and business entities.

### Core Entities

```text
User
 ├── Employee
 ├── Employer
 └── Supplier

Employer
      │
      ├────────────► Jobs
      │                 │
      │                 ▼
      │            Applications
      │                 ▲
      │                 │
Employee ───────────────┘

Supplier
      │
      ▼
Construction Materials

User
      │
      ▼
Notifications
```

---

## Primary Data Domains

- Users
- Roles
- Authentication
- Jobs
- Applications
- Construction Materials
- Suppliers
- Notifications
- OTP Verification

---

# 🔄 Application Workflow

```text
User Registration
        │
        ▼
OTP Verification
        │
        ▼
Account Activation
        │
        ▼
JWT Login
        │
        ▼
Role Identification
        │
        ▼
Dashboard
        │
 ┌──────┼─────────────┐
 │      │             │
 ▼      ▼             ▼
Employee Employer  Supplier
 │      │             │
 │      │             │
 ▼      ▼             ▼
Jobs   Hiring   Marketplace
```

---

# 🔀 Request Flow

Every request follows the same secure processing pipeline.

```text
Client Request
      │
      ▼
JWT Authentication
      │
      ▼
Authorization Filter
      │
      ▼
Controller
      │
      ▼
Service Layer
      │
      ▼
Repository Layer
      │
      ▼
MySQL Database
      │
      ▼
Response
```

---

# 📈 Scalability Considerations

The architecture has been designed with scalability in mind.

### Current Design

- Layered Architecture
- Stateless REST APIs
- JWT Authentication
- Modular Services
- Redis Caching
- Database Abstraction using JPA
- Docker-based deployment

### Future Enhancements

- Microservices architecture
- API Gateway
- Distributed caching
- Message queues
- WebSocket-based real-time communication
- Kubernetes deployment
- Cloud-native infrastructure

# 🔒 Security

Security is a core aspect of NirmaanSetu. The platform follows modern backend security practices to protect user data, secure API communication, and enforce role-based access control.

## Authentication

- JWT-based Authentication
- Refresh Token Support
- OTP Verification using Twilio
- Stateless Authentication
- Secure Password Storage (BCrypt)

---

## Authorization

The platform uses **Role-Based Access Control (RBAC)** to ensure users can only access resources permitted for their role.

Supported roles include:

- Employee
- Employer
- Supplier
- Administrator

Each API endpoint is protected using Spring Security authorization rules.

---

## API Security

- Protected REST APIs
- Request Validation
- DTO-based Request Handling
- Global Exception Handling
- Input Sanitization
- Secure Error Responses

---

## Credential Management

Sensitive configuration values are never hardcoded into the source code.

Configuration is managed using environment variables, including:

- Database Credentials
- JWT Secret
- Twilio Credentials
- Redis Configuration
- API Keys

---

## Production Security Checklist

| Security Feature | Status |
|------------------|:------:|
| JWT Authentication | ✅ |
| Refresh Tokens | ✅ |
| Role-Based Authorization | ✅ |
| Password Encryption | ✅ |
| OTP Verification | ✅ |
| Environment Variables | ✅ |
| Input Validation | ✅ |
| Global Exception Handling | ✅ |
| Secure REST APIs | ✅ |

---

# ⚡ Performance Optimizations

The application is designed to deliver fast response times while remaining scalable as the number of users grows.

## Backend Optimizations

- Layered Architecture
- Stateless REST APIs
- Optimized Database Queries
- Pagination Support
- DTO Mapping
- Connection Pooling
- Scheduled Background Jobs

---

## Redis Caching

Redis is used to improve application performance by reducing unnecessary database queries.

Typical use cases include:

- OTP Storage
- Frequently Accessed Data
- Temporary Session Data
- Rate Limiting Support

---

## Database Optimization

Performance improvements include:

- Indexed Database Tables
- Efficient Entity Relationships
- Hibernate Query Optimization
- Lazy Loading
- Optimized JPA Repositories

---

## Monitoring & Observability

Application health and runtime metrics are monitored using:

- Spring Boot Actuator
- Elasticsearch
- Logstash
- Kibana
- Prometheus

These tools help monitor application health, logs, and performance in production environments.

---

# 📖 API Documentation

The backend exposes RESTful APIs documented using **OpenAPI (Swagger)**.

After running the application, API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## API Modules

| Module | Description |
|---------|-------------|
| Authentication | Login, Registration, JWT, OTP |
| Users | User Profile Management |
| Employees | Employee Operations |
| Employers | Employer Operations |
| Suppliers | Marketplace Operations |
| Jobs | Job Posting & Applications |
| Notifications | User Notifications |
| Dashboard | Statistics & Analytics |

---

## Sample API Endpoints

| Method | Endpoint | Description |
|---------|-----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | User Login |
| POST | `/auth/send-otp` | Send OTP |
| POST | `/auth/verify-otp` | Verify OTP |
| GET | `/users/{id}` | Fetch User Profile |
| PUT | `/users/{id}` | Update User |
| DELETE | `/users/{id}` | Delete User |

---

# 🚀 Getting Started

## Prerequisites

Before running the project, ensure the following software is installed:

| Software | Version |
|-----------|----------|
| Java | 21+ |
| Maven | 3.9+ |
| MySQL | 8+ |
| Redis | Latest |
| Docker | Latest |
| Git | Latest |

---

# 📥 Installation

## 1. Clone the Repository

```bash
git clone https://github.com/your-username/nirmaansetu-backend.git

cd nirmaansetu-backend
```

---

## 2. Configure Environment Variables

Create the appropriate configuration file (or use environment variables) before starting the application.

Example:

```properties
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET=

REDIS_HOST=
REDIS_PORT=

TWILIO_ACCOUNT_SID=
TWILIO_AUTH_TOKEN=
TWILIO_PHONE_NUMBER=
```

---

## 3. Build the Project

```bash
./mvnw clean install
```

---

## 4. Run the Application

```bash
./mvnw spring-boot:run
```

Or

```bash
java -jar target/backend.jar
```

---

## 5. Verify the Application

Open

```text
http://localhost:8080
```

Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

Actuator

```text
http://localhost:8080/actuator
```

---

# 🐳 Running with Docker

## Build Docker Image

```bash
docker build -t nirmaansetu-backend .
```

---

## Run Docker Container

```bash
docker run -p 8080:8080 nirmaansetu-backend
```

---

## Docker Compose

Start all required services:

```bash
docker compose up -d
```

Stop services:

```bash
docker compose down
```

---

# 💻 Local Development

## Development Workflow

```text
Clone Repository
        │
        ▼
Configure Database
        │
        ▼
Configure Environment Variables
        │
        ▼
Start Redis
        │
        ▼
Run Spring Boot
        │
        ▼
Open Swagger
        │
        ▼
Begin Development
```

---

## Useful Maven Commands

Build Project

```bash
./mvnw clean install
```

Run Tests

```bash
./mvnw test
```

Package Application

```bash
./mvnw package
```

Skip Tests

```bash
./mvnw clean install -DskipTests
```

---

# ✅ Health Checks

Spring Boot Actuator provides several useful endpoints.

| Endpoint | Purpose |
|-----------|----------|
| `/actuator/health` | Application Health |
| `/actuator/info` | Application Information |
| `/actuator/metrics` | Runtime Metrics |
| `/actuator/prometheus` | Prometheus Metrics |

---

# 📸 Screenshots

The following screenshots showcase the primary features and user interfaces of NirmaanSetu.

## 🏠 Landing Page

> Modern and responsive landing page introducing the platform and its services.

![Landing Page](src/main/resources/screenshots/img_2.png)

---

## 🔐 Authentication

Secure login with JWT authentication and OTP verification.

![Login](src/main/resources/screenshots/img_4.png)

---

## 📊 Dashboard

Role-based dashboards providing personalized insights and quick access to platform features.

![Dashboard](src/main/resources/screenshots/img_5.png)

---

## 🏢 Employer Dashboard

Employers can create jobs, manage applicants, and monitor hiring activities.

![Employer Dashboard](src/main/resources/screenshots/img_6.png)

---

## 🏪 Supplier Dashboard

Suppliers can manage products, marketplace listings, and customer inquiries.

![Supplier Dashboard](src/main/resources/screenshots/img_7.png)

---

## 💼 Job Marketplace

Employees can browse available opportunities and apply for construction jobs.

![Job Listing](src/main/resources/screenshots/img_8.png)

---

## 🧱 Material Marketplace

Browse and discover construction materials from verified suppliers.

![Marketplace](src/main/resources/screenshots/img_9.png)

---

## 👷 Worker Profile

Professional worker profiles with skills and experience.

![Worker Profile](src/main/resources/screenshots/img_10.png)

---

## 🔔 Notifications

Real-time notification system for important platform updates.

![Notifications](src/main/resources/screenshots/img_11.png)

---

# 🌍 Deployment

The application is designed for cloud deployment and production environments.

## Frontend

| Platform | Status |
|----------|--------|
| Vercel | ✅ |

---

## Backend

| Platform | Status |
|----------|--------|
| Railway | ✅ |

---

## Database

| Service | Status |
|----------|--------|
| MySQL | ✅ |

---

## Cache

| Service | Status |
|----------|--------|
| Redis | ✅ |

---

## Monitoring

| Service | Status |
|----------|--------|
| Elasticsearch | ✅ |
| Logstash | ✅ |
| Kibana | ✅ |
| Prometheus | ✅ |

---

# 🚀 Deployment Architecture

```text
                    Internet
                        │
                        ▼
              Next.js Frontend (Vercel)
                        │
                 HTTPS + REST APIs
                        │
                        ▼
         Spring Boot Backend (Railway)
            │           │           │
            │           │           │
            ▼           ▼           ▼
         MySQL       Redis      ELK Stack
```

---

# 🛣️ Roadmap

## Completed

- [x] User Authentication
- [x] JWT Authentication
- [x] OTP Verification
- [x] Role-Based Authorization
- [x] Employee Portal
- [x] Employer Portal
- [x] Supplier Portal
- [x] Job Marketplace
- [x] Dashboard
- [x] Notifications
- [x] Redis Integration
- [x] Docker Support
- [x] Swagger Documentation
- [x] ELK Integration
- [x] Responsive Frontend

---

## Planned

- [ ] Real-time Chat
- [ ] Payment Gateway
- [ ] Email Notifications
- [ ] Advanced Search Filters
- [ ] Push Notifications
- [ ] Mobile Application
- [ ] Progressive Web App (PWA)
- [ ] Multi-language Support
- [ ] Advanced Analytics
- [ ] Public REST API
- [ ] Admin Analytics Dashboard
- [ ] Cloud Object Storage

---

## Long-Term Vision

The long-term goal of NirmaanSetu is to become a comprehensive digital ecosystem for the construction industry by connecting workers, employers, suppliers, and customers on a single platform. Future iterations will focus on improved scalability, enhanced collaboration features, and enterprise-ready infrastructure.

---

# 🤝 Contributing

Contributions are welcome and appreciated.

If you would like to contribute:

1. Fork the repository.
2. Create a new feature branch.

```bash
git checkout -b feature/your-feature
```

3. Commit your changes.

```bash
git commit -m "Add your feature"
```

4. Push the branch.

```bash
git push origin feature/your-feature
```

5. Open a Pull Request.

Please ensure your code follows the project's coding standards and passes all tests before submitting a pull request.

---

# 🧪 Testing

Run unit tests:

```bash
./mvnw test
```

Run all verification checks:

```bash
./mvnw verify
```

Build without tests:

```bash
./mvnw clean install -DskipTests
```

---

# 📚 Documentation

The project includes:

- REST API Documentation (Swagger/OpenAPI)
- Spring Boot Actuator Endpoints
- Docker Configuration
- Deployment Configuration
- Environment Variable Documentation

---

# 📄 License

This project is licensed under the **MIT License**.

See the **LICENSE** file for more details.

---

# 🙌 Acknowledgements

Special thanks to the open-source community and the technologies that made this project possible.

- Spring Boot
- Spring Security
- Next.js
- React
- MySQL
- Redis
- Docker
- Elasticsearch
- Logstash
- Kibana
- Prometheus
- Twilio
- Swagger/OpenAPI

---

# 👨‍💻 Author

**Parveen Kumar**

**Backend Engineer | Full-Stack Developer**

- 🌐 Portfolio: https://your-portfolio-link
- 💼 LinkedIn: https://linkedin.com/in/parveen-kumar-664b8b24b
- 💻 GitHub: https://github.com/Parveen-collab
- 📧 Email: your-email@example.com

---

# ⭐ Support

If you found this project helpful, please consider giving it a ⭐ on GitHub.

Your support helps improve the project and encourages future development.

---

<div align="center">

### 🏗️ NirmaanSetu

**Connecting the Construction Industry Through Technology**

Made with ❤️ using Spring Boot, Next.js, and Java.

</div>
