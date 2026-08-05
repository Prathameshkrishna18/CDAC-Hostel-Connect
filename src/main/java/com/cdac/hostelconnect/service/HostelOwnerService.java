package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.HostelRequest;
import com.cdac.hostelconnect.dto.RoomRequest;
import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.repository.RoomRepository;
import com.cdac.hostelconnect.entity.CdacCenter;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.CdacCenterRepository;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelOwnerService {

    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;
    private final CdacCenterRepository centerRepository;
	private final RoomRepository roomRepository;

    public HostelOwnerService(
            HostelRepository hostelRepository,
            UserRepository userRepository,
            CdacCenterRepository centerRepository,
            RoomRepository roomRepository) {

        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
        this.centerRepository = centerRepository;
        this.roomRepository = roomRepository;
    }


    // =====================================================
    // OWNER
    // =====================================================

    private User getOwner(String ownerEmail) {

        return userRepository
                .findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found"
                        )
                );
    }


    // =====================================================
    // CREATE HOSTEL
    // =====================================================

    public Hostel createHostel(
            HostelRequest request,
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);


        CdacCenter center =
                centerRepository
                        .findById(
                                request.getCdacCenterId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CDAC center not found"
                                )
                        );


        Hostel hostel =
                new Hostel();


        hostel.setHostelName(
                request.getHostelName()
        );

        hostel.setDescription(
                request.getDescription()
        );

        hostel.setAddress(
                request.getAddress()
        );

        hostel.setCity(
                request.getCity()
        );

        hostel.setState(
                request.getState()
        );

        hostel.setPincode(
                request.getPincode()
        );

        hostel.setContactNumber(
                request.getContactNumber()
        );

        hostel.setEmail(
                request.getEmail()
        );

        hostel.setLatitude(
                request.getLatitude()
        );

        hostel.setLongitude(
                request.getLongitude()
        );


        // Assign owner
        hostel.setOwner(owner);


        // Assign CDAC center
        hostel.setCdacCenter(center);


        // New hostel always starts as PENDING
        hostel.setStatus(
                HostelStatus.PENDING
        );


        return hostelRepository.save(hostel);
    }
    
    
 // =====================================================
 // ADD ROOM
 // =====================================================

 public Room addRoom(
         Long hostelId,
         RoomRequest request,
         String ownerEmail) {

     // First verify hostel belongs to logged-in owner
     Hostel hostel =
             getMyHostel(
                     hostelId,
                     ownerEmail
             );


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


     // Validate available beds
     if (
             request.getAvailableBeds()
                     > request.getTotalBeds()
     ) {

         throw new RuntimeException(
                 "Available beds cannot be greater than total beds"
         );
     }


     Room room =
             new Room();


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


     // IMPORTANT
     // Connect room with hostel
     room.setHostel(hostel);


     return roomRepository.save(room);
 }
    
 
//=====================================================
//GET ROOMS
//=====================================================

public List<Room> getRooms(
      Long hostelId,
      String ownerEmail) {

  // Authorization check
  getMyHostel(
          hostelId,
          ownerEmail
  );

  return roomRepository
          .findByHostelId(hostelId);
}


//=====================================================
//UPDATE ROOM
//=====================================================

