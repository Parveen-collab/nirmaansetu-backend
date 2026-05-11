NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

## inspiration
1. Upwork
2. Fiverr

## Users 
## for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)


## Analysis of Project till Phase 1 and MVP

## Run These commands before push
1. .\mvnw clean install -DskipTests
2. .\mvnw test = Run this command to ensure all unit and integration tests pass.
3. .\mvnw clean install = Run the following combined command for a comprehensive check.
4. mvn verify = This will trigger the plugin to download the latest API definitions and update swagger-docs.json in your project root.

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
9. do billing for OpenAI API key
10. check that how much LLM Token will it take in single API hit of AI chatbot feature
10. test all the following AI-features
      1. Conversational Support Bot (LLM Chat)
      2. Smart Enquiry Processing
      3. AI Cost & Material Estimator
      6. OCR for Professional Verification
      7. Sentiment Analysis for Trust & Safety
10. document the NirmaanSetu Backend Development till Phase 1
11. for employees there is keyword search parameter also so what search will work keyword or AI search
12. how do i verify without UI that this AI based search is working
13. what database is being used to store the vector store



I have gathered information about the project's technologies and goals. I will now generate a professional `README.md` for **NirmaanSetu**.

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
*NirmaanSetu - Building Bridges in Construction.*