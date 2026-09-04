package com.nirmaansetu.backend.config;

import com.nirmaansetu.backend.modules.applications.entity.ProjectApplication;
import com.nirmaansetu.backend.modules.applications.enums.ApplicationStatus;
import com.nirmaansetu.backend.modules.applications.repository.ProjectApplicationRepository;
import com.nirmaansetu.backend.modules.projects.entity.Project;
import com.nirmaansetu.backend.modules.projects.entity.ProjectRole;
import com.nirmaansetu.backend.modules.projects.entity.ProjectStatus;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRepository;
import com.nirmaansetu.backend.modules.projects.repository.ProjectRoleRepository;
import com.nirmaansetu.backend.modules.shop.entity.Material;
import com.nirmaansetu.backend.modules.shop.entity.Shop;
import com.nirmaansetu.backend.modules.shop.repository.MaterialRepository;
import com.nirmaansetu.backend.modules.shop.repository.ShopRepository;
import com.nirmaansetu.backend.modules.users.entity.Address;
import com.nirmaansetu.backend.modules.users.entity.AddressType;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import com.nirmaansetu.backend.modules.users.entity.EmployerProfile;
import com.nirmaansetu.backend.modules.users.entity.RegistrationStatus;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.ShopType;
import com.nirmaansetu.backend.modules.users.entity.SupplierProfile;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.EmployerProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.SupplierProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds minimum demo data required by the frontend.
 *
 * This seeder is idempotent:
 * restarting the application will not create duplicate
 * demo users, shops, materials, projects or applications.
 *
 * Orders are intentionally not seeded for now because
 * OrderItemRepository is not available yet.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final SupplierProfileRepository supplierProfileRepository;

    private final ShopRepository shopRepository;
    private final MaterialRepository materialRepository;

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;

    private final ProjectApplicationRepository projectApplicationRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        log.info("==========================================");
        log.info("Starting database demo data seeding...");
        log.info("==========================================");

        /*
         * ----------------------------------------------------
         * 1. USERS
         * ----------------------------------------------------
         */

        User superAdmin = createUserIfNotExists(
                "+919999999999",
                "Super Admin",
                "superadmin@nirmaansetu.com",
                "000000000001",
                Role.SUPER_ADMIN,
                RegistrationStatus.ACTIVE,
                "Admin@123"
        );

        User admin = createUserIfNotExists(
                "+919999999998",
                "Admin User",
                "admin@nirmaansetu.com",
                "000000000002",
                Role.ADMIN,
                RegistrationStatus.ACTIVE,
                "Admin@123"
        );

        User guest = createUserIfNotExists(
                "+919999999997",
                "Guest User",
                "guest@nirmaansetu.com",
                "000000000003",
                Role.GUEST,
                RegistrationStatus.GUEST,
                "Guest@123"
        );

        /*
         * ----------------------------------------------------
         * 2. EMPLOYEES
         * ----------------------------------------------------
         */

        String[] employeeNames = {
                "Raj Kumar",
                "Amit Sharma",
                "Ravi Verma",
                "Suresh Yadav",
                "Vikash Kumar"
        };

        String[] employeeCategories = {
                "Construction",
                "Construction",
                "Construction",
                "Construction",
                "Construction"
        };

        String[] employeeSpecialities = {
                "Masonry",
                "Electrical Work",
                "Plumbing",
                "Carpentry",
                "Painting"
        };

        int[] employeeExperience = {
                8,
                6,
                7,
                10,
                5
        };

        User[] employees = new User[5];

        for (int i = 0; i < 5; i++) {

            String phone = "+91980000000" + (1 + i);
            String email = "employee" + (i + 1) + "@nirmaansetu.com";
            String aadhaar = "10000000000" + (1 + i);

            employees[i] = createUserIfNotExists(
                    phone,
                    employeeNames[i],
                    email,
                    aadhaar,
                    Role.EMPLOYEE,
                    RegistrationStatus.ACTIVE,
                    "Employee@123"
            );

            ensureEmployeeProfile(
                    employees[i],
                    employeeCategories[i],
                    employeeSpecialities[i],
                    employeeExperience[i]
            );

            ensureAddress(
                    employees[i],
                    "Jharkhand",
                    "Ranchi",
                    "10" + i,
                    "Main Road",
                    "83400" + (10 + i),
                    "Harmu",
                    "Employee Building " + (i + 1),
                    23.3441 + (i * 0.001),
                    85.3096 + (i * 0.001),
                    AddressType.CURRENT
            );
        }

        /*
         * ----------------------------------------------------
         * 3. EMPLOYERS
         * ----------------------------------------------------
         */

        String[] employerNames = {
                "Sharma Construction Pvt Ltd",
                "Ranchi BuildTech",
                "Jharkhand Infrastructure",
                "Apex Construction Works",
                "Harmu Builders"
        };

        User[] employers = new User[5];

        for (int i = 0; i < 5; i++) {

            String phone = "+91980000010" + (1 + i);
            String email = "employer" + (i + 1) + "@nirmaansetu.com";
            String aadhaar = "20000000000" + (1 + i);

            employers[i] = createUserIfNotExists(
                    phone,
                    "Employer " + (i + 1),
                    email,
                    aadhaar,
                    Role.EMPLOYER,
                    RegistrationStatus.ACTIVE,
                    "Employer@123"
            );

            ensureEmployerProfile(
                    employers[i],
                    employerNames[i]
            );

            ensureAddress(
                    employers[i],
                    "Jharkhand",
                    "Ranchi",
                    "20" + i,
                    "Near Main Road",
                    "83400" + (20 + i),
                    "Lalpur",
                    "Employer Building " + (i + 1),
                    23.3500 + (i * 0.001),
                    85.3200 + (i * 0.001),
                    AddressType.CURRENT
            );
        }

        /*
         * ----------------------------------------------------
         * 4. SUPPLIERS
         * ----------------------------------------------------
         */

        String[] shopNames = {
                "Ranchi Building Materials",
                "Harmu Hardware Store",
                "Shree Construction Supplies",
                "Jharkhand Cement & Steel",
                "Apex Building Mart"
        };

        String[] shopCategories = {
                "Building Materials",
                "Hardware",
                "Construction Supplies",
                "Cement and Steel",
                "Building Materials"
        };

        String[] shopSpecialities = {
                "Cement, Sand and Bricks",
                "Tools and Hardware",
                "Construction Materials",
                "Cement, Steel and Rods",
                "General Construction Materials"
        };

        User[] suppliers = new User[5];

        for (int i = 0; i < 5; i++) {

            String phone = "+91980000020" + (1 + i);
            String email = "supplier" + (i + 1) + "@nirmaansetu.com";
            String aadhaar = "30000000000" + (1 + i);

            suppliers[i] = createUserIfNotExists(
                    phone,
                    "Supplier " + (i + 1),
                    email,
                    aadhaar,
                    Role.SUPPLIER,
                    RegistrationStatus.ACTIVE,
                    "Supplier@123"
            );

            ensureSupplierProfile(
                    suppliers[i],
                    shopNames[i],
                    shopCategories[i],
                    shopSpecialities[i]
            );

            ensureAddress(
                    suppliers[i],
                    "Jharkhand",
                    "Ranchi",
                    "30" + i,
                    "Near Main Road",
                    "83400" + (30 + i),
                    "Doranda",
                    "Supplier Building " + (i + 1),
                    23.3200 + (i * 0.001),
                    85.3000 + (i * 0.001),
                    AddressType.CURRENT
            );
        }

        /*
         * ----------------------------------------------------
         * 5. SHOPS
         * ----------------------------------------------------
         */

        Shop[] shops = new Shop[5];

        for (int i = 0; i < 5; i++) {

            shops[i] = createShopIfNotExists(
                    shopNames[i],
                    suppliers[i]
            );
        }

        /*
         * ----------------------------------------------------
         * 6. MATERIALS
         * ----------------------------------------------------
         */

        createMaterialIfNotExists(
                "OPC Cement",
                "High quality Ordinary Portland Cement for construction.",
                420.0,
                "bag",
                shops[0]
        );

        createMaterialIfNotExists(
                "River Sand",
                "Washed construction grade river sand.",
                1800.0,
                "ton",
                shops[0]
        );

        createMaterialIfNotExists(
                "Red Bricks",
                "Standard red clay construction bricks.",
                9.0,
                "piece",
                shops[0]
        );

        createMaterialIfNotExists(
                "Steel Rod 8mm",
                "8mm TMT steel reinforcement rod.",
                65.0,
                "kg",
                shops[1]
        );

        createMaterialIfNotExists(
                "Steel Rod 12mm",
                "12mm TMT steel reinforcement rod.",
                68.0,
                "kg",
                shops[1]
        );

        createMaterialIfNotExists(
                "Binding Wire",
                "Construction grade binding wire.",
                75.0,
                "kg",
                shops[1]
        );

        createMaterialIfNotExists(
                "PVC Pipe",
                "Construction grade PVC plumbing pipe.",
                180.0,
                "piece",
                shops[2]
        );

        createMaterialIfNotExists(
                "PVC Elbow",
                "PVC plumbing elbow fitting.",
                35.0,
                "piece",
                shops[2]
        );

        createMaterialIfNotExists(
                "PVC T Joint",
                "PVC plumbing T joint fitting.",
                45.0,
                "piece",
                shops[2]
        );

        createMaterialIfNotExists(
                "Cement",
                "Premium quality construction cement.",
                430.0,
                "bag",
                shops[3]
        );

        createMaterialIfNotExists(
                "TMT Steel Bar",
                "High strength TMT steel bar.",
                72.0,
                "kg",
                shops[3]
        );

        createMaterialIfNotExists(
                "Concrete Blocks",
                "Standard concrete blocks for construction.",
                55.0,
                "piece",
                shops[4]
        );

        createMaterialIfNotExists(
                "Construction Tiles",
                "Durable construction and flooring tiles.",
                55.0,
                "sqft",
                shops[4]
        );

        createMaterialIfNotExists(
                "Waterproofing Chemical",
                "Waterproofing solution for concrete structures.",
                320.0,
                "litre",
                shops[4]
        );

        /*
         * ----------------------------------------------------
         * 7. PROJECTS
         * ----------------------------------------------------
         */

        Project project1 = createProjectIfNotExists(
                "Residential Building Construction",
                "Construction of a two-storey residential building.",
                "Harmu, Ranchi",
                23.3441,
                85.3096,
                employers[0],
                ProjectStatus.IN_PROGRESS
        );

        Project project2 = createProjectIfNotExists(
                "Commercial Complex Construction",
                "Construction of a commercial shopping complex.",
                "Lalpur, Ranchi",
                23.3616,
                85.3347,
                employers[1],
                ProjectStatus.PLANNING
        );

        Project project3 = createProjectIfNotExists(
                "Road Development Project",
                "Local road construction and development project.",
                "Doranda, Ranchi",
                23.3167,
                85.3188,
                employers[2],
                ProjectStatus.IN_PROGRESS
        );

        /*
         * ----------------------------------------------------
         * 8. PROJECT ROLES
         * ----------------------------------------------------
         */

        ProjectRole project1Mason = ensureProjectRole(
                project1,
                "Mason",
                3
        );

        ProjectRole project1Electrician = ensureProjectRole(
                project1,
                "Electrician",
                2
        );

        ProjectRole project1Plumber = ensureProjectRole(
                project1,
                "Plumber",
                2
        );

        ProjectRole project2Carpenter = ensureProjectRole(
                project2,
                "Carpenter",
                3
        );

        ProjectRole project2Painter = ensureProjectRole(
                project2,
                "Painter",
                4
        );

        ProjectRole project3Mason = ensureProjectRole(
                project3,
                "Mason",
                5
        );

        ProjectRole project3Electrician = ensureProjectRole(
                project3,
                "Electrician",
                2
        );

        /*
         * ----------------------------------------------------
         * 9. PROJECT APPLICATIONS
         * ----------------------------------------------------
         */

        createApplicationIfNotExists(
                employees[0],
                project1Mason,
                ApplicationStatus.PENDING,
                "I have 8 years of masonry experience and would like to work on this project."
        );

        createApplicationIfNotExists(
                employees[1],
                project1Electrician,
                ApplicationStatus.ACCEPTED,
                "I am interested in the electrician position."
        );

        createApplicationIfNotExists(
                employees[2],
                project1Plumber,
                ApplicationStatus.HIRED,
                "I have experience in residential plumbing work."
        );

        createApplicationIfNotExists(
                employees[3],
                project2Carpenter,
                ApplicationStatus.REJECTED,
                "I would like to apply for the carpenter position."
        );

        createApplicationIfNotExists(
                employees[4],
                project2Painter,
                ApplicationStatus.OFFERED,
                "I am available for the painting work."
        );

        createApplicationIfNotExists(
                employees[0],
                project3Mason,
                ApplicationStatus.APPROVED,
                "Available for the road development project."
        );

        /*
         * ----------------------------------------------------
         * 10. ORDERS
         * ----------------------------------------------------
         *
         * Intentionally not seeded.
         *
         * Reason:
         * OrderItemRepository is not available yet.
         *
         * We can add order seeding later without changing
         * the rest of this seeder.
         */

        log.info("==========================================");
        log.info("Database demo data seeding completed.");
        log.info("==========================================");

        log.info("SUPER_ADMIN : +919999999999 / Admin@123");
        log.info("ADMIN       : +919999999998 / Admin@123");
        log.info("GUEST       : +919999999997 / Guest@123");
        log.info("EMPLOYEES   : +919800000001 - +919800000005");
        log.info("EMPLOYERS   : +919800000011 - +919800000015");
        log.info("SUPPLIERS   : +919800000021 - +919800000025");
    }

    /*
     * ========================================================
     * USER
     * ========================================================
     */

    private User createUserIfNotExists(
            String phoneNumber,
            String name,
            String email,
            String aadhaarNumber,
            Role role,
            RegistrationStatus registrationStatus,
            String password
    ) {

        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> {

                    User user = new User();

                    user.setPhoneNumber(phoneNumber);
                    user.setName(name);
                    user.setEmail(email);
                    user.setAadhaarNumber(aadhaarNumber);
                    user.setRole(role);
                    user.setRegistrationStatus(registrationStatus);
                    user.setPassword(passwordEncoder.encode(password));

                    User savedUser = userRepository.save(user);

                    log.info(
                            "Created {} user: {} ({})",
                            role,
                            name,
                            phoneNumber
                    );

                    return savedUser;
                });
    }

    /*
     * ========================================================
     * ADDRESS
     * ========================================================
     */

    private void ensureAddress(
            User user,
            String state,
            String district,
            String wardNumber,
            String landmark,
            String pincode,
            String areaVillage,
            String building,
            Double latitude,
            Double longitude,
            AddressType type
    ) {

        /*
         * Only add an address when the user has no addresses.
         *
         * This avoids duplicate addresses when the application
         * is restarted.
         */
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            return;
        }

        Address address = new Address();

        address.setState(state);
        address.setDistrict(district);
        address.setWardNumber(wardNumber);
        address.setLandmark(landmark);
        address.setPincode(pincode);
        address.setAreaVillage(areaVillage);
        address.setBuilding(building);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setType(type);

        user.addAddress(address);

        userRepository.save(user);
    }

    /*
     * ========================================================
     * EMPLOYEE PROFILE
     * ========================================================
     */

    private void ensureEmployeeProfile(
            User user,
            String serviceCategory,
            String serviceSpeciality,
            Integer experienceYears
    ) {

        boolean exists = employeeProfileRepository
                .findAll()
                .stream()
                .anyMatch(profile ->
                        profile.getUser() != null
                                && profile.getUser().getId().equals(user.getId())
                );

        if (exists) {
            return;
        }

        EmployeeProfile profile = new EmployeeProfile();

        profile.setServiceCategory(serviceCategory);
        profile.setServiceSpeciality(serviceSpeciality);
        profile.setExperienceYears(experienceYears);
        profile.setRating(4.5);
        profile.setAvailable(true);
        profile.setVerified(true);
        profile.setUser(user);

        employeeProfileRepository.save(profile);
    }

    /*
     * ========================================================
     * EMPLOYER PROFILE
     * ========================================================
     */

    private void ensureEmployerProfile(
            User user,
            String companyName
    ) {

        boolean exists = employerProfileRepository
                .findAll()
                .stream()
                .anyMatch(profile ->
                        profile.getUser() != null
                                && profile.getUser().getId().equals(user.getId())
                );

        if (exists) {
            return;
        }

        EmployerProfile profile = new EmployerProfile();

        profile.setCompanyName(companyName);
        profile.setState("Jharkhand");
        profile.setDistrict("Ranchi");
        profile.setWardNumber("12");
        profile.setLandmark("Main Road");
        profile.setPincode("834001");
        profile.setAreaVillage("Ranchi");
        profile.setBuilding("Construction Office");
        profile.setLatitude(23.3441);
        profile.setLongitude(85.3096);
        profile.setUser(user);

        employerProfileRepository.save(profile);
    }

    /*
     * ========================================================
     * SUPPLIER PROFILE
     * ========================================================
     */

    private void ensureSupplierProfile(
            User user,
            String shopName,
            String shopCategory,
            String shopSpeciality
    ) {

        boolean exists = supplierProfileRepository
                .findAll()
                .stream()
                .anyMatch(profile ->
                        profile.getUser() != null
                                && profile.getUser().getId().equals(user.getId())
                );

        if (exists) {
            return;
        }

        SupplierProfile profile = new SupplierProfile();

        profile.setShopName(shopName);
        profile.setShopCategory(shopCategory);
        profile.setShopSpeciality(shopSpeciality);
        profile.setShopType(ShopType.BOTH);
        profile.setState("Jharkhand");
        profile.setDistrict("Ranchi");
        profile.setWardNumber("15");
        profile.setLandmark("Main Road");
        profile.setPincode("834001");
        profile.setAreaVillage("Ranchi");
        profile.setBuilding("Supplier Shop");
        profile.setLatitude(23.3441);
        profile.setLongitude(85.3096);
        profile.setRating(4.4);
        profile.setVerified(true);
        profile.setUser(user);

        supplierProfileRepository.save(profile);
    }

    /*
     * ========================================================
     * SHOP
     * ========================================================
     */

    private Shop createShopIfNotExists(
            String shopName,
            User owner
    ) {

        List<Shop> shops = shopRepository.findAll();

        for (Shop shop : shops) {

            if (shop.getName().equalsIgnoreCase(shopName)) {
                return shop;
            }
        }

        Shop newShop = new Shop();

        newShop.setName(shopName);
        newShop.setAddress(
                "Main Road, Ranchi, Jharkhand, 834001"
        );
        newShop.setContactNumber(
                owner.getPhoneNumber()
        );
        newShop.setOwner(owner);

        Shop savedShop = shopRepository.save(newShop);

        log.info("Created shop: {}", shopName);

        return savedShop;
    }

    /*
     * ========================================================
     * MATERIAL
     * ========================================================
     */

    private Material createMaterialIfNotExists(
            String name,
            String description,
            Double price,
            String unit,
            Shop shop
    ) {

        List<Material> materials = materialRepository.findAll();

        for (Material material : materials) {

            if (material.getName().equalsIgnoreCase(name)) {
                return material;
            }
        }

        Material material = new Material();

        material.setName(name);
        material.setDescription(description);
        material.setPrice(price);
        material.setUnit(unit);
        material.setShop(shop);

        Material savedMaterial = materialRepository.save(material);

        log.info(
                "Created material: {} - {} {}",
                name,
                price,
                unit
        );

        return savedMaterial;
    }

    /*
     * ========================================================
     * PROJECT
     * ========================================================
     */

    private Project createProjectIfNotExists(
            String title,
            String description,
            String locationName,
            Double latitude,
            Double longitude,
            User createdBy,
            ProjectStatus status
    ) {

        List<Project> projects = projectRepository.findAll();

        for (Project project : projects) {

            if (project.getTitle().equalsIgnoreCase(title)) {
                return project;
            }
        }

        Project project = new Project();

        project.setTitle(title);
        project.setDescription(description);
        project.setLocationName(locationName);
        project.setLatitude(latitude);
        project.setLongitude(longitude);
        project.setCreatedBy(createdBy);
        project.setStatus(status);

        Project savedProject = projectRepository.save(project);

        log.info("Created project: {}", title);

        return savedProject;
    }

    /*
     * ========================================================
     * PROJECT ROLE
     * ========================================================
     */

    private ProjectRole ensureProjectRole(
            Project project,
            String roleName,
            Integer totalRequired
    ) {

        List<ProjectRole> roles = projectRoleRepository.findAll();

        for (ProjectRole role : roles) {

            if (role.getProject() != null
                    && role.getProject().getId().equals(project.getId())
                    && role.getRoleName().equalsIgnoreCase(roleName)) {

                return role;
            }
        }

        ProjectRole projectRole = new ProjectRole();

        projectRole.setRoleName(roleName);
        projectRole.setTotalRequired(totalRequired);
        projectRole.setOccupiedCount(0);
        projectRole.setProject(project);

        ProjectRole savedRole = projectRoleRepository.save(projectRole);

        log.info(
                "Created project role: {} for project {}",
                roleName,
                project.getTitle()
        );

        return savedRole;
    }

    /*
     * ========================================================
     * PROJECT APPLICATION
     * ========================================================
     */

    private void createApplicationIfNotExists(
            User user,
            ProjectRole projectRole,
            ApplicationStatus status,
            String coverLetter
    ) {

        if (projectApplicationRepository
                .findByUserAndProjectRole(user, projectRole)
                .isPresent()) {

            return;
        }

        ProjectApplication application = ProjectApplication.builder()
                .user(user)
                .projectRole(projectRole)
                .status(status)
                .coverLetter(coverLetter)
                .isDirectOffer(false)
                .build();

        projectApplicationRepository.save(application);

        log.info(
                "Created application: {} -> {} -> {}",
                user.getName(),
                projectRole.getRoleName(),
                status
        );
    }
}