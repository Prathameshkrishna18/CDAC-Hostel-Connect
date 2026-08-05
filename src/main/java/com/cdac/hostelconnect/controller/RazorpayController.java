package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.service.RazorpayService;
import com.razorpay.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class RazorpayController {

    private final RazorpayService razorpayService;

    public RazorpayController(
            RazorpayService razorpayService) {

        this.razorpayService = razorpayService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestParam double amount,
            @RequestParam String receipt) {

        try {

            Order order =
                    razorpayService.createOrder(
                            500,
                            receipt
                    );

            return ResponseEntity.ok(
                    order.toString()
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Payment order creation failed: "
                                    + e.getMessage()
                    );
        }
    }

    @GetMapping("/key")
    public ResponseEntity<String> getRazorpayKey() {

        return ResponseEntity.ok(
                razorpayService.getKeyId()
        );
    }
}