public Room updateRoom(
     Long hostelId,
     Long roomId,
     RoomRequest request,
     String ownerEmail) {

 // Verify hostel ownership
 Hostel hostel =
         getMyHostel(
                 hostelId,
                 ownerEmail
         );


 Room room =
         roomRepository
                 .findById(roomId)
                 .orElseThrow(() ->
                         new RuntimeException(
                                 "Room not found"
                         )
                 );


 // Verify room belongs to this hostel
 if (
         room.getHostel() == null ||
         !room.getHostel()
                 .getId()
                 .equals(hostel.getId())
 ) {

     throw new RuntimeException(
             "This room does not belong to this hostel"
     );
 }


 // Check duplicate sharing type
 if (
         !room.getSharingType()
                 .equals(
                         request.getSharingType()
                 )
 ) {

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
 if (
         request.getAvailableBeds()
                 > request.getTotalBeds()
 ) {

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


 return roomRepository.save(room);
}


//=====================================================
//DELETE ROOM
//=====================================================

public void deleteRoom(
     Long hostelId,
     Long roomId,
     String ownerEmail) {

 // Verify hostel ownership
 Hostel hostel =
         getMyHostel(
                 hostelId,
                 ownerEmail
         );


 Room room =
         roomRepository
                 .findById(roomId)
                 .orElseThrow(() ->
                         new RuntimeException(
                                 "Room not found"
                         )
                 );


 // Verify room belongs to hostel
 if (
         room.getHostel() == null ||
         !room.getHostel()
                 .getId()
                 .equals(hostel.getId())
 ) {

     throw new RuntimeException(
             "This room does not belong to this hostel"
     );
 }


 roomRepository.delete(room);
}

    // =====================================================
    // GET MY HOSTELS
    // =====================================================

    public List<Hostel> getMyHostels(
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);

        return hostelRepository
                .findByOwner(owner);
    }


    // =====================================================
    // GET SINGLE HOSTEL
    // OWNER AUTHORIZATION
    // =====================================================

    public Hostel getMyHostel(
            Long hostelId,
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);


        Hostel hostel =
                hostelRepository
                        .findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );


        // IMPORTANT:
        // Check whether hostel belongs
        // to currently logged-in owner.

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


        return hostel;
    }


    // =====================================================
    // UPDATE HOSTEL
    // =====================================================

    public Hostel updateHostel(
            Long hostelId,
            HostelRequest request,
            String ownerEmail) {


        Hostel hostel =
                getMyHostel(
                        hostelId,
                        ownerEmail
                );


        CdacCenter center =
                centerRepository
                        .findById(
                                request.getCdacCenterId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CDAC center not found"
                                )
                        );


        // ==========================================
        // UPDATE BASIC INFORMATION
        // ==========================================

        hostel.setHostelName(
                request.getHostelName()
        );

        hostel.setDescription(
                request.getDescription()
        );


        // ==========================================
        // UPDATE LOCATION
        // ==========================================

        hostel.setAddress(
                request.getAddress()
        );

        hostel.setCity(
                request.getCity()
        );

        hostel.setState(
                request.getState()
        );

        hostel.setPincode(
                request.getPincode()
        );


        // ==========================================
        // UPDATE CONTACT
        // ==========================================

        hostel.setContactNumber(
                request.getContactNumber()
        );

        hostel.setEmail(
                request.getEmail()
        );


        // ==========================================
        // UPDATE COORDINATES
        // ==========================================

        hostel.setLatitude(
                request.getLatitude()
        );

        hostel.setLongitude(
                request.getLongitude()
        );


        // ==========================================
        // UPDATE CDAC CENTER
        // ==========================================

        hostel.setCdacCenter(center);


        /*
         * IMPORTANT:
         *
         * We DO NOT automatically change the owner.
         *
         * We DO NOT allow the owner to approve
         * their own hostel.
         *
         * We also keep the existing status.
         *
         * Admin remains responsible for approval.
         */


        return hostelRepository.save(hostel);
    }


    // =====================================================
    // DELETE HOSTEL
    // OWNER AUTHORIZATION
    // =====================================================

    public void deleteHostel(
            Long hostelId,
            String ownerEmail) {


        Hostel hostel =
                getMyHostel(
                        hostelId,
                        ownerEmail
                );


        hostelRepository.delete(hostel);
    }


    // =====================================================
    // DASHBOARD - TOTAL
    // =====================================================

    public long getTotalHostels(
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);

        return hostelRepository
                .countByOwner(owner);
    }


    // =====================================================
    // DASHBOARD - APPROVED
    // =====================================================

    public long getApprovedHostels(
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);

        return hostelRepository
                .countByOwnerAndStatus(
                        owner,
                        HostelStatus.APPROVED
                );
    }


    // =====================================================
    // DASHBOARD - PENDING
    // =====================================================

    public long getPendingHostels(
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);

        return hostelRepository
                .countByOwnerAndStatus(
                        owner,
                        HostelStatus.PENDING
                );
    }


    // =====================================================
    // DASHBOARD - REJECTED
    // =====================================================

    public long getRejectedHostels(
            String ownerEmail) {

        User owner =
                getOwner(ownerEmail);

        return hostelRepository
                .countByOwnerAndStatus(
                        owner,
                        HostelStatus.REJECTED
                );
    }
}