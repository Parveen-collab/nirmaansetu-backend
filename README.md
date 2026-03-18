NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

API List
1. /api/auth/send-otp = done, ____, done, done, ____
2. /api/auth/verify-otp = done,____, done, done, ____
3. /api/sms/send = done, ____, done, done, ____

API Testing
1️⃣ Unit Testing (Controller/Service Level) = Individual methods in isolation.
2️⃣ Integration Testing = Controller + Service + Repository + Database together.
3️⃣ Functional Testing (Manual API Testing) = Does API behave correctly for different inputs?
4️⃣ Security Testing = SQL Injection, JWT token tampering, Brute force attack,Rate limiting, Unauthorized access.
5️⃣ Performance Testing 🚀 = What happens with 10,000 requests?
6️⃣ Contract Testing (Frontend ↔ Backend)

TO DO
1.Create a System Design for the Frontend and Backend of Nirmaansetu
2.Create a UML Diagram
3.Create a High Level Design
4.Create a Low Level Design
5.Create a Schema for the Database
6.Think it as a Global Level Scalable Product = done

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
