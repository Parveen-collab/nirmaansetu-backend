package com.nirmaansetu.backend.modules.recommendation.service;

import com.nirmaansetu.backend.modules.recommendation.dto.RecommendationResponseDto;
import com.nirmaansetu.backend.modules.users.entity.Address;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import com.nirmaansetu.backend.modules.users.entity.SupplierProfile;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.SupplierProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing recommendations using vector search.
 * It indexes employee and supplier profiles into a VectorStore and provides
 * search capabilities for recommendations.
 */
@Service
@Slf4j
public class RecommendationService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private SupplierProfileRepository supplierProfileRepository;

    /**
     * Indexes an employee profile into the vector store.
     * Converts profile details into a searchable document format.
     * 
     * @param profile The employee profile to index.
     */
    public void indexEmployeeProfile(EmployeeProfile profile) {
        User user = profile.getUser();
        String addressStr = formatAddresses(user.getAddresses());
        
        // Construct search text containing key profile information
        String content = String.format(
            "Employee Profile: Name: %s, Category: %s, Speciality: %s, Experience: %d years, Rating: %.1f, Available: %s, Location: %s",
            user.getName(),
            profile.getServiceCategory(),
            profile.getServiceSpeciality(),
            profile.getExperienceYears(),
            profile.getRating(),
            profile.isAvailable() ? "Yes" : "No",
            addressStr
        );

        // Create document with metadata for filtering
        Document document = new Document(content, Map.of(
            "type", "EMPLOYEE",
            "profileId", profile.getId(),
            "userId", user.getId(),
            "category", profile.getServiceCategory(),
            "speciality", profile.getServiceSpeciality(),
            "rating", profile.getRating(),
            "isAvailable", profile.isAvailable()
        ));

        vectorStore.add(List.of(document));
        log.info("Indexed employee profile for user: {}", user.getName());
    }

    /**
     * Indexes a supplier profile into the vector store.
     * 
     * @param profile The supplier profile to index.
     */
    public void indexSupplierProfile(SupplierProfile profile) {
        User user = profile.getUser();
        String location = String.format("%s, %s, %s, %s", 
            profile.getAreaVillage(), profile.getDistrict(), profile.getState(), profile.getPincode());
        
        // Construct search text for the supplier
        String content = String.format(
            "Supplier Profile: Shop Name: %s, Category: %s, Speciality: %s, Type: %s, Rating: %.1f, Location: %s",
            profile.getShopName(),
            profile.getShopCategory(),
            profile.getShopSpeciality(),
            profile.getShopType(),
            profile.getRating(),
            location
        );

        // Store with metadata for targeted searches
        Document document = new Document(content, Map.of(
            "type", "SUPPLIER",
            "profileId", profile.getId(),
            "userId", user.getId(),
            "category", profile.getShopCategory(),
            "speciality", profile.getShopSpeciality(),
            "rating", profile.getRating()
        ));

        vectorStore.add(List.of(document));
        log.info("Indexed supplier profile for shop: {}", profile.getShopName());
    }

    /**
     * Recommends employees based on a natural language query.
     * 
     * @param query The search query (e.g., "experienced plumber in Delhi").
     * @param limit Maximum number of recommendations to return.
     * @return List of matching employee recommendations.
     */
    public List<RecommendationResponseDto> recommendEmployees(String query, int limit) {
        SearchRequest searchRequest = SearchRequest.query(query)
            .withTopK(limit)
            .withFilterExpression("type == 'EMPLOYEE'");
        
        return vectorStore.similaritySearch(searchRequest).stream()
            .map(doc -> {
                Long profileId = Long.valueOf(doc.getMetadata().get("profileId").toString());
                EmployeeProfile profile = employeeProfileRepository.findById(profileId).orElse(null);
                if (profile == null) return null;
                
                User user = profile.getUser();
                return RecommendationResponseDto.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .serviceCategory(profile.getServiceCategory())
                    .serviceSpeciality(profile.getServiceSpeciality())
                    .experienceYears(profile.getExperienceYears())
                    .location(formatAddresses(user.getAddresses()))
                    // Calculate similarity score from vector search distance
                    .score(doc.getMetadata().containsKey("distance") ? 1.0 - (Double) doc.getMetadata().get("distance") : 0.0)
                    .build();
            })
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    /**
     * Recommends suppliers based on a natural language query.
     * 
     * @param query The search query.
     * @param limit Maximum number of recommendations to return.
     * @return List of matching supplier recommendations.
     */
    public List<RecommendationResponseDto> recommendSuppliers(String query, int limit) {
        SearchRequest searchRequest = SearchRequest.query(query)
            .withTopK(limit)
            .withFilterExpression("type == 'SUPPLIER'");
        
        return vectorStore.similaritySearch(searchRequest).stream()
            .map(doc -> {
                Long profileId = Long.valueOf(doc.getMetadata().get("profileId").toString());
                SupplierProfile profile = supplierProfileRepository.findById(profileId).orElse(null);
                if (profile == null) return null;
                
                User user = profile.getUser();
                String location = String.format("%s, %s, %s, %s", 
                    profile.getAreaVillage(), profile.getDistrict(), profile.getState(), profile.getPincode());
                
                return RecommendationResponseDto.builder()
                    .userId(user.getId())
                    .name(profile.getShopName())
                    .phoneNumber(user.getPhoneNumber())
                    .serviceCategory(profile.getShopCategory())
                    .serviceSpeciality(profile.getShopSpeciality())
                    .location(location)
                    .score(doc.getMetadata().containsKey("distance") ? 1.0 - (Double) doc.getMetadata().get("distance") : 0.0)
                    .build();
            })
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    /**
     * Formats address entities into a readable string.
     */
    private String formatAddresses(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) return "N/A";
        return addresses.stream()
            .map(a -> String.format("%s, %s, %s, %s", a.getAreaVillage(), a.getDistrict(), a.getState(), a.getPincode()))
            .collect(Collectors.joining(" | "));
    }
    
    /**
     * Clears existing vector data and re-indexes all profiles from the database.
     */
    public void reindexAll() {
        log.info("Reindexing all profiles...");
        List<EmployeeProfile> employees = employeeProfileRepository.findAll();
        employees.forEach(this::indexEmployeeProfile);
        
        List<SupplierProfile> suppliers = supplierProfileRepository.findAll();
        suppliers.forEach(this::indexSupplierProfile);
        log.info("Reindexing completed.");
    }
}
