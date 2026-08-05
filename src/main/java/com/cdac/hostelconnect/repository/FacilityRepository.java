package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository
        extends JpaRepository<Facility, Long> {

    Optional<Facility> findByNameIgnoreCase(String name);
}