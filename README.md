NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

Users 
for phase 1 = Employees(Mistry, Helper, Engineers, Carpenter and many more), Employers(Any Common man, Contractors, Builders), Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

API List
1. /api/auth/send-otp
2. /api/auth/verify-otp
3. /api/sms/send

TO DO
1.Create a High Level Design = done
2.Create a System Design for the Backend of Nirmaansetu
3.Create a Schema for the Database
4.Create a Low Level Design
5.Create a UML Diagram
6.Think it as a Global Level Scalable Product = done

7.❌ Don’t jump directly into complex microservices infra
✅ Design like microservices (HLD)
✅ Implement as modular monolith

8.🧩 Simple Strategy for You
Step 1: Modular Monolith (NOW)
Step 2: Add Redis + Queue
Step 3: Break into microservices (LATER)

9.You'll likely use
HTTP for:
Get contractors
Post projects
Login/auth

WebSocket for:
Live chat between users
Real-time project updates
Notifications (new bids, approvals)

10. generate JWT token after verifying otp
11. understand the concepts of refresh token 
Current Issues
12. Database Overload: Storing short-lived OTPs in a persistent database (SQL/NoSQL) adds unnecessary overhead and requires manual cleanup of expired records.
13. No Session Management: Successful verification returns a string but no JWT or session token, meaning the user isn't actually "logged in" for subsequent requests.
14. Incomplete Rate Limiting: You increment the counter in Redis, but your code doesn't appear to block the request if the limit is exceeded.

Recommended Improvements
15. Store OTP in Redis with TTL: Move the OTP storage from the database to Redis. Use the phoneNumber as the key and set an expiration (e.g., 5 minutes). Redis will automatically delete it when it expires.
16. Generate JWT on Success: Modify verifyOtp to return a JWT token if the OTP is correct. This allows the frontend to authenticate future API calls.
17. Enforce Rate Limiting: Check the Redis counter before sending the SMS. If it exceeds a threshold (e.g., 3 attempts per minute), return a 429 Too Many Requests error.
18. Hash OTPs: For better security, store the hash of the OTP instead of the plain text, even in Redis.

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
