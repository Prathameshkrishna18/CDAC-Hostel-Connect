package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.ChatMessage;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByStudentAndHostelOrderByCreatedAtAsc(
            User student,
            Hostel hostel
    );

    List<ChatMessage> findByOwnerOrderByCreatedAtDesc(
            User owner
    );

    // Needed for owner conversation list
    List<ChatMessage> findByHostelOrderByCreatedAtDesc(
            Hostel hostel
    );

    // Needed when owner opens a student's chat
    List<ChatMessage> findByStudentAndHostel(
            User student,
            Hostel hostel
    );
}