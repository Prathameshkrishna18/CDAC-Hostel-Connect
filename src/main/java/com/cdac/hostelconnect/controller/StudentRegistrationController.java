package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.PaymentVerificationRequest;
import com.cdac.hostelconnect.dto.StudentRegistrationRequest;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelStatus;
import com.cdac.hostelconnect.entity.RegistrationStatus;
import com.cdac.hostelconnect.entity.Role;
import com.cdac.hostelconnect.entity.Room;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.entity.StudentRegistration;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.RoomRepository;
import com.cdac.hostelconnect.repository.StudentRegistrationRepository;
import com.cdac.hostelconnect.repository.UserRepository;
import com.cdac.hostelconnect.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.Utils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/student/registrations")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentRegistrationController {

    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;
    private final StudentRegistrationRepository registrationRepository;
    private final RazorpayService razorpayService;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    public StudentRegistrationController(
            UserRepository userRepository,
            HostelRepository hostelRepository,
            RoomRepository roomRepository,
            StudentRegistrationRepository registrationRepository,
            RazorpayService razorpayService
    ) {
        this.userRepository = userRepository;
        this.hostelRepository = hostelRepository;
        this.roomRepository = roomRepository;
        this.registrationRepository = registrationRepository;
        this.razorpayService = razorpayService;
    }

    // =========================================================
    // CREATE REGISTRATION + RAZORPAY ORDER
    //
    // POST /api/student/registrations/create
    // =========================================================

    @PostMapping("/create")
    public ResponseEntity<?> createRegistration(
            @RequestBody StudentRegistrationRequest request,
            Authentication authentication
    ) {

        try {

            // -------------------------------------------------
            // Validate authentication
            // -------------------------------------------------

            if (authentication == null) {
                return ResponseEntity
                        .status(401)
                        .body("Authentication required.");
            }

            String email = authentication.getName();

            // -------------------------------------------------
            // Find logged-in student
            // -------------------------------------------------

            User student = userRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found.")
                    );

            // -------------------------------------------------
            // Only STUDENT can register
            // -------------------------------------------------

            if (student.getRole() != Role.STUDENT) {

                return ResponseEntity
                        .status(403)
                        .body("Only students can register for hostels.");
            }

            // -------------------------------------------------
            // Validate request
            // -------------------------------------------------

            if (request.getHostelId() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Hostel ID is required.");
            }

            if (request.getRoomId() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Room ID is required.");
            }

            // -------------------------------------------------
            // Find hostel
            // -------------------------------------------------

            Hostel hostel = hostelRepository
                    .findById(request.getHostelId())
                    .orElseThrow(() ->
                            new RuntimeException("Hostel not found.")
                    );

            // -------------------------------------------------
            // Hostel must be approved
            // -------------------------------------------------

            if (hostel.getStatus() != HostelStatus.APPROVED) {

                return ResponseEntity
                        .badRequest()
                        .body("This hostel is not approved.");
            }

            // -------------------------------------------------
            // Find room
            // -------------------------------------------------

            Room room = roomRepository
                    .findById(request.getRoomId())
                    .orElseThrow(() ->
                            new RuntimeException("Room not found.")
                    );

            // -------------------------------------------------
            // Room must belong to selected hostel
            // -------------------------------------------------

            if (room.getHostel() == null ||
                    room.getHostel().getId() == null ||
                    !room.getHostel()
                            .getId()
                            .equals(hostel.getId())) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Selected room does not belong to this hostel."
                        );
            }

            // -------------------------------------------------
            // Check available beds
            // -------------------------------------------------

            if (room.getAvailableBeds() == null ||
                    room.getAvailableBeds() <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Selected sharing type is fully occupied."
                        );
            }

            // -------------------------------------------------
            // Prevent duplicate PAID registration
            // -------------------------------------------------

            boolean alreadyRegistered =
                    registrationRepository
                            .existsByStudentAndHostelAndStatus(
                                    student,
                                    hostel,
                                    RegistrationStatus.CONFIRMED
                            );

            if (alreadyRegistered) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "You are already registered for this hostel."
                        );
            }

            // -------------------------------------------------
            // Create Razorpay order
            //
            // ₹500 = 50000 paise
            // -------------------------------------------------

            Order order =
                    razorpayService.createOrder(
                            50000,
                            "HOSTEL_" +
                                    hostel.getId() +
                                    "_STUDENT_" +
                                    student.getId()
                    );

            String razorpayOrderId =
                    order.get("id");

            // -------------------------------------------------
            // Create registration
            // -------------------------------------------------

            StudentRegistration registration =
                    new StudentRegistration();

            registration.setStudent(student);
            registration.setHostel(hostel);
            registration.setRoom(room);

            registration.setRegistrationFee(500.0);

            registration.setStatus(
                    RegistrationStatus.PENDING
            );

            registration.setRazorpayOrderId(
                    razorpayOrderId
            );

            // -------------------------------------------------
            // Save registration
            // -------------------------------------------------

            registration =
                    registrationRepository.save(
                            registration
                    );

            // -------------------------------------------------
            // Prepare Razorpay response
            // -------------------------------------------------

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put(
                    "registrationId",
                    registration.getId()
            );

            response.put(
                    "razorpayOrderId",
                    razorpayOrderId
            );

            response.put(
                    "amount",
                    50000
            );

            response.put(
                    "currency",
                    "INR"
            );

            response.put(
                    "keyId",
                    razorpayService.getKeyId()
            );

            response.put(
                    "studentName",
                    student.getName()
            );

            response.put(
                    "studentEmail",
                    student.getEmail()
            );

            response.put(
                    "studentPhone",
                    student.getPhone() == null
                            ? ""
                            : student.getPhone()
            );

            response.put(
                    "hostelId",
                    hostel.getId()
            );

            response.put(
                    "hostelName",
                    hostel.getHostelName()
            );

            response.put(
                    "roomId",
                    room.getId()
            );

            response.put(
                    "sharingType",
                    room.getSharingType() == null
                            ? ""
                            : room.getSharingType().name()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Unable to create registration."
                    );
        }
    }

    // =========================================================
    // GET MY LATEST REGISTRATION
    //
    // GET /api/student/registrations/my-registration
    //
    // This endpoint is used by StudentProfile.jsx
    // =========================================================

    @GetMapping("/my-registration")
    public ResponseEntity<?> getMyRegistration(
            Authentication authentication
    ) {

        try {

            // -------------------------------------------------
            // Validate authentication
            // -------------------------------------------------

            if (authentication == null) {

                return ResponseEntity
                        .status(401)
                        .body("Authentication required.");
            }

            String email =
                    authentication.getName();

            // -------------------------------------------------
            // Find logged-in student
            // -------------------------------------------------

            User student =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Student not found."
                                    )
                            );

            // -------------------------------------------------
            // Find latest registration
            // -------------------------------------------------

            StudentRegistration registration =
                    registrationRepository
                            .findTopByStudentOrderByCreatedAtDesc(
                                    student
                            )
                            .orElse(null);

            // -------------------------------------------------
            // No registration
            // -------------------------------------------------

            if (registration == null) {

                return ResponseEntity
                        .status(404)
                        .body(
                                "Registration not found."
                        );
            }

            // -------------------------------------------------
            // Prepare safe response
            // -------------------------------------------------

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put(
                    "id",
                    registration.getId()
            );

            response.put(
                    "registrationFee",
                    registration.getRegistrationFee()
            );

            response.put(
                    "status",
                    registration.getStatus() == null
                            ? null
                            : registration.getStatus().name()
            );

            response.put(
                    "razorpayOrderId",
                    registration.getRazorpayOrderId()
            );

            response.put(
                    "razorpayPaymentId",
                    registration.getRazorpayPaymentId()
            );

            response.put(
                    "razorpaySignature",
                    registration.getRazorpaySignature()
            );

            response.put(
                    "createdAt",
                    registration.getCreatedAt()
            );

            // -------------------------------------------------
            // Student information
            // -------------------------------------------------

            Map<String, Object> studentData =
                    new LinkedHashMap<>();

            studentData.put(
                    "id",
                    student.getId()
            );

            studentData.put(
                    "name",
                    student.getName()
            );

            studentData.put(
                    "email",
                    student.getEmail()
            );

            studentData.put(
                    "phone",
                    student.getPhone()
            );

            response.put(
                    "student",
                    studentData
            );

            // -------------------------------------------------
            // Hostel information
            // -------------------------------------------------

            Hostel hostel =
                    registration.getHostel();

            if (hostel != null) {

                Map<String, Object> hostelData =
                        new LinkedHashMap<>();

                hostelData.put(
                        "id",
                        hostel.getId()
                );

                hostelData.put(
                        "hostelName",
                        hostel.getHostelName()
                );

                hostelData.put(
                        "description",
                        hostel.getDescription()
                );

                hostelData.put(
                        "address",
                        hostel.getAddress()
                );

                hostelData.put(
                        "city",
                        hostel.getCity()
                );

                hostelData.put(
                        "state",
                        hostel.getState()
                );

                hostelData.put(
                        "pincode",
                        hostel.getPincode()
                );

                hostelData.put(
                        "contactNumber",
                        hostel.getContactNumber()
                );

                hostelData.put(
                        "email",
                        hostel.getEmail()
                );

                response.put(
                        "hostel",
                        hostelData
                );
            }

            // -------------------------------------------------
            // Room information
            // -------------------------------------------------

            Room room =
                    registration.getRoom();

            if (room != null) {

                Map<String, Object> roomData =
                        new LinkedHashMap<>();

                roomData.put(
                        "id",
                        room.getId()
                );

                roomData.put(
                        "sharingType",
                        room.getSharingType() == null
                                ? null
                                : room.getSharingType().name()
                );

                roomData.put(
                        "rent",
                        room.getRent()
                );

                roomData.put(
                        "totalBeds",
                        room.getTotalBeds()
                );

                roomData.put(
                        "availableBeds",
                        room.getAvailableBeds()
                );

                roomData.put(
                        "description",
                        room.getDescription()
                );

                response.put(
                        "room",
                        roomData
                );
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Unable to load registration details."
                    );
        }
    }

    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    //
    // POST /api/student/registrations/verify-payment
    // =========================================================

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerificationRequest request,
            Authentication authentication
    ) {

        try {

            // -------------------------------------------------
            // Validate authentication
            // -------------------------------------------------

            if (authentication == null) {

                return ResponseEntity
                        .status(401)
                        .body("Authentication required.");
            }

            String email =
                    authentication.getName();

            // -------------------------------------------------
            // Find logged-in student
            // -------------------------------------------------

            User student =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Student not found."
                                    )
                            );

            // -------------------------------------------------
            // Validate request
            // -------------------------------------------------

            if (request.getRazorpayOrderId() == null ||
                    request.getRazorpayOrderId().isBlank()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Razorpay order ID is required."
                        );
            }

            if (request.getRazorpayPaymentId() == null ||
                    request.getRazorpayPaymentId().isBlank()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Razorpay payment ID is required."
                        );
            }

            if (request.getRazorpaySignature() == null ||
                    request.getRazorpaySignature().isBlank()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Razorpay signature is required."
                        );
            }

            // -------------------------------------------------
            // Find registration
            // -------------------------------------------------

            StudentRegistration registration =
                    registrationRepository
                            .findByRazorpayOrderId(
                                    request.getRazorpayOrderId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Registration not found."
                                    )
                            );

            // -------------------------------------------------
            // Make sure registration belongs to student
            // -------------------------------------------------

            if (registration.getStudent() == null ||
                    registration.getStudent().getId() == null ||
                    !registration
                            .getStudent()
                            .getId()
                            .equals(student.getId())) {

                return ResponseEntity
                        .status(403)
                        .body(
                                "Invalid registration."
                        );
            }

            // =================================================
            // IMPORTANT:
            // Prevent double payment verification
            // and double bed reduction
            // =================================================

            if (registration.getStatus() ==
                    RegistrationStatus.CONFIRMED) {

                Map<String, Object> response =
                        new LinkedHashMap<>();

                response.put(
                        "success",
                        true
                );

                response.put(
                        "message",
                        "Payment already verified. Registration is confirmed."
                );

                response.put(
                        "registrationId",
                        registration.getId()
                );

                response.put(
                        "status",
                        RegistrationStatus.CONFIRMED.name()
                );

                return ResponseEntity.ok(response);
            }

            // -------------------------------------------------
            // Prepare Razorpay signature verification
            // -------------------------------------------------

            JSONObject options =
                    new JSONObject();

            options.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            options.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            options.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            // -------------------------------------------------
            // Verify payment signature
            // -------------------------------------------------

            boolean verified =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpaySecret
                    );

            // -------------------------------------------------
            // Payment verification failed
            // -------------------------------------------------

            if (!verified) {

                registration.setStatus(
                        RegistrationStatus.CANCELLED
                );

                registrationRepository.save(
                        registration
                );

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Payment verification failed."
                        );
            }

            // =================================================
            // PAYMENT SUCCESS
            // =================================================

            Room room =
                    registration.getRoom();

            // -------------------------------------------------
            // Check bed availability again
            // -------------------------------------------------

            if (room == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Selected room is no longer available."
                        );
            }

            if (room.getAvailableBeds() == null ||
                    room.getAvailableBeds() <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Sorry, no bed is available in the selected sharing type."
                        );
            }

            // -------------------------------------------------
            // Save Razorpay details
            // -------------------------------------------------

            registration.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            registration.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            registration.setStatus(
                    RegistrationStatus.CONFIRMED
            );

            // -------------------------------------------------
            // Reduce available bed
            // -------------------------------------------------

            room.setAvailableBeds(
                    room.getAvailableBeds() - 1
            );

            roomRepository.save(room);

            // -------------------------------------------------
            // Save registration
            // -------------------------------------------------

            registrationRepository.save(
                    registration
            );

            // -------------------------------------------------
            // Success response
            // -------------------------------------------------

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Payment successful. Registration confirmed."
            );

            response.put(
                    "registrationId",
                    registration.getId()
            );

            response.put(
                    "status",
                    RegistrationStatus.CONFIRMED.name()
            );

            response.put(
                    "razorpayPaymentId",
                    registration.getRazorpayPaymentId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Payment verification failed."
                    );
        }
    }
}