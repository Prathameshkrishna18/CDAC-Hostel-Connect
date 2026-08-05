package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.entity.CdacCenter;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.service.AdminHostelService;
import com.cdac.hostelconnect.service.CdacCenterService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final CdacCenterService cdacCenterService;
    private final AdminHostelService adminHostelService;

    public AdminController(
            CdacCenterService cdacCenterService,
            AdminHostelService adminHostelService) {

        this.cdacCenterService = cdacCenterService;
        this.adminHostelService = adminHostelService;
    }


    // =====================================================
    // HOSTELS
    // =====================================================

    


   


    // =====================================================
    // APPROVE / REJECT
    // =====================================================

    

    // =====================================================
    // DASHBOARD COUNTS
    // =====================================================

   


    


    // =====================================================
    // CDAC CENTERS
    // =====================================================

    @GetMapping("/centers")
    public ResponseEntity<List<CdacCenter>> getActiveCenters() {

        return ResponseEntity.ok(
                cdacCenterService.getActiveCenters()
        );
    }


    @GetMapping("/centers/all")
    public ResponseEntity<List<CdacCenter>> getAllCenters() {

        return ResponseEntity.ok(
                cdacCenterService.getAllCenters()
        );
    }


    @GetMapping("/centers/{id}")
    public ResponseEntity<CdacCenter> getCenterById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cdacCenterService.getCenterById(id)
        );
    }
}