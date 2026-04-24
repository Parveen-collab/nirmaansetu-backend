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
8. test the all created APIs and fix the issues
9. analyse the logic of shop and supplier
10. shop can be created by only user with supplier role
11. notification should get sent after project creation
12. how to create Admin to get all enquiries
13. how to create super admin to manage the main dashboard
14. analyse the working of notification API
15. what is the APi end point to create orders
16. understand the working of payment API
### **1. Critical Runtime/Test Errors**
- **H2 Syntax Error in Tests**: The test suite encountered an `org.h2.jdbc.JdbcSQLSyntaxErrorException` because it tried to drop a foreign key on the `SUPPLIER_PROFILES` table before the table was created. This indicates an issue with the JPA schema generation order or Hibernate's interaction with the H2 database in the `test` profile.

### **2. Compiler Warnings (Potential Logic Errors)**
- **Lombok `@Builder` Initialization Issues**:
   - **`Notification.java`**: The field `isRead` is initialized to `false`, but Lombok's `@Builder` will ignore this default value. You should use `@Builder.Default`.
   - **`ProjectApplication.java`**: The field `status` is initialized to `ApplicationStatus.PENDING`, which is also ignored by `@Builder`.

### **3. Deprecation and Configuration Warnings**
- **Spring Security Deprecations**:`AntPathRequestMatcher.antMatcher()` is flagged as deprecated. The modern approach is to use `requestMatchers("/path")` directly within the `authorizeHttpRequests` block.
- **Hibernate `@Where` Deprecation**: the `@Where` annotation used for soft deletes is deprecated in Hibernate 6.3+ (standard in Spring Boot 3.x). It should be replaced with `@SoftDelete`.
- **Global AuthenticationManager Warning**: Spring Security warns that configuring a `Global AuthenticationManager` with an `AuthenticationProvider` bean prevents it from automatically using `UserDetailsService` beans.

### **4. Infrastructure Warnings**
- **Spring Data Redis Store Assignment**: Multiple warnings indicate that Spring Data Redis cannot safely identify store assignments for several JPA repositories (e.g., `ProjectApplicationRepository`, `EnquiryRepository`). This happens because both JPA and Redis modules are on the classpath, and the repositories aren't explicitly restricted to JPA.
- **Open-In-View Warning**: `spring.jpa.open-in-view` is enabled by default, which can lead to performance issues and "n+1" query problems if not managed carefully.


