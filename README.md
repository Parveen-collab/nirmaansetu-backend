NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

inspiration
1. Upwork
2. Fiverr

Users 
for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

API list created
1. mobile otp verification api
/api/auth/send-otp
/api/auth/verify-otp
/api/sms/send
/api/auth/refresh
3. registration api
/api/user/register 
4. DELETE 
/api/user/{id}
5. UPDATE
/api/user/{id}
6. GET
/api/user/{id}

API list to be created
2. login api (accessible only by registered/existed users)

4. change/forgot password api 
flow = (click on chnage/forgot/reset=>resetpassword screen=>mobile/email otp verification=>type 5. 5. password=>retype password=>done)

5. create work api
6. send enquiry api
7. send feedback api
8. get all employees
10. get all employers
11. get employer by id
12. get all shops
13. get shop by id
14. get all projects
15. get project by id
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
11. create a CI/CD pipeline for every git push to check test cases
12. No OTP Link: Currently, the /api/user/register endpoint is open to everyone (permitAll) and does not check if the phone number being registered was actually verified via OTP
13. check why spring boot checks userByMobileNumber when i pass access token
14. soft delete things should get seen by ADMIN
15. how do we restore the soft deleted data within the time
16. create a proper documentation of the API = from the documentation anyone who is reading it must get to know that how backend is working, which fields are optional/required, which API requires Auth and which does not.
17. do a complete all possible postman test of each and every api end points developed till now and verify them in MySQL database.
14. do a complete senior level engineer tests till now of development
15. Right now: (only for me on my own computer)
        Enable Swagger locally
        Before deployment: (for Frontend team and QA testing)
        Add JWT in Swagger
        Add API versioning
        After deployment: (for real world users)
        Disable OR secure Swagger
        Share API docs via Postman
