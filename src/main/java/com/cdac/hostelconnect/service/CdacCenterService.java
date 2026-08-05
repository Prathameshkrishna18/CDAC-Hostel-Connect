package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.entity.CdacCenter;
import com.cdac.hostelconnect.repository.CdacCenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CdacCenterService {

    private final CdacCenterRepository cdacCenterRepository;

    public CdacCenterService(
            CdacCenterRepository cdacCenterRepository) {

        this.cdacCenterRepository = cdacCenterRepository;
    }

    public List<CdacCenter> getActiveCenters() {
        return cdacCenterRepository.findByActiveTrue();
    }

    public List<CdacCenter> getAllCenters() {
        return cdacCenterRepository.findAll();
    }

    public CdacCenter getCenterById(Long id) {

        return cdacCenterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "CDAC Center not found"
                        )
                );
    }
}