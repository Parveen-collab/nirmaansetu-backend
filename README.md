NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

## inspiration
1. Upwork
2. Fiverr

## Users 
## for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

## Code Analysis of Project till Auth Module, User Module and Enquiries Module 
`UserService` effectively uses caching, mapping, and strategy patterns, and while `validateProfileImageUrl` has timeouts, its synchronous network request could still impact registration speed. I'll now summarize my findings and provide recommendations.

### **Project Analysis: NirmaanSetu Backend**
The project is a robust, modular Spring Boot application designed to connect various stakeholders in the construction sector. It employs modern Java features and a well-thought-out architecture.

#### **1. Technical Stack**
- **Core**: Java 21, Spring Boot 3.x (Modular Monolith approach).
- **Data**: MySQL (Persistence), Redis (OTP storage, Rate limiting, Caching).
- **Security**: Spring Security with JWT, SHA-256 for OTP hashing, BCrypt for passwords.
- **Integration**: Twilio (SMS), ELK Stack (Logging), Prometheus (Monitoring), MapStruct (DTO Mapping).
- **Patterns**: Strategy & Factory (Profile management), Global Exception Handling, State Machine (Order workflow).

#### **2. Key Strengths**
- **Modular Design**: Domain logic is neatly separated into `modules/` (auth, enquiries, users), promoting maintainability.
- **Infrastructure**: Advanced observability with ELK and Prometheus integration.
- **Clean Code**: Consistent use of DTOs, Mappers, and clear service boundaries.
- **Robust Auth**: Implements OTP with rate limiting, hashing, and verification states.
---

## Concepts used till Auth module, User module and Enquiries module
Based on the analysis of the codebase, here is a list of the key software engineering concepts and patterns implemented:

### **1. Architectural Patterns**
- **Modular Monolith**: The application is organized into distinct domain-specific modules (`auth`, `users`, `enquiries`) to maintain high cohesion and low coupling.
- **Layered Architecture**: Standard separation into **Controller**, **Service**, **Repository**, and **DTO/Entity** layers.

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

## Points to be remembered for code consistency in other modules
To ensure consistency across your new modules, you can use the following **Prompt Template**. It is designed to enforce the architectural patterns, naming conventions, and best practices already established in your `auth`, `users`, and `enquiries` modules.

### **Zencoder/Coding Agent Prompt Template**

> **Task**: Implement the `[Module Name, e.g., Projects]` module.
>
> **Requirements & Consistency Rules**:
> 1. **Module Structure**: Create the module under `com.nirmaansetu.backend.modules.[module_name]` with sub-packages: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, and `task` (if needed).
> 2. **Base Entity**: All entities MUST extend `com.nirmaansetu.backend.shared.utils.BaseEntity` to support soft deletes and common metadata.
> 3. **DTO Pattern**: Use separate `[Entity]RequestDto` and `[Entity]ResponseDto`. Apply `@Valid` and Bean Validation (e.g., `@NotBlank`, `@Size`) for all request inputs.
> 4. **Mapping**: Use **MapStruct** for conversions between Entities and DTOs. Ensure the mapper interface is named `[Entity]Mapper`.
> 5. **Service Layer**:
     >    - Use `@Service` and `@Validated` annotations.
>    - Implement business logic here, keeping controllers thin.
>    - Use `@Transactional` for all write operations.
>    - Follow the existing pattern for exception handling (e.g., `throw new RuntimeException("Specific Error Message")` for the `GlobalExceptionHandler` to catch).
> 6. **Design Patterns**:
     >    - If the logic varies by user role, use the **Strategy Pattern** with a **Factory**, mimicking the `ProfileStrategy` implementation.
>    - Use **Constructor Injection** (or `@Autowired` on fields to match existing style) for dependencies.
> 7. **Caching**: Apply `@Cacheable` and `@CacheEvict` using **Redis** for frequently accessed data (e.g., `getById`).
> 8. **Documentation**: Add **OpenAPI/Swagger** annotations (`@Operation`, `@ApiResponse`) to all controller methods.
> 9. **Scheduled Tasks**: If the module requires data cleanup (e.g., final deletion of soft-deleted records), create a `[Module]CleanupTask` using `@Scheduled`, similar to `UserCleanupTask`.
> 10. **File/Shared Services**: Leverage existing shared services like `FileService` for uploads or `OtpService` for verification if applicable.
>
> **Please analyze the existing `modules/users` and `modules/auth` packages first to ensure the code style and imports match exactly.**

### **How to use this:**
1. **Copy the text above.**
2. **Replace `[Module Name]`** with `Projects`, `Employee`, etc.
3. **Paste it** whenever you start a new feature or module.
This template forces the AI to look at your `BaseEntity`, `MapStruct` mappers, and `Strategy` patterns before writing a single line of new code.

## flow of /api/v1/auth/send-otp
The flow of the `/api/v1/auth/send-otp` endpoint is managed by the `AuthController`, `OtpRequestDto`, `RateLimitingException`,`PhoneNumberValidator`, `ValidPhoneNumber`, `application.properties`, `OtpService` and `SmsService`. Here is the step-by-step process:

### 1. Request Handling
The `AuthController` receives a POST request at `/api/v1/auth/send-otp` with a JSON body containing the `phoneNumber`.

### 2. Rate Limiting Check
The `OtpService` first checks Redis for the key `otp_limit:<phoneNumber>` to prevent abuse.
- If the number of attempts exceeds 5, it throws a `RateLimitException`.
- The rate limit is reset after 10 minutes.

### 3. OTP Generation
A random 4-digit OTP (between 1000 and 9999) is generated.

### 4. Storage in Redis
To verify the OTP later, the service:
- Hashes the OTP using SHA-256 for security.
- Stores the hashed OTP in Redis with the key `otp:<phoneNumber>`.
- The OTP is set to expire in 5 minutes.

### 5. SMS Delivery
The `OtpService` calls the `SmsService` to send the plain-text OTP to the user's phone number.
- Message format: `"Your NirmaanSetu OTP is: {otp}. Valid for 5 minutes."`

### 6. Response
If all steps succeed, the API returns a `200 OK` status with the message `"OTP sent successfully"`.


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

## API list to be created
5. create work/project api
6. get all projects
7. get project by id
8. get all notifications
19. get notification by id
8. get all employees
10. get all employers
11. get employer by id
12. get all shops
13. get shop by id
20. get all summary of payment from the day of registration
21. get all transactions
22. payment api
23. get all orders
24. get order by id
25. Hire api
26. apply work api
27. add material api
28. apply for material api
29. send feedback api

## Important URLs
1. - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
2. - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
3. - Postman: Import `swagger-docs.json` from the root directory.

TO DO LIST
7. learn the use of all files/folders in main folder (21 April)
8. learn the readme file. (21 April)
8. learn the codes of /api/v1/auth/send-otp, (21 April)
7. next work on Project module (23 April)


