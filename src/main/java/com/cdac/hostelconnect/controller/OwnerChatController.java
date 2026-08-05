package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.entity.ChatMessage;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.Role;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.UserRepository;
import com.cdac.hostelconnect.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class OwnerChatController {

    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public OwnerChatController(
            UserRepository userRepository,
            HostelRepository hostelRepository,
            ChatService chatService,
            SimpMessagingTemplate messagingTemplate) {

        this.userRepository = userRepository;
        this.hostelRepository = hostelRepository;
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    // =====================================================
    // OWNER CONVERSATIONS
    // GET /api/owner/chat/conversations
    // =====================================================

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(
            Authentication authentication) {

        try {

            User owner = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() ->
                            new RuntimeException("Owner not found"));

            if (owner.getRole() != Role.HOSTEL_OWNER) {

                return ResponseEntity
                        .status(403)
                        .body("Access denied.");
            }

            List<Map<String, Object>> conversations =
                    chatService.getOwnerConversations(owner);

            return ResponseEntity.ok(conversations);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to load conversations.");
        }
    }

    // =====================================================
    // OPEN CHAT
    // GET /api/owner/chat/{studentId}
    // =====================================================

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getConversation(
            @PathVariable Long studentId,
            Authentication authentication) {

        try {

            User owner = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() ->
                            new RuntimeException("Owner not found"));

            return ResponseEntity.ok(
                    chatService.getConversation(
                            owner,
                            studentId
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to load chat.");
        }
    }

    // =====================================================
    // OWNER SEND MESSAGE
    // POST /api/owner/chat/reply
    // =====================================================

    @PostMapping("/reply")
    public ResponseEntity<?> reply(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        try {

            User owner = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() ->
                            new RuntimeException("Owner not found"));

            Long studentId =
                    Long.parseLong(body.get("studentId"));

            String message =
                    body.get("message");

            User student =
                    userRepository.findById(studentId)
                            .orElseThrow(() ->
                                    new RuntimeException("Student not found"));

            List<Hostel> hostels = hostelRepository.findByOwner(owner);

            if(hostels.isEmpty()){
                throw new RuntimeException("Hostel not found");
            }

            Hostel hostel = hostels.get(0);

            ChatMessage chat =
                    chatService.ownerReply(
                            owner,
                            student,
                            hostel,
                            message
                    );

            // ============================================
            // Send realtime update to student
            // ============================================

            messagingTemplate.convertAndSend(
                    "/topic/chat/" + hostel.getId(),
                    chat
            );

            return ResponseEntity.ok(chat);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body("Unable to send message.");
        }
    }
}