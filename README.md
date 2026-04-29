NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

## inspiration
1. Upwork
2. Fiverr

## Users 
## for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)


## Analysis of Project till Auth, User and Enquiries Module 

## Project Analysis: NirmaanSetu Backend
The project is a robust, modular Spring Boot application designed to connect various stakeholders in the construction sector. It employs modern Java features and a well-thought-out architecture.

## 1. Technical Stack
- Core: Java 21, Spring Boot 3.5.10 (Modular Monolith approach).
- Data: MySQL (Persistence), Redis (OTP storage, Rate limiting, Caching).
- Security: Spring Security with JWT, SHA-256 for OTP hashing, Bcrypt for passwords.
- Integration: Twilio (SMS), ELK Stack (Logging), Prometheus (Monitoring), MapStruct (DTO Mapping).
- Patterns: Strategy & Factory (Profile management), Global Exception Handling, State Machine (Order workflow).

## 2. Key Strengths
- Modular Design: Domain logic is neatly separated into `modules/` (auth, enquiries, users), promoting maintainability.
- Infrastructure: Advanced observability with ELK and Prometheus integration.
- Clean Code: Consistent use of DTOs, Mappers, and clear service boundaries.
- Robust Auth: Implements OTP with rate limiting, hashing, and verification states.
---

## Concepts used till Auth, User and Enquiries module
Based on the analysis of the codebase, here is a list of the key software engineering concepts and patterns implemented:

## 1. Architectural Patterns
- Modular Monolith: The application is organized into distinct domain-specific modules (`auth`, `users`, `enquiries`) to maintain high cohesion and low coupling.
- Layered Architecture: Standard separation into Controller, Service, Repository, and DTO/Entity layers.

### **2. Design Patterns**
- **Strategy Pattern**: Used for handling different user profile types (Employee, Employer, Supplier) dynamically.
- **Factory Pattern**: Implemented via `ProfileStrategyFactory` to instantiate the correct strategy based on user roles.
- **Data Mapper Pattern**: Uses **MapStruct** for efficient and type-safe conversion between Entities and DTOs.
- **Singleton Pattern**: Managed by Spring's IoC container for service and configuration beans.

### **3. Security Concepts**
- **Stateless Authentication**: Implemented using **JWT (JSON Web Tokens)**.
- **Role-Based Access Control (RBAC)**: Security configurations manage access based on roles like `ADMIN`, `USER`, and `SUPER_ADMIN`.
- **Cryptographic Hashing**: Uses **BCrypt** for password storage and **SHA-256** for OTP (One-Time Password) hashing in Redis.
- **Rate Limiting**: Implemented in `OtpService` to prevent brute-force attacks on OTP generation.

### **4. Infrastructure & Integration**
- **Event-Driven Workflow**: Uses **Spring Statemachine** to manage complex states (e.g., Order states).
- **Caching**: Employs **Redis** for performance optimization (using `@Cacheable` and `@CacheEvict`).
- **Distributed Logging (ELK)**: Configured for **Elasticsearch**, **Logstash**, and **Kibana** for centralized log management.
- **Observability**: Uses **Spring Boot Actuator**, **Micrometer**, and **Prometheus** for health monitoring and metrics collection.

### **5. Clean Code & Backend Practices**
- **Global Exception Handling**: Centralized error management using `@ControllerAdvice`.
- **Soft Deletes**: Managed through a `BaseEntity` and automated cleanup tasks.
- **Declarative Validation**: Uses **Bean Validation (JSR-303)** for request DTOs.
- **Automated Tasks**: Uses `@Scheduled` for periodic data cleanup (e.g., `UserCleanupTask`).
- **External Service Integration**: Integrated with **Twilio** for SMS notifications and **OpenAPI (Swagger)** for API documentation.

## Run These commands before push
1. .\mvnw clean install -DskipTests
2. .\mvnw test = Run this command to ensure all unit and integration tests pass.
3. .\mvnw clean install = Run the following combined command for a comprehensive check.

## Run the following command to run nirmaansetu docker image
1. docker run -p 8080:8080 parveendockerhub/nirmaansetu:latest
2. Your backend is now globally usable
Your image URL:parveendockerhub/nirmaansetu:latest

