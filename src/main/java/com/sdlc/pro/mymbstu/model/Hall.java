package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hallName;
    private String roomNumber;
    private String seatNumber;
    private boolean status;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    // Constructors
    public Hall() {}

    public Hall(String hallName, String roomNumber, String seatNumber, boolean status) {
        this.hallName = hallName;
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
}
