package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.AdminRegisterRequest;
import com.cdac.hostelconnect.dto.LoginRequest;
import com.cdac.hostelconnect.dto.LoginResponse;
import com.cdac.hostelconnect.dto.RegisterRequest;
import com.cdac.hostelconnect.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =====================================================
    // NORMAL REGISTER
    // STUDENT / HOSTEL OWNER
    // =====================================================

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        try {

            String response = authService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // LOGIN
    // STUDENT / HOSTEL OWNER / ADMIN
    // =====================================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }


    // =====================================================
    // ADMIN REGISTER
    // SPECIAL ADMIN REGISTRATION CODE REQUIRED
    // =====================================================

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(
            @Valid @RequestBody AdminRegisterRequest request) {

        try {

            String response =
                    authService.registerAdmin(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}