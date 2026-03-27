NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

inspiration
1. Upwork
2. Fiverr

Users 
for phase 1 = Employees(Mistry, Helper, Engineers, Carpenter and many more), Employers(Any Common man, Contractors, Builders), Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

API list to be created
1. mobile otp verification api (public) = done
/api/auth/send-otp (public)
/api/auth/verify-otp (public)
/api/sms/send (public

3. registration api (accessible only by mobile-verified users)

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


Created API List
1. /api/auth/send-otp (public)
2. /api/auth/verify-otp (public)
3. /api/sms/send (public)

TO DO
Recommended Improvements
19. Security Note: To fully protect your routes, you should eventually add spring-boot-starter-security and implement a JWT Filter that checks the Authorization: Bearer <token> header on every request. This filter would use your JwtUtil to validate the token before allowing access to the controller.

Suggested Improvements for users module
1. Service Layer Logic
   Automated Profile Creation: The UserService should automatically instantiate the correct profile entity (e.g., EmployeeProfile) based on the Role provided during registration.
   Transactional Integrity: Wrap registration in @Transactional to ensure that if saving a profile fails, the User record is also rolled back.
2. Validation & Security
   Input Validation: Add Jakarta Validation constraints (e.g., @Size, @NotBlank, @Email) to UserRequestDto to catch bad data early.
   Unique Constraints: Ensure email and aadhaarNumber are marked as @Column(unique = true) in the database.
   Data Encryption: Since the project has an EncryptionConverter, apply it to sensitive fields like aadhaarNumber in the User entity.
3. API & DTO Enhancements
   Mapping Libraries: Use MapStruct to replace manual mapping code in UserService, reducing boilerplate and potential bugs.
   Unified Registration: Update UserRequestDto to include nested objects for Address and Profile data so users can register in a single request.
4. Address Management
   Cascade Persistence: Use CascadeType.ALL in the User -> Address relationship so addresses are saved/updated automatically when the user is saved.
5. Technical Debt
   Soft Deletes: Add a deleted flag or status enum to the User entity to allow soft deletes instead of permanent record removal.
   Audit Fields: Implement Spring Data JPA Auditing (e.g., @CreatedDate, @LastModifiedDate) to track when user records are created or updated.
6. Role-Specific Logic
   Polymorphism: If role-specific behavior becomes complex, consider using a strategy pattern in the service layer to handle logic different for Employees vs. Employers.


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
