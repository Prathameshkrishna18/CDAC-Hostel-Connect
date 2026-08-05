package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.repository.HostelRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminHostelService {

    private final HostelRepository hostelRepository;

    public AdminHostelService(
            HostelRepository hostelRepository) {

        this.hostelRepository = hostelRepository;
    }

    // =====================================================
    // 1. GET ALL PENDING HOSTELS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Hostel> getPendingHostels() {

        return hostelRepository.findByStatus(
                HostelStatus.PENDING
        );
    }

    // =====================================================
    // 2. GET ALL HOSTELS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Hostel> getAllHostels() {

        return hostelRepository.findAll();
    }

    // =====================================================
    // 3. GET APPROVED HOSTELS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Hostel> getApprovedHostels() {

        return hostelRepository.findByStatus(
                HostelStatus.APPROVED
        );
    }

    // =====================================================
    // 4. GET REJECTED HOSTELS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Hostel> getRejectedHostels() {

        return hostelRepository.findByStatus(
                HostelStatus.REJECTED
        );
    }

    // =====================================================
    // 5. GET HOSTEL DETAILS
    // =====================================================

    @Transactional(readOnly = true)
    public Hostel getHostel(Long hostelId) {

        return hostelRepository.findById(hostelId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hostel not found"
                        ));
    }

    // =====================================================
    // 6. APPROVE HOSTEL
    // =====================================================

    @Transactional
    public Hostel approveHostel(Long hostelId) {

        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                ));

        // Only PENDING hostel can be approved
        if (hostel.getStatus() != HostelStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending hostels can be approved"
            );
        }

        hostel.setStatus(
                HostelStatus.APPROVED
        );

        // Remove old rejection reason
        hostel.setRejectionReason(null);

        return hostelRepository.save(hostel);
    }

    // =====================================================
    // 7. REJECT HOSTEL
    // =====================================================

    @Transactional
    public Hostel rejectHostel(
            Long hostelId,
            String reason) {

        if (reason == null ||
                reason.trim().isEmpty()) {

            throw new RuntimeException(
                    "Rejection reason is required"
            );
        }

        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                ));

        // Only PENDING hostel can be rejected
        if (hostel.getStatus() != HostelStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending hostels can be rejected"
            );
        }

        hostel.setStatus(
                HostelStatus.REJECTED
        );

        hostel.setRejectionReason(
                reason.trim()
        );

        return hostelRepository.save(hostel);
    }

    // =====================================================
    // 8. DASHBOARD COUNTS
    // =====================================================

    @Transactional(readOnly = true)
    public long getPendingCount() {

        return hostelRepository.countByStatus(
                HostelStatus.PENDING
        );
    }

    @Transactional(readOnly = true)
    public long getApprovedCount() {

        return hostelRepository.countByStatus(
                HostelStatus.APPROVED
        );
    }

    @Transactional(readOnly = true)
    public long getRejectedCount() {

        return hostelRepository.countByStatus(
                HostelStatus.REJECTED
        );
    }
}