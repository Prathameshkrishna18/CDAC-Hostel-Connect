package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.HostelRequest;
import com.cdac.hostelconnect.dto.OwnerDashboardResponse;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.service.HostelOwnerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
public class HostelOwnerController {

    private final HostelOwnerService hostelOwnerService;

    public HostelOwnerController(
            HostelOwnerService hostelOwnerService) {

        this.hostelOwnerService = hostelOwnerService;
    }


    // =====================================================
    // DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public ResponseEntity<OwnerDashboardResponse> getDashboard(
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        OwnerDashboardResponse response =
                new OwnerDashboardResponse(

                        hostelOwnerService
                                .getTotalHostels(
                                        ownerEmail
                                ),

                        hostelOwnerService
                                .getApprovedHostels(
                                        ownerEmail
                                ),

                        hostelOwnerService
                                .getPendingHostels(
                                        ownerEmail
                                ),

                        hostelOwnerService
                                .getRejectedHostels(
                                        ownerEmail
                                )
                );

        return ResponseEntity.ok(response);
    }


    // =====================================================
    // CREATE HOSTEL
    // =====================================================

    @PostMapping("/hostels")
    public ResponseEntity<Hostel> createHostel(
            @Valid
            @RequestBody HostelRequest request,
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        Hostel hostel =
                hostelOwnerService.createHostel(
                        request,
                        ownerEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hostel);
    }


    // =====================================================
    // GET MY HOSTELS
    // =====================================================

    @GetMapping("/hostels")
    public ResponseEntity<List<Hostel>> getMyHostels(
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                hostelOwnerService
                        .getMyHostels(ownerEmail)
        );
    }


    // =====================================================
    // GET MY HOSTEL
    // =====================================================

    @GetMapping("/hostels/{id}")
    public ResponseEntity<Hostel> getMyHostel(
            @PathVariable Long id,
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                hostelOwnerService.getMyHostel(
                        id,
                        ownerEmail
                )
        );
    }


    // =====================================================
    // UPDATE HOSTEL
    // =====================================================

    @PutMapping("/hostels/{id}")
    public ResponseEntity<Hostel> updateHostel(
            @PathVariable Long id,
            @Valid
            @RequestBody HostelRequest request,
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        return ResponseEntity.ok(
                hostelOwnerService.updateHostel(
                        id,
                        request,
                        ownerEmail
                )
        );
    }


    // =====================================================
    // DELETE HOSTEL
    // =====================================================

    @DeleteMapping("/hostels/{id}")
    public ResponseEntity<Void> deleteHostel(
            @PathVariable Long id,
            Authentication authentication) {

        String ownerEmail =
                authentication.getName();

        hostelOwnerService.deleteHostel(
                id,
                ownerEmail
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}