package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;
import java.util.Random;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId; // Retrieved from session
    private String tokenNumber; // Unique 6-character alphanumeric code
    private String hallName;
    private String mealType; // Lunch/Dinner
    private String mealDate; // Date when the meal is valid


    private boolean verified = false;

    public Token() {
        //this.tokenNumber = generateUniqueToken();
    }

    public Token(String studentId, String hallName, String mealType, String mealDate) {
        this.studentId = studentId;
        this.tokenNumber = generateUniqueToken();
        this.hallName = hallName;
        this.mealType = mealType;
        this.mealDate = mealDate;
        this.verified=false;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    // Generate a unique 6-character alphanumeric token
    private String generateUniqueToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        return token.toString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(String tokenNumber) { this.tokenNumber = tokenNumber; }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getMealDate() { return mealDate; }
    public void setMealDate(String mealDate) { this.mealDate = mealDate; }
}
