package com.cdac.hostelconnect.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Order createOrder(double amount, String receipt) throws RazorpayException {

        RazorpayClient razorpayClient =
                new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();

        // Amount must be in paise
        int amountInPaise = (int) (amount * 100);

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);

        Order order = razorpayClient.orders.create(orderRequest);

        return order;
    }
    public String getKeyId() {
        return keyId;
    }
}