package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.entity.CdacCenter;
import com.cdac.hostelconnect.service.CdacCenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cdac-centers")
@CrossOrigin(origins = "http://localhost:5173")
public class CdacCenterController {

    private final CdacCenterService cdacCenterService;

    public CdacCenterController(
            CdacCenterService cdacCenterService) {

        this.cdacCenterService = cdacCenterService;
    }

    /**
     * Get all active CDAC centers.
     *
     * Used by Hostel Owner while adding a hostel.
     */
    @GetMapping
    public ResponseEntity<List<CdacCenter>> getActiveCenters() {

        return ResponseEntity.ok(
                cdacCenterService.getActiveCenters()
        );
    }

    /**
     * Get all CDAC centers.
     *
     * Can be used by Admin.
     */
    @GetMapping("/all")
    public ResponseEntity<List<CdacCenter>> getAllCenters() {

        return ResponseEntity.ok(
                cdacCenterService.getAllCenters()
        );
    }

    /**
     * Get a particular CDAC center.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CdacCenter> getCenterById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cdacCenterService.getCenterById(id)
        );
    }
}