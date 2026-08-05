package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Hostel;

import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostelRepository
        extends JpaRepository<Hostel, Long> {

    List<Hostel> findByOwner(User owner);

    List<Hostel> findByStatus(HostelStatus status);

    List<Hostel> findByCdacCenterIdAndStatus(
            Long centerId,
            HostelStatus status
    );
    
    List<Hostel> findByOwnerAndStatus(
            User owner,
            HostelStatus status
    );

    long countByStatus(HostelStatus status);

    long countByOwner(User owner);

    long countByOwnerAndStatus(
            User owner,
            HostelStatus status
    );
}