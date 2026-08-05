package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.StudentHostelRegistration;
import com.cdac.hostelconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentHostelRegistrationRepository
        extends JpaRepository<StudentHostelRegistration, Long> {

    List<StudentHostelRegistration> findByStudent(User student);

    Optional<StudentHostelRegistration>
    findByRazorpayOrderId(String razorpayOrderId);

    boolean existsByStudentIdAndHostelIdAndStatus(
            Long studentId,
            Long hostelId,
            com.cdac.hostelconnect.entity.RegistrationStatus status
    );
}