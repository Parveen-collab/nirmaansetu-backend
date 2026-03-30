NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

inspiration
1. Upwork
2. Fiverr

Users 
for phase 1 = Employees(Mistry, Helper, Engineers, Carpenter and many more), Employers(Any Common man, Contractors, Builders), Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

API list created
1. mobile otp verification api (public) = done
/api/auth/send-otp (public)
/api/auth/verify-otp (public)
/api/sms/send (public
3. registration api = /api/user/register (accessible only by mobile-verified users)
4. DELETE /api/user/{id}

API list to be created
2. login api (accessible only by registered/existed users)

4. change/forgot password api 
flow = (click on chnage/forgot/reset=>resetpassword screen=>mobile/email otp verification=>type 5. 5. password=>retype password=>done)

5. create work api
6. send enquiry api
7. send feedback api
8. get all employees
9. get employee by id
10. get all employers
11. get employer by id
12. get all shops
13. get shop by id
14. get all projects
15. get project by id
16. get profile
17. edit profile
18. get all notifications
19. get notification by id
20. get all summary of payment from the day of registration
21. get all transactions
22. payment api
23. get all orders
24. get order by id
25. Hire api
26. apply work api
27. add material api
28. apply for material api

important URLs
1. - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
2. - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
3. - Postman: Import `swagger-docs.json` from the root directory.

TO DO
Recommended Improvements
5. Technical Debt
   Audit Fields: Implement Spring Data JPA Auditing (e.g., @CreatedDate, @LastModifiedDate) to track when user records are created or updated.

6. Role-Specific Logic
   Polymorphism: If role-specific behavior becomes complex, consider using a strategy pattern in the service layer to handle logic different for Employees vs. Employers.
7. Note: The exact nesting depends on how you handle MultipartFile for the live photo and company photos in your UserController.java.


9. Based on the current state of the **Users Module**, here is a comprehensive checklist for robust, production-ready development:
### 10. Edge Cases to Test for users module
- **Duplicate Registration**: Registering with a `phoneNumber` that already exists.
- **Inconsistent Profile**: Providing `employeeProfile` data while the `role` is `EMPLOYER`.
- **Address Limits**: Users with only one address type (missing `CURRENT` or `PERMANENT`) or multiple same-type addresses.
- **Malformed Aadhaar**: Aadhaar numbers that aren't 12 digits or fail checksum.
- **Image Upload Failures**: Providing a `profileImageUrl` that is inaccessible or malformed.
- **Null/Empty Fields**: Sending an empty `name`, `phoneNumber`, or nested profile objects.

### 2. Input Validation (Requires `@Valid` in `UserController`)
- **PhoneNumber**: Use `@Pattern` or `libphonenumber` to ensure valid country codes and length.
- **Aadhaar**: `@Pattern(regexp = "^[2-9]{1}[0-9]{11}$")` for basic structure validation.
- **Role**: Use `@NotNull` and ensure it matches the `Role` enum.
- **Nested Objects**: Use `@Valid` on `employeeProfile`, `employerProfile`, etc., to validate their specific fields (e.g., `experienceYears > 0`).

### 3. Rate Limiting
- **OTP/Registration**: Implement a bucket-per-IP or bucket-per-phone-number limit (e.g., max 3 registration attempts per hour) using **Redis** (already in `pom.xml`) or **Bucket4j**.
- **Global API Limit**: Limit requests to `/api/user/**` to prevent DoS attacks.

### 4. Data Encryption
- **PII Protection**: Encrypt `aadhaarNumber` in the database (using JPA Attribute Converters with AES-256).
- **Communication**: Ensure all API traffic is over **HTTPS** (TLS 1.3).
- **Secrets**: Use **Spring Cloud Config** or Environment Variables for DB credentials and Twilio keys.

### 5. Performance Checklist
- **Database Indexing**: Ensure `phoneNumber` and `aadhaarNumber` are indexed for fast lookup.
- **Caching**: Use **Redis** to cache user profiles that are frequently accessed but rarely changed.
- **Lazy Loading**: Use `FetchType.LAZY` for profile associations in [./src/main/java/com/nirmaansetu/backend/modules/users/entity/User.java](./src/main/java/com/nirmaansetu/backend/modules/users/entity/User.java) to avoid "N+1" query issues.

### 6. Error Handling Checklist
- **Global Exception Handler**: Create a `@ControllerAdvice` to handle `MethodArgumentNotValidException`, `DataIntegrityViolationException`, and custom `UserNotFoundException`.
- **Standardized Response**: Ensure error responses always return a consistent JSON format with an error code and message.

### 7. Logging & Monitoring Checklist
- **Audit Logs**: Log sensitive actions (e.g., "User [ID] updated their Aadhaar").
- **Metrics**: Use **Actuator** and **Prometheus** (in `pom.xml`) to monitor registration success rates and API latency.
- **ELK/Splunk**: Ship logs to a central system for troubleshooting production issues.

### 8. Scalability Checklist (Global Ready)
- **Stateless Auth**: Use **JWT** (already in `pom.xml`) so the backend can scale horizontally.
- **Read-Write Splitting**: Use a read replica for GET requests if traffic increases.
- **CDN**: Serve `profileImageUrl` and company photos via a CDN (like CloudFront or Cloudinary).

### 9. DevOps & Deployment Checklist
- **Docker**: The project has a `Dockerfile`; ensure it uses a multi-stage build to keep the image small.
- **CI/CD**: Set up a pipeline (GitHub Actions/Jenkins) to run tests (`mvn test`) and linting on every push.
- **Health Checks**: Configure Kubernetes Liveness/Readiness probes using `/actuator/health`.
- **Database Migrations**: Use **Flyway** or **Liquibase** instead of `hibernate.ddl-auto=update`.

10. Right now:
Enable Swagger locally
Document all APIs

Before deployment:
Add JWT in Swagger
Add API versioning

After deployment:
Disable OR secure Swagger
Share API docs via Postman


SPRING STATE MACHINE
Do use it for: Any business entity that has more than 3 states and specific rules about how it moves between them (e.g., a "Project" or an "Order").
Future Need (Highly Recommended)
Since NirmaanSetu aims to connect "all aspects of construction industries," you will inevitably encounter complex workflows that require a State Machine. Here’s where it will be essential:

1. Project Lifecycle Management
   A construction project goes through rigid phases:

PROPOSED → PLANNING → BIDDING → IN_PROGRESS → ON_HOLD → COMPLETED.
Rules like "Cannot start IN_PROGRESS unless a bid is ACCEPTED" are best managed by a State Machine to prevent invalid transitions.
2. Procurement & Material Supply
   Handling orders for raw materials (cement, steel, etc.):

ORDERED → IN_TRANSIT → DELIVERED → INSPECTED → PAID.
You need to handle "Cancelled" or "Returned" states at specific points, which Spring State Machine handles elegantly via Events and Transitions.
3. Tendering and Bidding Process
   When contractors bid on projects:

BID_SUBMITTED → UNDER_REVIEW → SHORTLISTED → ACCEPTED / REJECTED.
Automated actions (like notifying the contractor on ACCEPTED) can be triggered as Actions within the state machine.
4. Compliance and Approval Workflows
   Approval of blueprints, safety permits, or environmental clearances:

DRAFT → SUBMITTED → REVISION_REQUIRED → APPROVED.
