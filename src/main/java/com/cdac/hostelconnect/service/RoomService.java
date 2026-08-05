package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.RoomRequest;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.entity.SharingType;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.RoomRepository;
import com.cdac.hostelconnect.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public RoomService(
            RoomRepository roomRepository,
            HostelRepository hostelRepository,
            UserRepository userRepository) {

        this.roomRepository = roomRepository;
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }


    // =====================================================
    // GET ROOMS
    // =====================================================

    public List<Room> getRooms(
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

        // Owner authorization
        verifyOwnership(hostel, owner);

        return roomRepository.findByHostelId(hostelId);
    }


    // =====================================================
    // ADD ROOM
    // =====================================================

    public Room addRoom(
            Long hostelId,
            RoomRequest request,
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

        // Owner authorization
        verifyOwnership(hostel, owner);


        // Prevent duplicate sharing type
        boolean exists =
                roomRepository
                        .existsByHostelIdAndSharingType(
                                hostelId,
                                request.getSharingType()
                        );

        if (exists) {

            throw new RuntimeException(
                    "This sharing type already exists for this hostel"
            );
        }


        // Validate beds
        validateBeds(request);


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


        return roomRepository.save(room);
    }


    // =====================================================
    // UPDATE ROOM
    // =====================================================

    public Room updateRoom(
            Long hostelId,
            Long roomId,
            RoomRequest request,
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


        // Owner authorization
        verifyOwnership(hostel, owner);


        Room room =
                roomRepository.findById(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Room not found"
                                )
                        );


        // Make sure room actually belongs
        // to the requested hostel
        if (!room.getHostel()
                .getId()
                .equals(hostelId)) {

            throw new RuntimeException(
                    "Room does not belong to this hostel"
            );
        }


        // Prevent duplicate sharing type
        // when changing sharing type
        if (request.getSharingType()
                != room.getSharingType()) {

            boolean exists =
                    roomRepository
                            .existsByHostelIdAndSharingType(
                                    hostelId,
                                    request.getSharingType()
                            );

            if (exists) {

                throw new RuntimeException(
                        "This sharing type already exists for this hostel"
                );
            }
        }


        // Validate beds
        validateBeds(request);


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


        return roomRepository.save(room);
    }


    // =====================================================
    // DELETE ROOM
    // =====================================================

    public void deleteRoom(
            Long hostelId,
            Long roomId,
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


        // Owner authorization
        verifyOwnership(hostel, owner);


        Room room =
                roomRepository.findById(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Room not found"
                                )
                        );


        // Make sure room belongs to this hostel
        if (!room.getHostel()
                .getId()
                .equals(hostelId)) {

            throw new RuntimeException(
                    "Room does not belong to this hostel"
            );
        }


        roomRepository.delete(room);
    }


    // =====================================================
    // OWNER AUTHORIZATION
    // =====================================================

    private void verifyOwnership(
            Hostel hostel,
            User owner) {

        if (!hostel.getOwner()
                .getId()
                .equals(owner.getId())) {

            throw new RuntimeException(
                    "You are not authorized to access this hostel"
            );
        }
    }


    // =====================================================
    // BED VALIDATION
    // =====================================================

    private void validateBeds(
            RoomRequest request) {

        if (request.getAvailableBeds() >
                request.getTotalBeds()) {

            throw new RuntimeException(
                    "Available beds cannot exceed total beds"
            );
        }
    }
}