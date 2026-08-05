package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.FacilityResponse;
import com.cdac.hostelconnect.dto.FoodMenuResponse;
import com.cdac.hostelconnect.dto.HostelResponse;
import com.cdac.hostelconnect.dto.PhotoResponse;
import com.cdac.hostelconnect.dto.RoomResponse;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.repository.HostelRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;

    public HostelService(HostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    @Transactional(readOnly = true)
    public List<HostelResponse> getApprovedHostels() {

        List<Hostel> hostels =
                hostelRepository.findByStatus(HostelStatus.APPROVED);

        return hostels.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HostelResponse getApprovedHostelById(Long id) {

        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Hostel not found"));

        if (hostel.getStatus() != HostelStatus.APPROVED) {
            throw new RuntimeException(
                    "This hostel is not approved"
            );
        }

        return convertToResponse(hostel);
    }

    private HostelResponse convertToResponse(Hostel hostel) {

        HostelResponse response = new HostelResponse();

        response.setId(hostel.getId());
        response.setHostelName(hostel.getHostelName());
        response.setDescription(hostel.getDescription());

        response.setAddress(hostel.getAddress());
        response.setCity(hostel.getCity());
        response.setState(hostel.getState());
        response.setPincode(hostel.getPincode());

        response.setContactNumber(hostel.getContactNumber());
        response.setEmail(hostel.getEmail());

        response.setLatitude(hostel.getLatitude());
        response.setLongitude(hostel.getLongitude());

        response.setStatus(hostel.getStatus());
        
        response.setRejectionReason(
                hostel.getRejectionReason()
        );

        // CDAC Center
        if (hostel.getCdacCenter() != null) {

            response.setCdacCenterId(
                    hostel.getCdacCenter().getId()
            );

            response.setCdacCenterName(
                    hostel.getCdacCenter().getCenterName()
            );
        }

        // Rooms
        response.setRooms(
                hostel.getRooms()
                        .stream()
                        .map(room ->
                                new RoomResponse(
                                        room.getId(),
                                        room.getSharingType(),
                                        room.getRent(),
                                        room.getTotalBeds(),
                                        room.getAvailableBeds(),
                                        room.getDescription()
                                )
                        )
                        .collect(Collectors.toList())
        );

        // Food Menu
        response.setFoodMenu(
                hostel.getFoodMenus()
                        .stream()
                        .map(food ->
                                new FoodMenuResponse(
                                        food.getId(),
                                        food.getDay(),
                                        food.getMealType(),
                                        food.getMenu()
                                )
                        )
                        .collect(Collectors.toList())
        );

        // Facilities
        response.setFacilities(
                hostel.getFacilities()
                        .stream()
                        .map(facility ->
                                new FacilityResponse(
                                        facility.getId(),
                                        facility.getName(),
                                        facility.getDescription()
                                )
                        )
                        .collect(Collectors.toList())
        );

        // Photos
        response.setPhotos(
                hostel.getPhotos()
                        .stream()
                        .map(photo ->
                                new PhotoResponse(
                                        photo.getId(),
                                        photo.getImageUrl(),
                                        photo.getDescription()
                                )
                        )
                        .collect(Collectors.toList())
        );

        return response;
    }
}