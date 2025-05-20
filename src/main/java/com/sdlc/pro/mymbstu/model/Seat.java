package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    private String id; // Match with User's ID type

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String hallName;
    private String roomNumber; // e.g., "405"
    private String seatNumber; // e.g., "A", "B", "C", "D"
    private String sessionId; // e.g., "2019-20"
    private String allocationDate;
    private String payment;

    // Default no-args constructor (required by JPA)
    public Seat() {
    }

    // Parameterized constructor
    public Seat(User user, String hallName, String roomNumber,
                String seatNumber, String sessionId, String allocationDate,
                String expiryDate) {
        this.user = user;
        this.hallName = hallName;
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
        this.sessionId = sessionId;
        this.allocationDate = allocationDate;
        this.payment = expiryDate;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}