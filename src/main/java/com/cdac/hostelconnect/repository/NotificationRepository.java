package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Notification;
import com.cdac.hostelconnect.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByUserOrderByCreatedAtDesc(
            User user
    );

    long countByUserAndReadStatus(
            User user,
            boolean readStatus
    );
}