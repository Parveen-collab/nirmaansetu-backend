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

❌ Don’t jump directly into complex microservices infra
✅ Design like microservices (HLD)
✅ Implement as modular monolith

🧩 Simple Strategy for You
Step 1: Modular Monolith (NOW)
Step 2: Add Redis + Queue
Step 3: Break into microservices (LATER)

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
