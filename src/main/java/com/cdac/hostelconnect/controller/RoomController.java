package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.RoomRequest;
import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/hostels")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // =====================================================
    // GET ROOMS
    // =====================================================

    @GetMapping("/{hostelId}/rooms")
    public ResponseEntity<List<Room>> getRooms(
            @PathVariable Long hostelId,
            Authentication authentication) {

        return ResponseEntity.ok(
                roomService.getRooms(
                        hostelId,
                        authentication.getName()
                )
        );
    }

    // =====================================================
    // ADD ROOM
    // =====================================================

    @PostMapping("/{hostelId}/rooms")
    public ResponseEntity<Room> addRoom(
            @PathVariable Long hostelId,
            @Valid @RequestBody RoomRequest request,
            Authentication authentication) {

        Room room = roomService.addRoom(
                hostelId,
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(room);
    }

    // =====================================================
    // UPDATE ROOM
    // =====================================================

    @PutMapping("/{hostelId}/rooms/{roomId}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long hostelId,
            @PathVariable Long roomId,
            @Valid @RequestBody RoomRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                roomService.updateRoom(
                        hostelId,
                        roomId,
                        request,
                        authentication.getName()
                )
        );
    }

    // =====================================================
    // DELETE ROOM
    // =====================================================

    @DeleteMapping("/{hostelId}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long hostelId,
            @PathVariable Long roomId,
            Authentication authentication) {

        roomService.deleteRoom(
                hostelId,
                roomId,
                authentication.getName()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}