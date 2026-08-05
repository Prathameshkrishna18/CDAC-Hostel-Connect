package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.entity.Facility;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.FacilityRepository;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public FacilityService(
            FacilityRepository facilityRepository,
            HostelRepository hostelRepository,
            UserRepository userRepository) {

        this.facilityRepository = facilityRepository;
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }


    // =====================================================
    // ADD FACILITY TO HOSTEL
    // =====================================================

    /**
     * Add a facility to a hostel.
     *
     * Only the owner of the hostel can perform this operation.
     *
     * If the facility does not already exist in the master
     * facilities table, it will be created.
     */
    @Transactional
    public Hostel addFacilityToHostel(
            Long hostelId,
            String facilityName,
            String ownerEmail) {

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found"
                                )
                        );


        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );


        // -------------------------------------------------
        // OWNER AUTHORIZATION
        // -------------------------------------------------

        if (
                hostel.getOwner() == null ||
                !hostel.getOwner()
                        .getId()
                        .equals(owner.getId())
        ) {

            throw new RuntimeException(
                    "You are not authorized to modify this hostel"
            );
        }


        // -------------------------------------------------
        // VALIDATE FACILITY NAME
        // -------------------------------------------------

        if (
                facilityName == null ||
                facilityName.trim().isEmpty()
        ) {

            throw new RuntimeException(
                    "Facility name cannot be empty"
            );
        }


        String cleanedFacilityName =
                facilityName.trim();


        // -------------------------------------------------
        // FIND OR CREATE MASTER FACILITY
        // -------------------------------------------------

        Facility facility =
                facilityRepository
                        .findByNameIgnoreCase(
                                cleanedFacilityName
                        )
                        .orElseGet(() ->
                                facilityRepository.save(
                                        new Facility(
                                                cleanedFacilityName,
                                                null
                                        )
                                )
                        );


        // -------------------------------------------------
        // ADD ONLY IF NOT ALREADY PRESENT
        // -------------------------------------------------

        boolean alreadyExists =
                hostel.getFacilities()
                        .stream()
                        .anyMatch(existingFacility ->
                                existingFacility.getId()
                                        .equals(facility.getId())
                        );


        if (!alreadyExists) {

            hostel.getFacilities()
                    .add(facility);
        }


        return hostelRepository.save(hostel);
    }


    // =====================================================
    // GET ALL MASTER FACILITIES
    // =====================================================

    /**
     * Returns all facilities available in the system.
     */
    public List<Facility> getAllFacilities() {

        return facilityRepository.findAll();
    }


    // =====================================================
    // GET FACILITIES OF SPECIFIC HOSTEL
    // =====================================================

    /**
     * Returns facilities assigned to a specific hostel.
     *
     * Only the owner of the hostel can access this endpoint.
     */
    @Transactional(readOnly = true)
    public List<Facility> getHostelFacilities(
            Long hostelId,
            String ownerEmail) {

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found"
                                )
                        );


        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );


        // -------------------------------------------------
        // OWNER AUTHORIZATION
        // -------------------------------------------------

        if (
                hostel.getOwner() == null ||
                !hostel.getOwner()
                        .getId()
                        .equals(owner.getId())
        ) {

            throw new RuntimeException(
                    "You are not authorized to access this hostel"
            );
        }


        // -------------------------------------------------
        // INITIALIZE LAZY COLLECTION INSIDE TRANSACTION
        // -------------------------------------------------

        hostel.getFacilities().size();


        return hostel.getFacilities();
    }


    // =====================================================
    // REMOVE FACILITY FROM HOSTEL
    // =====================================================

    /**
     * Remove a facility from a hostel.
     *
     * The facility itself is NOT deleted from the master
     * facilities table.
     *
     * Only the relationship between hostel and facility
     * is removed.
     */
    @Transactional
    public Hostel removeFacilityFromHostel(
            Long hostelId,
            Long facilityId,
            String ownerEmail) {

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found"
                                )
                        );


        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );


        // -------------------------------------------------
        // OWNER AUTHORIZATION
        // -------------------------------------------------

        if (
                hostel.getOwner() == null ||
                !hostel.getOwner()
                        .getId()
                        .equals(owner.getId())
        ) {

            throw new RuntimeException(
                    "You are not authorized"
            );
        }


        // -------------------------------------------------
        // VALIDATE FACILITY ID
        // -------------------------------------------------

        if (facilityId == null) {

            throw new RuntimeException(
                    "Facility ID cannot be null"
            );
        }


        // -------------------------------------------------
        // REMOVE FACILITY RELATIONSHIP
        // -------------------------------------------------

        boolean removed =
                hostel.getFacilities()
                        .removeIf(facility ->
                                facility.getId()
                                        .equals(facilityId)
                        );


        if (!removed) {

            throw new RuntimeException(
                    "Facility is not assigned to this hostel"
            );
        }


        return hostelRepository.save(hostel);
    }
}