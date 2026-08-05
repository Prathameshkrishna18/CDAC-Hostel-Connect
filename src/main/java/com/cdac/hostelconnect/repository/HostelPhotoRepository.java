package com.cdac.hostelconnect.repository;

import com.cdac.hostelconnect.entity.HostelPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostelPhotoRepository
        extends JpaRepository<HostelPhoto, Long> {

    List<HostelPhoto> findByHostelId(Long hostelId);
}