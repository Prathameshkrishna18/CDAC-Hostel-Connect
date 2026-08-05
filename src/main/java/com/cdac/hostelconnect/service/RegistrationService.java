package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.CreateRegistrationRequest;
import com.cdac.hostelconnect.dto.PaymentOrderResponse;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.RegistrationStatus;
import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.RoomRepository;
import com.cdac.hostelconnect.repository.StudentHostelRegistrationRepository;
import com.cdac.hostelconnect.repository.UserRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final StudentHostelRegistrationRepository registrationRepository;
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public RegistrationService(
            StudentHostelRegistrationRepository registrationRepository,
            HostelRepository hostelRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {

        this.registrationRepository = registrationRepository;
        this.hostelRepository = hostelRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentOrderResponse createRegistration(
            CreateRegistrationRequest request,
            String studentEmail) {

        // -----------------------------------------
        // 1. Find student
        // -----------------------------------------

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );

        // -----------------------------------------
        // 2. Verify role
        // -----------------------------------------

        if (student.getRole() !=
                com.cdac.hostelconnect.entity.Role.STUDENT) {

            throw new RuntimeException(
                    "Only students can register for hostels"
            );
        }

        // -----------------------------------------
        // 3. Find hostel
        // -----------------------------------------

        Hostel hostel = hostelRepository
                .findById(request.getHostelId())
                .orElseThrow(() ->
                        new RuntimeException("Hostel not found")
                );

        // -----------------------------------------
        // 4. Hostel must be approved
        // -----------------------------------------

        if (hostel.getStatus() !=
                com.cdac.hostelconnect.entity.HostelStatus.APPROVED) {

            throw new RuntimeException(
                    "This hostel is not approved"
            );
        }

        // -----------------------------------------
        // 5. Find room
        // -----------------------------------------

        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException("Room not found")
                );

        // -----------------------------------------
        // 6. Verify room belongs to hostel
        // -----------------------------------------

        if (!room.getHostel()
                .getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "Selected room does not belong to this hostel"
            );
        }

        // -----------------------------------------
        // 7. Check room availability
        // -----------------------------------------

        if (room.getAvailableBeds() == null ||
                room.getAvailableBeds() <= 0) {

            throw new RuntimeException(
                    "No beds available in this room"
            );
        }

        // -----------------------------------------
        // 8. Prevent duplicate active registration
        // -----------------------------------------

        boolean alreadyRegistered =
                registrationRepository
                        .existsByStudentIdAndHostelIdAndStatus(
                                student.getId(),
                                hostel.getId(),
                                RegistrationStatus.COMPLETED
                        );

        if (alreadyRegistered) {

            throw new RuntimeException(
                    "You are already registered for this hostel"
            );
        }

        // -----------------------------------------
        // 9. Registration fee
        // -----------------------------------------

        double registrationFee = 500.0;

        // -----------------------------------------
        // 10. Create Razorpay client
        // -----------------------------------------

        try {

            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            razorpayKeyId,
                            razorpayKeySecret
                    );

            // -----------------------------------------
            // 11. Razorpay amount is in paise
            // ₹500 = 50000 paise
            // -----------------------------------------

            int amountInPaise =
                    (int) Math.round(
                            registrationFee * 100
                    );

            JSONObject orderRequest =
                    new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "HOSTEL_" +
                            System.currentTimeMillis()
            );

            orderRequest.put(
                    "payment_capture",
                    1
            );

            Order razorpayOrder =
                    razorpayClient.orders.create(
                            orderRequest
                    );

            String orderId =
                    razorpayOrder.get("id");

            // -----------------------------------------
            // 12. Save registration
            // -----------------------------------------

            com.cdac.hostelconnect.entity
                    .StudentHostelRegistration registration =
                    new com.cdac.hostelconnect.entity
                            .StudentHostelRegistration();

            registration.setStudent(student);
            registration.setHostel(hostel);
            registration.setRoom(room);
            registration.setRegistrationFee(
                    registrationFee
            );
            registration.setRazorpayOrderId(
                    orderId
            );
            registration.setStatus(
                    RegistrationStatus.PENDING
            );

            registrationRepository.save(
                    registration
            );

            // -----------------------------------------
            // 13. Return payment information
            // -----------------------------------------

            return new PaymentOrderResponse(
                    registration.getId(),
                    orderId,
                    registrationFee,
                    "INR",
                    hostel.getHostelName(),
                    room.getSharingType().name()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create payment order: "
                            + e.getMessage()
            );
        }
    }
}