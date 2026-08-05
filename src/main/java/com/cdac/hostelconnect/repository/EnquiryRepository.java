package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Enquiry;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository
        extends JpaRepository<Enquiry, Long> {

    List<Enquiry> findByHostelOwner(User owner);

    List<Enquiry> findByStudent(User student);

    List<Enquiry> findByHostelId(Long hostelId);
}