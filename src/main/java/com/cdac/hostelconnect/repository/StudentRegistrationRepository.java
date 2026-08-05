package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.RegistrationStatus;
import com.cdac.hostelconnect.entity.StudentRegistration;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRegistrationRepository
        extends JpaRepository<StudentRegistration, Long> {

    boolean existsByStudentAndHostelAndStatus(
            User student,
            Hostel hostel,
            RegistrationStatus status
    );

    Optional<StudentRegistration> findByRazorpayOrderId(
            String razorpayOrderId
    );

    Optional<StudentRegistration> findByStudent(
            User student
    );

    Optional<StudentRegistration>
    findTopByStudentOrderByCreatedAtDesc(
            User student
    );
}