## Whenever you update your backend code:
1. Step 1:Rebuild Image = docker build -t nirmaansetu .
2. Step 2:Tag Again = docker tag nirmaansetu parveendockerhub/nirmaansetu:latest
3. Step 3:Push Again = docker push parveendockerhub/nirmaansetu:latest
4. 🔄 Step 4: Restart your container / redeploy
   docker stop <container_id>
   docker rm <container_id>
   docker run -p 8080:8080 parveendockerhub/nirmaansetu:latest
If using Render / Railway:
   👉 It may auto-redeploy OR you click “Deploy latest image”


## API list created
1. **Auth APIs**
   - `/api/v1/auth/login` (POST)
   - `/api/v1/auth/send-otp` (POST)
   - `/api/v1/auth/verify-otp` (POST)
   - `/api/v1/auth/send-otp-forgot` (POST)
   - `/api/v1/auth/reset-password` (POST)
   - `/api/v1/auth/refresh` (POST)
2. **SMS APIs**
   - `/api/sms/send` (POST)
3. **User APIs**
   - `/api/v1/user/register` (POST - multipart)
   - `/api/v1/user/{id}` (GET, PATCH - multipart, DELETE)
   - `/api/v1/user/all` (GET)
4. **Admin APIs**
   - `/api/admin/create-admin` (POST - multipart)
5. **Enquiry APIs**
   - `/api/enquiries` (POST, GET)
6. **Project APIs**
   - `/api/v1/projects` (POST, GET)
   - `/api/v1/projects/{id}` (GET, PATCH, DELETE)
7. **Notification APIs**
   - `/api/v1/notifications` (GET)
   - `/api/v1/notifications/{id}` (GET)
8. **Employee APIs**
   - `/api/v1/employees` (GET)
9. **Employer APIs**
   - `/api/v1/employers` (GET)
   - `/api/v1/employers/{id}` (GET)
10. **Shop APIs**
    - `/api/v1/shops` (POST, GET)
    - `/api/v1/shops/{id}` (GET)
    - `/api/v1/shops/{shopId}/materials` (POST)

## Important URLs
1. - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
2. - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
3. - Postman: Import `swagger-docs.json` from the root directory.

TO DO LIST
9. be able to explain Spring Boot + AI Integration flow verbally, clear in Database schema design and backend feels routine.
      -learn the flow of all APIs 
      -document the NirmaanSetu Backend Development till Phase 1
10.  rather than owning its own), its structure will differ slightly from a standard CRUD module.

To complete it for **Admin/Super_Admin** use, here is what you need:

### **1. Implementation Strategy**
*   **Service Layer ([./src/main/java/com/nirmaansetu/backend/modules/dashboard/service/DashboardService.java](./src/main/java/com/nirmaansetu/backend/modules/dashboard/service/DashboardService.java))**:
    This service should **inject the repositories or services** of other modules (e.g., `UserRepository`, `OrderRepository`, `ProjectRepository`). It will perform `count()` operations or complex aggregations to fill the `DashboardStatsDto`.
*   **Controller Layer ([./src/main/java/com/nirmaansetu/backend/modules/dashboard/controller/DashboardController.java](./src/main/java/com/nirmaansetu/backend/modules/dashboard/controller/DashboardController.java))**:
    Apply strict security annotations here to ensure only authorized roles can access it:
    ```java
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    ```

### **2. What is still "Missing"?**
Even as an aggregator, you should add these to maintain the project's "Backend as Routine" feel:

*   **`mapper` folder**: If you are transforming raw DB counts or projections into the `DashboardStatsDto`, a **MapStruct** mapper is recommended for consistency with the rest of the project.
*   **Specific DTOs**: You currently have [./src/main/java/com/nirmaansetu/backend/modules/dashboard/dto/DashboardStatsDto.java](./src/main/java/com/nirmaansetu/backend/modules/dashboard/dto/DashboardStatsDto.java). You might need more specialized DTOs like `UserGrowthDto` or `RevenueStatsDto` for a complete admin view.

### **3. Key Design Decisions**
*   **Read-Only**: This module usually won't have an `entity` folder because it doesn't "own" any tables; it views others.
*   **Performance**: Since it queries multiple modules, consider using **Spring Cache** or @Scheduled tasks to pre-calculate stats so the dashboard loads instantly for the Admin.

**Recommendation**: Start by implementing a `getGlobalStats()` method in the `DashboardService` that calls `.count()` on your existing repositories.