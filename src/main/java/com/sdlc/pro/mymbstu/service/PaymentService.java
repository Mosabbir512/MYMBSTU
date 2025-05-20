package com.sdlc.pro.mymbstu.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private static final double TOKEN_PRICE = 30.0; // Price per token

    public Map<String, String> processPayment(String paymentMethod, double amount, String transactionId) {
        // In a real application, integrate with payment gateway here
        Map<String, String> result = new HashMap<>();

        // Generate a mock transaction ID
        String transactionId1 = "TXN" + System.currentTimeMillis();

        result.put("status", "SUCCESS");
        result.put("transactionId", transactionId);
        result.put("message", "Payment processed successfully");

        return result;
    }

    public double calculateTotal(int tokenCount) {
        return tokenCount * TOKEN_PRICE;
    }
}