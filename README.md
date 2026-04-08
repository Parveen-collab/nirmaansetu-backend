NirmaanSetu-A Platform for connecting all aspects related with Construction Sectors.

## inspiration
1. Upwork
2. Fiverr

## Users 
## for phase 1 
   Employees = Mistry, Helper, Engineers, Carpenter and many more), 
   Employers(Any Common man, Contractors, Builders), 
   Shopkeepers/Suppliers(cement, gitti, balu, chhar, paint, water-related, pipe-related and many more)

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
6. create API to get all users, to get all users by role (by employee, by employer, by supplier)
7. Configured CORS properly
8. on successful user registration process there must be username and password to login in the response and it must be sent to the user's verified mobile number.
9. Role Restriction in Registration
      In your UserService.java, you must strictly validate that the SUPER_ADMIN role cannot be requested through the public register API. And even we must not give the option of Super_Admin role to select.
10. "Only Admin Can Create Admin" Rule
       If you want to allow more Super_Admins to be added later:
Create a separate, protected endpoint (e.g., /api/admin/create-admin).
Apply Role-Based Access Control (RBAC) to this endpoint so only someone who already has the SUPER_ADMIN role can access it.
11. create a role named "Super_Admin" with whole app access so that we can create a admin dashboard
12. create an API to restore the deleted data within 24 hours and it must be done by only Super_Admin role



