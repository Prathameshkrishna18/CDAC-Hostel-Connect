package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.HostelResponse;
import com.cdac.hostelconnect.service.HostelService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
@CrossOrigin(origins = "http://localhost:5173")
public class HostelController {

    private final HostelService hostelService;

    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    // Get all approved hostels
    @GetMapping("/approved")
    public ResponseEntity<List<HostelResponse>> getApprovedHostels() {

        List<HostelResponse> hostels =
                hostelService.getApprovedHostels();

        return ResponseEntity.ok(hostels);
    }

    // Get one approved hostel by ID
    @GetMapping("/approved/{id}")
    public ResponseEntity<HostelResponse> getApprovedHostelById(
            @PathVariable Long id) {

        HostelResponse hostel =
                hostelService.getApprovedHostelById(id);

        return ResponseEntity.ok(hostel);
    }
}