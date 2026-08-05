package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.HostelResponse;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.repository.StudentHostelRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentHostelService {

    private final StudentHostelRepository hostelRepository;
    private final HostelMapper hostelMapper;

    public StudentHostelService(
            StudentHostelRepository hostelRepository,
            HostelMapper hostelMapper) {

        this.hostelRepository = hostelRepository;
        this.hostelMapper = hostelMapper;
    }

    // =====================================================
    // 1. GET APPROVED HOSTELS BY CDAC CENTER
    // =====================================================

    public List<HostelResponse> getHostelsByCenter(
            Long centerId) {

        List<Hostel> hostels =
                hostelRepository
                        .findByCdacCenterIdAndStatus(
                                centerId,
                                HostelStatus.APPROVED
                        );

        return hostels.stream()
                .map(hostelMapper::toResponse)
                .toList();
    }

    // =====================================================
    // 2. GET APPROVED HOSTEL DETAILS
    // =====================================================

    public HostelResponse getHostelDetails(
            Long hostelId) {

        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                ));

        if (hostel.getStatus()
                != HostelStatus.APPROVED) {

            throw new RuntimeException(
                    "This hostel is not available"
            );
        }

        return hostelMapper.toResponse(hostel);
    }

    // =====================================================
    // 3. SEARCH APPROVED HOSTELS
    // =====================================================

    public List<HostelResponse> searchHostels(
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            throw new RuntimeException(
                    "Search keyword cannot be empty"
            );
        }

        List<Hostel> hostels =
                hostelRepository
                        .findByHostelNameContainingIgnoreCaseAndStatus(
                                keyword.trim(),
                                HostelStatus.APPROVED
                        );

        return hostels.stream()
                .map(hostelMapper::toResponse)
                .toList();
    }
}