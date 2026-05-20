

Problem Statement = done
Features = done
Tech Stack = done
Architecture
Screenshots
Live Demo
Installation = done
Performance
Scalability
Security
Future Improvements

## Problem Statement
The construction sector faces major challenges due to the lack of a unified digital platform connecting workers, contractors, suppliers, and customers. Communication gaps, unverified labor availability, inefficient material procurement, delayed project coordination, and limited transparency create difficulties for all stakeholders involved in construction projects.

Skilled workers such as masons, carpenters, plumbers, electricians, and helpers often struggle to find consistent employment opportunities, while builders, contractors, and homeowners face challenges in finding reliable and skilled labor on time. Similarly, suppliers and shopkeepers dealing with construction materials like cement, bricks, sand, steel, paint, and hardware have limited digital reach and inefficient methods for managing customer connections and deliveries.

Existing solutions are fragmented and do not provide an integrated ecosystem that combines workforce hiring, supplier management, service discovery, communication, and project coordination in one place. This results in increased project delays, higher operational costs, reduced productivity, and lack of trust among stakeholders.

To address these issues, **NirmaanSetu** aims to develop a centralized digital platform that connects all aspects of the construction sector by enabling seamless interaction between employees, employers, suppliers, and customers. The platform will simplify hiring, improve accessibility to construction materials and services, enhance transparency, and create a more efficient and organized construction ecosystem.


## inspiration
1. Upwork
2. Fiverr

## Users 
## for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)


# NirmaanSetu - Backend

**NirmaanSetu** is a comprehensive platform designed to bridge the gap between various stakeholders in the construction sector. It connects employees (skilled labor like engineers, masons, and carpenters), employers (builders, contractors, and individual homeowners), and suppliers (shopkeepers providing construction materials).

## 🚀 Key Features
- **Multi-Stakeholder Ecosystem**: Integrated platform for Employees, Employers, and Suppliers.
- **AI-Powered Search**: Advanced vector search capabilities using Spring AI and Elasticsearch for matching skills with requirements.
- **Secure Authentication**: Robust security implementation using JWT (JSON Web Tokens) and Twilio for OTP-based verification.
- **ELK Stack Integration**: Centralized logging and monitoring using Elasticsearch, Logstash, and Kibana.
- **Real-time Data Management**: Optimized performance with Redis caching.
- **Automated Cleanup**: Scheduled tasks for managing soft-deleted records and data retention.

## 🛠 Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **AI/ML**: Spring AI (OpenAI Embeddings)
- **Database**: MySQL (Persistence), Redis (Caching)
- **Search Engine**: Elasticsearch (Vector Store)
- **Monitoring**: ELK Stack (Elasticsearch, Logstash, Kibana), Prometheus, Actuator
- **Communication**: Twilio SMS API
- **Documentation**: Swagger/OpenAPI 3.0
- **Containerization**: Docker & Docker Compose

## 📋 Prerequisites
- JDK 17 or higher
- Maven 3.6+
- Docker & Docker Compose
- OpenAI API Key (for AI features)
- Twilio Account (for SMS features)

## Architecture
For the **Architecture** section in your README, you should explain how the system is structured, what technologies are used, and how different parts of the platform interact with each other.

Since your project is a large-scale construction ecosystem platform, your architecture section can include:

* Frontend
* Backend
* Database
* Authentication
* APIs
* User Roles
* Deployment/Scalability
* Future AI integrations (optional)

You can write something like this:

---

# Architecture
NirmaanSetu follows a modern full-stack web architecture designed to support scalability, modularity, and real-time interaction between stakeholders in the construction sector.

### Frontend
The frontend is built using:
* Next.js
* React.js
* TypeScript
* Tailwind CSS
* Material UI (MUI)

The frontend provides responsive and user-friendly interfaces for:
* Employees
* Employers
* Suppliers
* Admins

### Backend
The backend is responsible for:
* Authentication & Authorization
* Job Management
* Supplier Listings
* User Management
* Notifications
* API Handling

Technologies used:
* Node.js
* Express.js / Next.js API Routes
* REST APIs

### Database
The platform uses a relational database for storing:
* User Information
* Job Listings
* Material Listings
* Orders
* Reviews & Ratings
* Project Data

Database & ORM:
* MySQL / PostgreSQL
* Prisma ORM

### Authentication
Authentication system includes:
* Secure Login & Registration
* Role-Based Access Control (RBAC)
* JWT / Session-based Authentication

### User Roles
The platform supports multiple user roles:
1. Employee
2. Employer
3. Supplier
4. Admin
Each role has separate dashboards and permissions.

### Platform Workflow
1. Employers post construction-related work.
2. Employees apply for available jobs.
3. Suppliers list construction materials and services.
4. Customers can search and connect with workers or suppliers.
5. Admin manages platform activities and verification.

### Scalability & Future Scope
The architecture is designed to support:
* AI-based recommendations
* Real-time communication
* Geo-location services
* Multi-language support
* Cloud deployment
* Large-scale concurrent users
---
![img.png](img.png)

## Screenshots
## Live Demo

## ⚙️ Installation & Setup
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Environment Variables**:
   Create a `.env` file or set the following environment variables:
   ```env
   TWILIO_ACCOUNT_SID=your_sid
   TWILIO_AUTH_TOKEN=your_token
   TWILIO_PHONE_NUMBER=your_number
   OPENAI_API_KEY=your_openai_key
   ENCRYPTION_SECRET=your_encryption_secret
   JWT_SECRET=your_jwt_secret
   ```

3. **Spin up Infrastructure**:
   Use Docker Compose to start the ELK stack and other services:
   ```bash
   docker-compose up -d
   ```

4. **Build and Run**:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## 🧪 Testing & Verification
Run the following commands to ensure code quality:
- **Build without tests**: `.\mvnw clean install -DskipTests`
- **Run Unit Tests**: `.\mvnw test`
- **Full Verification**: `mvn verify` (This also updates `swagger-docs.json`)

## 📖 API Documentation
Once the application is running, you can access the Swagger UI at:
`http://localhost:8080/swagger-ui.html` (or the configured port)

## 🏗 Project Structure
- `src/main/java`: Backend source code.
- `src/main/resources`: Configuration files (`application.properties`, logback config).
- `logstash/`: Logstash pipeline configurations.
- `.zencoder/`: Project-specific AI rules and documentation.
- `docker-compose.yml`: Infrastructure orchestration.
---

## Performance
## Scalability
## Security
Future Improvements
NirmaanSetu - Building Bridges in Construction.