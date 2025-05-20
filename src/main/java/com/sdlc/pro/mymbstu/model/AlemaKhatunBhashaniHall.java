package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

@Entity
@Table
public class AlemaKhatunBhashaniHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    private String roomNumber;
    private String seatNumber;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public AlemaKhatunBhashaniHall() {
    }

    public AlemaKhatunBhashaniHall(String roomNumber, String seatNumber, boolean status) {
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
        this.status = status;
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
        this.status = (user != null);
    }
}