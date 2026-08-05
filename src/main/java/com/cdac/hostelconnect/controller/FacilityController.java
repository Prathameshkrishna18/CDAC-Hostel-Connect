package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.entity.Facility;
import com.cdac.hostelconnect.service.FacilityService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/hostels")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(
            FacilityService facilityService) {

        this.facilityService = facilityService;
    }


    // =====================================================
    // ADD FACILITY TO HOSTEL
    // =====================================================

    @PostMapping("/{hostelId}/facilities")
    public ResponseEntity<String> addFacility(
            @PathVariable Long hostelId,
            @RequestParam String name,
            Authentication authentication) {

        facilityService.addFacilityToHostel(
                hostelId,
                name,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Facility added successfully"
        );
    }


    // =====================================================
    // GET ALL AVAILABLE FACILITIES
    // =====================================================

    @GetMapping("/facilities")
    public ResponseEntity<List<Facility>>
    getAllFacilities() {

        return ResponseEntity.ok(
                facilityService.getAllFacilities()
        );
    }


    // =====================================================
    // GET FACILITIES OF A SPECIFIC HOSTEL
    // =====================================================

    @GetMapping("/{hostelId}/facilities")
    public ResponseEntity<List<Facility>>
    getHostelFacilities(
            @PathVariable Long hostelId,
            Authentication authentication) {

        return ResponseEntity.ok(
                facilityService.getHostelFacilities(
                        hostelId,
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // REMOVE FACILITY FROM HOSTEL
    // =====================================================

    @DeleteMapping("/{hostelId}/facilities/{facilityId}")
    public ResponseEntity<String> removeFacility(
            @PathVariable Long hostelId,
            @PathVariable Long facilityId,
            Authentication authentication) {

        facilityService.removeFacilityFromHostel(
                hostelId,
                facilityId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Facility removed successfully"
        );
    }
}