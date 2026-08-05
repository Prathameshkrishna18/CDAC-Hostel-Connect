package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByStudentIdAndHostelId(
            Long studentId,
            Long hostelId
    );
}