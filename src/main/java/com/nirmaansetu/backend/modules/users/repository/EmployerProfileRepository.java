package com.nirmaansetu.backend.modules.users.repository;  
  
import com.nirmaansetu.backend.modules.users.entity.EmployerProfile;  
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.stereotype.Repository;  
  
@Repository  
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {  
}  
