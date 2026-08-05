package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.CdacCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CdacCenterRepository
        extends JpaRepository<CdacCenter, Long> {

    List<CdacCenter> findByActiveTrue();
}