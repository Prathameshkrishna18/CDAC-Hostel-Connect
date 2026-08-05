package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.AdminRegisterRequest;
import com.cdac.hostelconnect.dto.LoginRequest;
import com.cdac.hostelconnect.dto.LoginResponse;
import com.cdac.hostelconnect.dto.RegisterRequest;
import com.cdac.hostelconnect.entity.Role;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.UserRepository;
import com.cdac.hostelconnect.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${admin.registration.code}")
    private String adminRegistrationCode;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // =====================================================
    // NORMAL REGISTER
    // STUDENT / HOSTEL OWNER ONLY
    // =====================================================

    public String register(RegisterRequest request) {

        // ADMIN cannot register using normal registration
        if (request.getRole() == Role.ADMIN) {

            throw new RuntimeException(
                    "ADMIN registration is not allowed here"
            );
        }


        // Check email
        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }


        // Check phone
        if (userRepository.existsByPhone(
                request.getPhone())) {

            throw new RuntimeException(
                    "Phone number already registered"
            );
        }


        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setPhone(request.getPhone());

        user.setRole(request.getRole());

        userRepository.save(user);

        return "User registered successfully";
    }


    // =====================================================
    // ADMIN REGISTER
    // SPECIAL ADMIN CODE REQUIRED
    // =====================================================

    public String registerAdmin(
            AdminRegisterRequest request) {


        // Check special admin code
        if (adminRegistrationCode == null ||
                !adminRegistrationCode.equals(
                        request.getAdminCode())) {

            throw new RuntimeException(
                    "Invalid admin registration code"
            );
        }


        // Check email
        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }


        // Check phone
        if (userRepository.existsByPhone(
                request.getPhone())) {

            throw new RuntimeException(
                    "Phone number already registered"
            );
        }


        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setPhone(request.getPhone());


        // IMPORTANT:
        // ADMIN role is assigned ONLY by backend.
        user.setRole(Role.ADMIN);


        userRepository.save(user);

        return "Admin registered successfully";
    }


    // =====================================================
    // LOGIN
    // =====================================================

    public LoginResponse login(
            LoginRequest request) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );


        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }


        // Generate JWT
        String token =
                jwtService.generateToken(
                        user.getEmail()
                );


        // Return login response
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}