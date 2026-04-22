package com.nirmaansetu.backend.modules.shop.repository;

import com.nirmaansetu.backend.modules.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
