package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

@Entity
public class JananetaAbdulMannanHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    private String roomNumber;
    private String seatNumber;
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Reference to the User who occupies this seat

    // Constructors
    public JananetaAbdulMannanHall() {
    }

    public JananetaAbdulMannanHall(String roomNumber, String seatNumber, boolean status) {
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
        this.status = status;
        this.user = null; // Initially no user assigned
    }

    // Getters and Setters
    public Long getHallId() {
        return hallId;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.status = (user != null); // Update status based on user assignment
    }
}