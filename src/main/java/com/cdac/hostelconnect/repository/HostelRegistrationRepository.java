package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.HostelRegistration;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostelRegistrationRepository
        extends JpaRepository<HostelRegistration, Long> {

    List<HostelRegistration>
    findByHostelOwner(User owner);

    List<HostelRegistration>
    findByStudent(User student);

    long countByHostelOwner(User owner);

    long countByHostelOwnerAndStatus(
            User owner,
            com.cdac.hostelconnect.entity.RegistrationStatus status
    );
}