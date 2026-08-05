package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.entity.SharingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHostelId(Long hostelId);

    boolean existsByHostelIdAndSharingType(
            Long hostelId,
            SharingType sharingType
    );
}