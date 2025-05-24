package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

import java.time.LocalDate;

import jakarta.persistence.Entity;



@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate complainDate;
    private String complainant;
    private String complain;
    private boolean solved;

    // Constructors
    public Complaint() {
        this.solved = false; // Default to unsolved
    }

    public Complaint(String complainant, String complain) {
        this.complainDate = LocalDate.now();
        this.complainant = complainant;
        this.complain = complain;
        this.solved = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public LocalDate getComplainDate() { return complainDate; }
    public String getComplainant() { return complainant; }
    public String getComplain() { return complain; }
    public boolean isSolved() { return solved; }

    public void setId(Long id) { this.id = id; }
    public void setComplainDate(LocalDate complainDate) { this.complainDate = complainDate; }
    public void setComplainant(String complainant) { this.complainant = complainant; }
    public void setComplain(String complain) { this.complain = complain; }
    public void setSolved(boolean solved) { this.solved = solved; }

    // Optional toString()
    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", complainDate=" + complainDate +
                ", complainant='" + complainant + '\'' +
                ", complain='" + complain + '\'' +
                ", solved=" + solved +
                '}';
    }
}