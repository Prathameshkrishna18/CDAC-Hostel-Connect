package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.service.AdminHostelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hostels")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminHostelController {

    private final AdminHostelService adminHostelService;

    public AdminHostelController(
            AdminHostelService adminHostelService) {

        this.adminHostelService = adminHostelService;
    }


    // =====================================================
    // GET ALL HOSTELS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Hostel>> getAllHostels() {

        return ResponseEntity.ok(
                adminHostelService.getAllHostels()
        );
    }


    // =====================================================
    // GET PENDING HOSTELS
    // =====================================================

    @GetMapping("/pending")
    public ResponseEntity<List<Hostel>> getPendingHostels() {

        return ResponseEntity.ok(
                adminHostelService.getPendingHostels()
        );
    }


    // =====================================================
    // GET APPROVED HOSTELS
    // =====================================================

    @GetMapping("/approved")
    public ResponseEntity<List<Hostel>> getApprovedHostels() {

        return ResponseEntity.ok(
                adminHostelService.getApprovedHostels()
        );
    }


    // =====================================================
    // GET REJECTED HOSTELS
    // =====================================================

    @GetMapping("/rejected")
    public ResponseEntity<List<Hostel>> getRejectedHostels() {

        return ResponseEntity.ok(
                adminHostelService.getRejectedHostels()
        );
    }


    // =====================================================
    // GET HOSTEL DETAILS
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<Hostel> getHostel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminHostelService.getHostel(id)
        );
    }


    // =====================================================
    // APPROVE HOSTEL
    // =====================================================

    @PutMapping("/{id}/approve")
    public ResponseEntity<Hostel> approveHostel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminHostelService.approveHostel(id)
        );
    }


    // =====================================================
    // REJECT HOSTEL
    // =====================================================

    @PutMapping("/{id}/reject")
    public ResponseEntity<Hostel> rejectHostel(
            @PathVariable Long id,
            @RequestParam String reason) {

        return ResponseEntity.ok(
                adminHostelService.rejectHostel(
                        id,
                        reason
                )
        );
    }


    // =====================================================
    // DASHBOARD COUNTS
    // =====================================================

    @GetMapping("/count/pending")
    public ResponseEntity<Long> getPendingCount() {

        return ResponseEntity.ok(
                adminHostelService.getPendingCount()
        );
    }


    @GetMapping("/count/approved")
    public ResponseEntity<Long> getApprovedCount() {

        return ResponseEntity.ok(
                adminHostelService.getApprovedCount()
        );
    }


    @GetMapping("/count/rejected")
    public ResponseEntity<Long> getRejectedCount() {

        return ResponseEntity.ok(
                adminHostelService.getRejectedCount()
        );
    }
}