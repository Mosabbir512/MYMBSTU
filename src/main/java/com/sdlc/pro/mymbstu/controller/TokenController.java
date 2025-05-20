package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.Token;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.TokenRepository;
import com.sdlc.pro.mymbstu.service.PaymentService;
import com.sdlc.pro.mymbstu.service.TokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TokenController {

    @Autowired
    private  TokenService tokenService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private  PaymentService paymentService;



    @GetMapping("/token")
    public String tokenHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        return "token/tokenHome";
    }

    @PostMapping("/token/process")
    public String processTokenRequest(
            @RequestParam String hallName,
            @RequestParam(required = false) String lunch,
            @RequestParam(required = false) String dinner,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) String transactionId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Validate payment for mobile payments
        if ((paymentMethod.equals("bKash") || paymentMethod.equals("Rocket") || paymentMethod.equals("Nagad"))
                && (transactionId == null || transactionId.isEmpty())) {
            model.addAttribute("errorMessage", "Phone number  is required for this payment method");
            return "token/tokenHome";
        }

        // Calculate total tokens and amount
        int tokenCount = 0;
        List<Token> generatedTokens = new ArrayList<>();

        if (lunch != null && lunch.equals("lunch")) {
            tokenCount++;
            Token lunchToken = tokenService.createToken(user, hallName, "LUNCH");
            generatedTokens.add(lunchToken);
        }

        if (dinner != null && dinner.equals("dinner")) {
            tokenCount++;
            Token dinnerToken = tokenService.createToken(user, hallName, "DINNER");
            generatedTokens.add(dinnerToken);
        }

        // Process payment
        double totalAmount = paymentService.calculateTotal(tokenCount);
        Map<String, String> paymentResult = paymentService.processPayment(paymentMethod, totalAmount, transactionId);

        if (!paymentResult.get("status").equals("SUCCESS")) {
            model.addAttribute("errorMessage", "Payment failed: " + paymentResult.get("message"));
            return "token/tokenHome";
        }

        model.addAttribute("tokens", generatedTokens);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("paymentMethod", paymentMethod);

        return "token/tokenSuccess";
    }





    @GetMapping("/token/download/{tokenNumber}")
    public ResponseEntity<byte[]> downloadToken(
            @PathVariable String tokenNumber,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Token token = tokenService.validateToken(tokenNumber);
        if (token == null || !token.getStudentId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }

        // Generate token content (in production, use a proper PDF generator)
        String tokenContent = generateTokenContent(token);
        byte[] tokenBytes = tokenContent.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"mbstu_token_" + tokenNumber + ".txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(tokenBytes);
    }

    private String generateTokenContent(Token token) {
        return String.format(
                "MBSTU Official Meal Token\n" +
                        "=========================\n\n" +
                        "Token Number: %s\n" +
                        "Student ID: %s\n" +
                        "Hall: %s\n" +
                        "Meal Type: %s\n" +
                        "Valid Date: %s\n\n" +
                        "Instructions:\n" +
                        "1. Present this token at the dining hall\n" +
                        "2. Token must be used on the valid date\n" +
                        "3. One token per meal only\n\n" +
                        "University Seal\n" +
                        "-------------------------\n" +
                        "MBSTU Hall Management System",
                token.getTokenNumber(),
                token.getStudentId(),
                token.getHallName(),
                token.getMealType(),
                token.getMealDate()
        );
    }

    @GetMapping("/admin/tokens/search-live")
    public ResponseEntity<List<Token>> searchTokensLive(@RequestParam String query) {
        List<Token> tokens = tokenService.findTokensStartingWith(query);return ResponseEntity.ok(tokens);
    }

    @GetMapping("/admin/tokens/search")
    public ResponseEntity<?> searchToken(@RequestParam String tokenNumber) {
        Token token = tokenService.findByTokenNumber(tokenNumber);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
        return ResponseEntity.ok(token);
    }


    @PostMapping("/admin/tokens/verify/{tokenId}")
    public ResponseEntity<?> verifyToken(@PathVariable Long tokenId) {
        Token token = tokenService.verifyToken(tokenId);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
        return ResponseEntity.ok(token);
    }



}