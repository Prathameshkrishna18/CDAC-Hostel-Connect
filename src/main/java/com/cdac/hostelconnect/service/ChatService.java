package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.ChatMessageDTO;
import com.cdac.hostelconnect.entity.*;
import com.cdac.hostelconnect.repository.ChatMessageRepository;
import com.cdac.hostelconnect.repository.HostelRepository;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatMessageRepository chatRepository;
    private final HostelRepository hostelRepository;

    public ChatService(
            ChatMessageRepository chatRepository,
            HostelRepository hostelRepository) {

        this.chatRepository = chatRepository;
        this.hostelRepository = hostelRepository;
    }

    // =====================================================
    // Student Messages
    // =====================================================

    public List<ChatMessageDTO> getMessages(
            User student,
            Hostel hostel) {

        return chatRepository
                .findByStudentAndHostelOrderByCreatedAtAsc(student, hostel)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Owner Conversation List
    // =====================================================

    public List<Map<String,Object>> getOwnerConversations(User owner){

        List<Hostel> hostels = hostelRepository.findByOwner(owner);

        if(hostels.isEmpty()){
            return new ArrayList<>();
        }

        Hostel hostel = hostels.get(0);

        List<ChatMessage> chats =
                chatRepository.findByHostelOrderByCreatedAtDesc(hostel);

        Map<Long,Map<String,Object>> map =
                new LinkedHashMap<>();

        for(ChatMessage chat : chats){

            Long studentId = chat.getStudent().getId();

            if(!map.containsKey(studentId)){

                Map<String,Object> item =
                        new LinkedHashMap<>();

                item.put("studentId",studentId);
                item.put("studentName",chat.getStudent().getName());
                item.put("lastMessage",chat.getMessage());
                item.put("createdAt",chat.getCreatedAt());

                map.put(studentId,item);
            }

        }

        return new ArrayList<>(map.values());
    }

    // =====================================================
    // Owner Open Chat
    // =====================================================

    public List<ChatMessageDTO> getConversation(
            User owner,
            Long studentId){

        List<Hostel> hostels =
                hostelRepository.findByOwner(owner);

        if(hostels.isEmpty()){

            throw new RuntimeException("Hostel not found");

        }

        Hostel hostel = hostels.get(0);

        User student = new User();
        student.setId(studentId);

        return chatRepository
                .findByStudentAndHostel(student,hostel)
                .stream()
                .sorted(
                        Comparator.comparing(ChatMessage::getCreatedAt)
                )
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Owner Reply
    // =====================================================

    public ChatMessage ownerReply(
            User owner,
            User student,
            Hostel hostel,
            String message){

        ChatMessage chat = new ChatMessage();

        chat.setOwner(owner);
        chat.setStudent(student);
        chat.setHostel(hostel);

        chat.setSender(Role.HOSTEL_OWNER);

        chat.setMessage(message);

        return chatRepository.save(chat);
    }

    // =====================================================
    // DTO
    // =====================================================

    private ChatMessageDTO convert(ChatMessage message){

        ChatMessageDTO dto = new ChatMessageDTO();

        dto.setId(message.getId());

        dto.setHostelId(message.getHostel().getId());

        dto.setStudentId(message.getStudent().getId());

        dto.setOwnerId(message.getOwner().getId());

        dto.setSender(message.getSender().name());

        dto.setMessage(message.getMessage());

        dto.setCreatedAt(message.getCreatedAt());

        dto.setReadStatus(message.getReadStatus());

        return dto;
    }
}