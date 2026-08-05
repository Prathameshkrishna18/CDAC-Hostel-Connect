package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.ChatMessageDTO;
import com.cdac.hostelconnect.dto.SendMessageRequest;
import com.cdac.hostelconnect.entity.*;
import com.cdac.hostelconnect.repository.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final ChatMessageRepository chatRepository;
    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(
            ChatMessageRepository chatRepository,
            UserRepository userRepository,
            HostelRepository hostelRepository,
            SimpMessagingTemplate messagingTemplate) {

        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.hostelRepository = hostelRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{hostelId}")
    public List<ChatMessage> loadMessages(
            @PathVariable Long hostelId,
            Authentication authentication){

        User student=userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Hostel hostel=hostelRepository
                .findById(hostelId)
                .orElseThrow();

        return chatRepository
                .findByStudentAndHostelOrderByCreatedAtAsc(
                        student,
                        hostel);
    }

    @PostMapping("/send")
    public ChatMessage send(
            @RequestBody SendMessageRequest request,
            Authentication authentication){

        User sender=userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Hostel hostel=hostelRepository
                .findById(request.getHostelId())
                .orElseThrow();

        User owner=hostel.getOwner();

        ChatMessage chat=new ChatMessage();

        chat.setStudent(sender);

        chat.setOwner(owner);

        chat.setHostel(hostel);

        chat.setSender(Role.STUDENT);

        chat.setMessage(request.getMessage());

        chatRepository.save(chat);

        messagingTemplate.convertAndSend(
                "/topic/chat/"+hostel.getId(),
                chat);

        return chat;

    }

}