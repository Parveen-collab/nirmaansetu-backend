package com.nirmaansetu.backend.modules.users.repository;  
  
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;  
import org.springframework.data.jpa.repository.JpaRepository;  
import org.springframework.stereotype.Repository;  
  
@Repository  
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {  
}  
