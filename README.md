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
6. after the completion of user module understand the concepts applied till here, next work on Project module


