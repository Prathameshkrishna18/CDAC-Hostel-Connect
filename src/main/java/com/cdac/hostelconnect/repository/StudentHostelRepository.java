package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentHostelRepository
        extends JpaRepository<Hostel, Long> {

    // Get approved hostels by CDAC Center
    List<Hostel> findByCdacCenterIdAndStatus(
            Long centerId,
            HostelStatus status
    );

    // Search approved hostels by hostel name
    List<Hostel> findByHostelNameContainingIgnoreCaseAndStatus(
            String hostelName,
            HostelStatus status
    );
}