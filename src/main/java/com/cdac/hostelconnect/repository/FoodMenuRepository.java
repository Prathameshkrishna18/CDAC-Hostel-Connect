package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodMenuRepository
        extends JpaRepository<FoodMenu, Long> {

    List<FoodMenu> findByHostelId(Long hostelId);
}