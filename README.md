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


## Important URLs
1. - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
2. - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
3. - Postman: Import `swagger-docs.json` from the root directory.

TO DO LIST
9. be able to explain Spring Boot + AI Integration flow verbally, clear in Database schema design and backend feels routine.
      -learn the flow of all APIs 
      -document the NirmaanSetu Backend Development till Phase 1
10. Based on my analysis of your **NirmaanSetu** project, here are several high-impact areas where you can implement AI to enhance the platform:

### 1. **AI-Powered Matching Engine (Recommendation System)**
Since your platform connects **Employees**, **Employers**, and **Suppliers**, a recommendation system would be highly valuable.
- **Implementation**: Use a recommendation algorithm to match Employers with the most suitable Employees (Mistry, Carpenter, etc.) based on their skills, location, availability, and historical performance ratings.
- **Tech**: Spring AI with vector database (like PGVector or Milvus) to store and query worker profiles.

### 2. **Smart Enquiry Analysis & Routing**
Your `Enquiry` module currently handles raw requests. AI can make this "smart."
- **Implementation**: When an Employer sends an enquiry, use an LLM (Large Language Model) to categorize the enquiry, extract required materials, and estimate the labor force needed.
- **Benefit**: It can automatically suggest the right **Suppliers** for the materials mentioned in the enquiry text.

### 3. **Natural Language Search (Semantic Search)**
Instead of basic keyword filtering (like the `keyword` parameter in `/api/v1/user/all`), implement semantic search.
- **Example**: A user searches for "someone who can build a wooden staircase near Delhi." Basic search might fail if "staircase" isn't a tag, but semantic search understands the intent and finds **Carpenters**.

### 4. **AI Chatbot for Construction Guidance**
Many users (like "Common Man") might not know exactly what they need for a construction task.
- **Implementation**: A RAG (Retrieval-Augmented Generation) based chatbot that answers technical construction questions, helps calculate material costs, and guides users through the `Order` workflow.

### 5. **Automated Verification & Document OCR**
Since users upload photos and potentially documents (profile photos, IDs):
- **Implementation**: Use AI to automatically verify identity documents (Aadhaar, PAN) or check if the uploaded profile photo is professional and appropriate.
- **Tech**: AWS Textract or Google Vision API integrated into your Spring Boot backend.

### 6. **Predictive Pricing for Materials**
For your **Suppliers/Shopkeepers**:
- **Implementation**: Use a regression model to predict price trends for materials like Cement, Steel, or Paint based on historical data. This helps suppliers optimize their pricing strategy.

### **Next Step Recommendation:**
I suggest starting with **AI-Powered Matching** or **Semantic Search** using **Spring AI**. This integrates cleanly with your existing modular monolith architecture and provides immediate value to your users.
