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

TO DO LIST
9. comprehensive checklist for robust, production-ready development:
### 10. Edge Cases to Test for users module
### 2. Input Validation
### 4. Data Encryption
### 5. Performance Checklist
### 6. Error Handling Checklist
### 7. Logging & Monitoring Checklist
### 8. Scalability Checklist (Global Ready) (universal)
-  Stateless Auth : Use  JWT  (already in `pom.xml`) so the backend can scale horizontally.
-  Read-Write Splitting : Use a read replica for GET requests if traffic increases.
-  CDN : Serve `profileImageUrl` and company photos via a CDN (like CloudFront or Cloudinary).

10. Right now:
Enable Swagger locally

Before deployment:
Add JWT in Swagger
Add API versioning

After deployment:
Disable OR secure Swagger
Share API docs via Postman

11. create a CI/CD pipeline for every git push to check test cases 


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
