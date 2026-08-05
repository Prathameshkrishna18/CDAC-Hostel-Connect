package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.*;
import com.cdac.hostelconnect.entity.*;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HostelMapper {

    public HostelResponse toResponse(Hostel hostel) {

        HostelResponse response =
                new HostelResponse();

        response.setId(hostel.getId());
        response.setHostelName(
                hostel.getHostelName()
        );
        response.setDescription(
                hostel.getDescription()
        );
        response.setAddress(
                hostel.getAddress()
        );
        response.setCity(
                hostel.getCity()
        );
        response.setState(
                hostel.getState()
        );
        response.setPincode(
                hostel.getPincode()
        );
        response.setContactNumber(
                hostel.getContactNumber()
        );
        response.setEmail(
                hostel.getEmail()
        );
        response.setLatitude(
                hostel.getLatitude()
        );
        response.setLongitude(
                hostel.getLongitude()
        );
        response.setStatus(
                hostel.getStatus()
        );

        if (hostel.getCdacCenter() != null) {

            response.setCdacCenterId(
                    hostel.getCdacCenter().getId()
            );

            response.setCdacCenterName(
                    hostel.getCdacCenter().getCenterName()
            );
        }

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