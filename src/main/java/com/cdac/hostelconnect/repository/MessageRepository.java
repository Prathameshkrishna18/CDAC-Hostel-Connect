package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository
        extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(
            Long conversationId
    );

    List<Message> findByReceiverIdAndReadFalseOrderByCreatedAtDesc(
            Long receiverId
    );
}