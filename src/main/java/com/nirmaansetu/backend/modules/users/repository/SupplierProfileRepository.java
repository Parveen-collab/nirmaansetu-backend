package com.nirmaansetu.backend.modules.users.repository;  
  
import com.nirmaansetu.backend.modules.users.entity.SupplierProfile;  
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.stereotype.Repository;  
  
@Repository  
public interface SupplierProfileRepository extends JpaRepository<SupplierProfile, Long> {  
} 
