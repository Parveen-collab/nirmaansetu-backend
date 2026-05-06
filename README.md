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
- **Vector Search & Recommendation**: Uses **Spring AI** with **Elasticsearch** as a vector store to match Employers with suitable Employees based on skills and location.
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
9. ### 1. **Conversational Support Bot (LLM Chat)**
Since you already have `spring-ai-openai`, you can implement a **Support Assistant**.
- **What it does**: Helps users (mistry, contractors, shopkeepers) navigate the app, find services, or get basic construction advice.
- **Implementation**: Create a `/api/chat` endpoint using `ChatClient` that uses RAG (Retrieval Augmented Generation) to answer questions based on your platform's documentation or user profiles. = done

### 2. **Smart Enquiry Processing**
Enhance your existing `enquiries` module with AI-driven categorization.
- **What it does**: Automatically extracts the type of work (Plumbing, Electrical, Masonry) and urgency from a user's raw text enquiry.
- **Implementation**: When an enquiry is created, send the text to an LLM to "label" it. This can then automatically route the enquiry to the most relevant employees.

### 3. **AI Cost & Material Estimator**
- **What it does**: An employer describes a project (e.g., *"I want to build a 20ft boundary wall"*), and the AI suggests the required materials (bricks, cement) and estimated labor cost.
- **Implementation**: Use an LLM with a specific prompt that references material data from your `shop` module to provide a rough quotation.

### 4. **Voice-to-Task (Multilingual)**
Many users in the construction sector prefer speaking over typing.
- **What it does**: Allows users to record a voice message to create an enquiry or search for a product.
- **Implementation**: Integrate **OpenAI Whisper** via Spring AI to convert audio to text, then use your existing vector search to find matches.

### 5. **Advanced "Best Match" Ranking**
Upgrade your current `recommendation` service.
- **What it does**: Instead of just showing similar profiles, use an LLM to rank the top 5 results from your vector store based on specific project needs, ratings, and availability.
- **Implementation**: Use the "Re-ranking" pattern: fetch candidates from Elasticsearch, then let the LLM pick the best one with a justification.

### 6. **OCR for Professional Verification**
- **What it does**: Automatically verifies the identity or certifications of Employees/Suppliers.
- **Implementation**: Use AI to extract text from images of ID cards or licenses during profile setup to ensure the data matches their registration.

### 7. **Sentiment Analysis for Trust & Safety**
- **What it does**: Scans reviews and feedback to detect negative behavior or fraud.
- **Implementation**: Automatically flag reviews with high negative sentiment for admin review to maintain the platform's quality.

**Recommended Next Step**:
Start with **Smart Enquiry Processing** or the **Support Bot**, as they leverage your existing OpenAI integration with minimal architectural changes.
10. be able to explain Spring Boot + AI Integration flow verbally, clear in Database schema design and backend feels routine.
      -learn the flow of all APIs 
      -document the NirmaanSetu Backend Development till Phase 1
10. Analyse the implementation of AI-based recommendation system for most suitable employee and supplier
11. for employees there is keyword search parameter also so what search will work keyword or AI search
12. how do i verify without UI that this AI based search is working
13. what database is being used to store the vector store


The workflow for the `/api/recommendations/employees` endpoint involves an **AI-driven similarity search** using a vector database. Here is the step-by-step process:

1.  **Request Reception**: The RecommendationController.java receives a `GET` request with a `query` (e.g., "experienced plumber") and a `limit`.
2.  **Vector Search**: The RecommendationService.java creates a `SearchRequest` that:
    *   Uses the input query to perform a semantic search.
    *   Applies a filter `type == 'EMPLOYEE'` to target only employee documents.
    *   Requests the top `K` results based on the `limit`.
3.  **Similarity Matching**: The `VectorStore` compares the query's vector embedding against stored employee profile embeddings (which contain details like category, speciality, and experience) and returns the most relevant documents.
4.  **Database Enrichment**: For each matched document:
    *   The service extracts the `profileId` from the document metadata.
    *   It fetches the full `EmployeeProfile` and associated `User` details from the relational database (PostgreSQL) using the `EmployeeProfileRepository`.
5.  **Score Calculation**: A similarity score is calculated based on the vector distance (typically `1.0 - distance`).
6.  **Response Construction**: The data is mapped into a `RecommendationResponseDto` and returned as a list of recommended employees sorted by relevance.


The workflow for the `/api/recommendations/suppliers` endpoint follows a similar **semantic search** pattern, tailored for material and tool providers:

1.  **Request Reception**: The RecommendationController.java handles the `GET` request, accepting a `query` (e.g., "cement supplier near Delhi") and a `limit`.
2.  **Vector Filtering**: The RecommendationService.java executes a `similaritySearch` on the `VectorStore` with a specific filter: `type == 'SUPPLIER'`.
3.  **Supplier Enrichment**: For each matched document, the service:
    *   Retrieves the `profileId` from metadata.
    *   Fetches the `SupplierProfile` and its associated `User` from the database.
    *   Extracts supplier-specific fields like `shopName`, `shopCategory`, and `shopType`.
4.  **Location Formatting**: It constructs a location string using the supplier's specific address fields (`areaVillage`, `district`, `state`, `pincode`).
5.  **Relevance Scoring**: It calculates a relevance score (inverse of vector distance) to show how well the supplier matches the search query.
6.  **Response Delivery**: A list of `RecommendationResponseDto` objects is returned, containing shop details and contact information.


The `/api/recommendations/reindex` API is an administrative endpoint used to synchronize the relational database with the AI vector store. Here is the workflow:

1.  **Administrative Trigger**: An admin sends a `POST` request to `/api/recommendations/reindex`.
2.  **Full Data Fetch**: The `RecommendationService` fetches every record from both the `EmployeeProfile` and `SupplierProfile` tables in the database.
3.  **Content Transformation**: For each profile, the service generates a "searchable" text block:
    *   **Employees**: Includes name, category, speciality, experience, rating, availability, and address.
    *   **Suppliers**: Includes shop name, category, speciality, type, rating, and location.
4.  **Metadata Tagging**: It attaches metadata (like `profileId`, `userId`, and `type`) to the text block so the search results can be linked back to database records.
5.  **Vector Embedding & Storage**:
    *   The service wraps the text and metadata into `Document` objects.
    *   It sends these to the `VectorStore`, which generates numerical embeddings (vectors) for the text and stores them in the vector database.
6.  **Synchronization**: This process ensures that any changes made directly to the database (or records created before the vector store was active) are now searchable by the AI recommendation engine.
