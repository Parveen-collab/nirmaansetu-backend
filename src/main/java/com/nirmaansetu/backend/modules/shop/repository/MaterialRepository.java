package com.nirmaansetu.backend.modules.shop.repository;

import com.nirmaansetu.backend.modules.shop.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
