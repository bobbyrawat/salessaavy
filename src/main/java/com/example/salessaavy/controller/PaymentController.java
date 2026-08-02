package com.example.salessaavy.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody Map<String, Object> request)
            throws Exception {

        int amount = Integer.parseInt(
                request.get("amount").toString());

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt",
                "sales_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(orderRequest);

        return ResponseEntity.ok(
                new JSONObject(order.toString()).toMap()
        );
    }
}