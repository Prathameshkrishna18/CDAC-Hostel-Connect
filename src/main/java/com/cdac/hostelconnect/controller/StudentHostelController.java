package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.HostelResponse;
import com.cdac.hostelconnect.service.StudentHostelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/hostels")
public class StudentHostelController {

    private final StudentHostelService hostelService;

    public StudentHostelController(
            StudentHostelService hostelService) {

        this.hostelService = hostelService;
    }

    // =====================================================
    // 1. GET HOSTELS BY CDAC CENTER
    // =====================================================

    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<HostelResponse>>
    getHostelsByCenter(
            @PathVariable Long centerId) {

        return ResponseEntity.ok(
                hostelService.getHostelsByCenter(
                        centerId
                )
        );
    }

    // =====================================================
    // 2. SEARCH HOSTELS
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<List<HostelResponse>>
    searchHostels(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                hostelService.searchHostels(
                        keyword
                )
        );
    }

    // =====================================================
    // 3. GET HOSTEL DETAILS
    // =====================================================

    @GetMapping("/{hostelId}")
    public ResponseEntity<HostelResponse>
    getHostelDetails(
            @PathVariable Long hostelId) {

        return ResponseEntity.ok(
                hostelService.getHostelDetails(
                        hostelId
                )
        );
    }
}