package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.FacilityResponse;
import com.cdac.hostelconnect.dto.FoodMenuResponse;
import com.cdac.hostelconnect.dto.RoomResponse;
import com.cdac.hostelconnect.dto.owner.OwnerFoodMenuRequest;
import com.cdac.hostelconnect.dto.owner.OwnerHostelRequest;
import com.cdac.hostelconnect.dto.owner.OwnerHostelResponse;
import com.cdac.hostelconnect.dto.owner.OwnerRoomRequest;

import com.cdac.hostelconnect.entity.*;

import com.cdac.hostelconnect.repository.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerHostelService {

    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;
    private final CdacCenterRepository cdacCenterRepository;
    private final FacilityRepository facilityRepository;
    private final RoomRepository roomRepository;
    private final FoodMenuRepository foodMenuRepository;

    public OwnerHostelService(
            HostelRepository hostelRepository,
            UserRepository userRepository,
            CdacCenterRepository cdacCenterRepository,
            FacilityRepository facilityRepository,
            RoomRepository roomRepository,
            FoodMenuRepository foodMenuRepository) {

        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
        this.cdacCenterRepository = cdacCenterRepository;
        this.facilityRepository = facilityRepository;
        this.roomRepository = roomRepository;
        this.foodMenuRepository = foodMenuRepository;
    }

    // =========================================================
    // CURRENT OWNER
    // =========================================================

    private User getCurrentOwner() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "Owner authentication required"
            );
        }

        User user =
                userRepository
                        .findByEmail(authentication.getName())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found"
                                )
                        );

        if (user.getRole() != Role.HOSTEL_OWNER) {

            throw new RuntimeException(
                    "Only hostel owners can perform this operation"
            );
        }

        return user;
    }

    // =========================================================
    // GET MY HOSTELS
    // =========================================================

    @Transactional(readOnly = true)
    public List<OwnerHostelResponse> getMyHostels() {

        User owner = getCurrentOwner();

        return hostelRepository
                .findByOwner(owner)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET ONE MY HOSTEL
    // =========================================================

    @Transactional(readOnly = true)
    public OwnerHostelResponse getMyHostel(Long id) {

        Hostel hostel = getOwnerHostel(id);

        return convertToResponse(hostel);
    }

    // =========================================================
    // CREATE HOSTEL
    // =========================================================

    @Transactional
    public OwnerHostelResponse createHostel(
            OwnerHostelRequest request) {

        User owner = getCurrentOwner();

        CdacCenter center =
                cdacCenterRepository
                        .findById(request.getCdacCenterId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CDAC center not found"
                                )
                        );

        Hostel hostel = new Hostel();

        hostel.setHostelName(request.getHostelName());
        hostel.setDescription(request.getDescription());
        hostel.setAddress(request.getAddress());
        hostel.setCity(request.getCity());
        hostel.setState(request.getState());
        hostel.setPincode(request.getPincode());
        hostel.setContactNumber(request.getContactNumber());
        hostel.setEmail(request.getEmail());
        hostel.setLatitude(request.getLatitude());
        hostel.setLongitude(request.getLongitude());

        hostel.setOwner(owner);
        hostel.setCdacCenter(center);

        // Every newly submitted hostel requires admin approval.
        hostel.setStatus(HostelStatus.PENDING);

        if (request.getFacilityIds() != null) {

            List<Facility> facilities =
                    facilityRepository.findAllById(
                            request.getFacilityIds()
                    );

            hostel.setFacilities(facilities);
        }

        Hostel saved =
                hostelRepository.save(hostel);

        return convertToResponse(saved);
    }

    // =========================================================
    // UPDATE HOSTEL
    // =========================================================

    @Transactional
    public OwnerHostelResponse updateHostel(
            Long id,
            OwnerHostelRequest request) {

        Hostel hostel = getOwnerHostel(id);

        CdacCenter center =
                cdacCenterRepository
                        .findById(request.getCdacCenterId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CDAC center not found"
                                )
                        );

        hostel.setHostelName(request.getHostelName());
        hostel.setDescription(request.getDescription());
        hostel.setAddress(request.getAddress());
        hostel.setCity(request.getCity());
        hostel.setState(request.getState());
        hostel.setPincode(request.getPincode());
        hostel.setContactNumber(request.getContactNumber());
        hostel.setEmail(request.getEmail());
        hostel.setLatitude(request.getLatitude());
        hostel.setLongitude(request.getLongitude());

        hostel.setCdacCenter(center);

        if (request.getFacilityIds() != null) {

            List<Facility> facilities =
                    facilityRepository.findAllById(
                            request.getFacilityIds()
                    );

            hostel.setFacilities(facilities);
        }

        /*
         * If an approved hostel is edited,
         * send it back for admin verification.
         */
        if (hostel.getStatus() == HostelStatus.APPROVED) {

            hostel.setStatus(HostelStatus.PENDING);
            hostel.setRejectionReason(null);
        }

        return convertToResponse(
                hostelRepository.save(hostel)
        );
    }

    // =========================================================
    // DELETE HOSTEL
    // =========================================================

    @Transactional
    public void deleteHostel(Long id) {

        Hostel hostel = getOwnerHostel(id);

        hostelRepository.delete(hostel);
    }

    // =========================================================
    // ADD ROOM
    // =========================================================

    @Transactional
    public OwnerHostelResponse addRoom(
            Long hostelId,
            OwnerRoomRequest request) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        if (request.getAvailableBeds() >
                request.getTotalBeds()) {

            throw new RuntimeException(
                    "Available beds cannot be greater than total beds"
            );
        }

        Room room = new Room();

        room.setSharingType(
                request.getSharingType()
        );

        room.setRent(
                request.getRent()
        );

        room.setTotalBeds(
                request.getTotalBeds()
        );

        room.setAvailableBeds(
                request.getAvailableBeds()
        );

        room.setDescription(
                request.getDescription()
        );

        room.setHostel(hostel);

        roomRepository.save(room);

        return convertToResponse(hostel);
    }

    // =========================================================
    // UPDATE ROOM
    // =========================================================

    @Transactional
    public OwnerHostelResponse updateRoom(
            Long hostelId,
            Long roomId,
            OwnerRoomRequest request) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        Room room =
                roomRepository
                        .findById(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Room not found"
                                )
                        );

        if (!room.getHostel().getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "Room does not belong to your hostel"
            );
        }

        if (request.getAvailableBeds() >
                request.getTotalBeds()) {

            throw new RuntimeException(
                    "Available beds cannot be greater than total beds"
            );
        }

        room.setSharingType(
                request.getSharingType()
        );

        room.setRent(
                request.getRent()
        );

        room.setTotalBeds(
                request.getTotalBeds()
        );

        room.setAvailableBeds(
                request.getAvailableBeds()
        );

        room.setDescription(
                request.getDescription()
        );

        roomRepository.save(room);

        return convertToResponse(hostel);
    }

    // =========================================================
    // DELETE ROOM
    // =========================================================

    @Transactional
    public OwnerHostelResponse deleteRoom(
            Long hostelId,
            Long roomId) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        Room room =
                roomRepository
                        .findById(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Room not found"
                                )
                        );

        if (!room.getHostel().getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "Room does not belong to your hostel"
            );
        }

        roomRepository.delete(room);

        return convertToResponse(hostel);
    }

    // =========================================================
    // ADD FOOD
    // =========================================================

    @Transactional
    public OwnerHostelResponse addFoodMenu(
            Long hostelId,
            OwnerFoodMenuRequest request) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        FoodMenu foodMenu = new FoodMenu();

        foodMenu.setDay(request.getDay());
        foodMenu.setMealType(
                request.getMealType()
        );
        foodMenu.setMenu(
                request.getMenu()
        );
        foodMenu.setHostel(hostel);

        foodMenuRepository.save(foodMenu);

        return convertToResponse(hostel);
    }

    // =========================================================
    // UPDATE FOOD
    // =========================================================

    @Transactional
    public OwnerHostelResponse updateFoodMenu(
            Long hostelId,
            Long foodId,
            OwnerFoodMenuRequest request) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        FoodMenu food =
                foodMenuRepository
                        .findById(foodId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Food menu not found"
                                )
                        );

        if (!food.getHostel().getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "Food menu does not belong to your hostel"
            );
        }

        food.setDay(request.getDay());
        food.setMealType(
                request.getMealType()
        );
        food.setMenu(
                request.getMenu()
        );

        foodMenuRepository.save(food);

        return convertToResponse(hostel);
    }

    // =========================================================
    // DELETE FOOD
    // =========================================================

    @Transactional
    public OwnerHostelResponse deleteFoodMenu(
            Long hostelId,
            Long foodId) {

        Hostel hostel =
                getOwnerHostel(hostelId);

        FoodMenu food =
                foodMenuRepository
                        .findById(foodId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Food menu not found"
                                )
                        );

        if (!food.getHostel().getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "Food menu does not belong to your hostel"
            );
        }

        foodMenuRepository.delete(food);

        return convertToResponse(hostel);
    }

    // =========================================================
    // OWNER DASHBOARD STATISTICS
    // =========================================================

    @Transactional(readOnly = true)
    public long getTotalHostels() {

        return hostelRepository.countByOwner(
                getCurrentOwner()
        );
    }

    @Transactional(readOnly = true)
    public long getApprovedHostels() {

        return hostelRepository.countByOwnerAndStatus(
                getCurrentOwner(),
                HostelStatus.APPROVED
        );
    }

    @Transactional(readOnly = true)
    public long getPendingHostels() {

        return hostelRepository.countByOwnerAndStatus(
                getCurrentOwner(),
                HostelStatus.PENDING
        );
    }

    @Transactional(readOnly = true)
    public long getRejectedHostels() {

        return hostelRepository.countByOwnerAndStatus(
                getCurrentOwner(),
                HostelStatus.REJECTED
        );
    }

    // =========================================================
    // SECURITY
    // =========================================================

    private Hostel getOwnerHostel(Long id) {

        User owner = getCurrentOwner();

        Hostel hostel =
                hostelRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );

        if (hostel.getOwner() == null ||
                !hostel.getOwner().getId()
                        .equals(owner.getId())) {

            throw new RuntimeException(
                    "You are not authorized to access this hostel"
            );
        }

        return hostel;
    }

    // =========================================================
    // RESPONSE
    // =========================================================

    private OwnerHostelResponse convertToResponse(
            Hostel hostel) {

        OwnerHostelResponse response =
                new OwnerHostelResponse();

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

        response.setRejectionReason(
                hostel.getRejectionReason()
        );

        if (hostel.getCdacCenter() != null) {

            response.setCdacCenterId(
                    hostel.getCdacCenter().getId()
            );

            response.setCdacCenterName(
                    hostel.getCdacCenter()
                            .getCenterName()
            );
        }

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

        return response;
    }
}