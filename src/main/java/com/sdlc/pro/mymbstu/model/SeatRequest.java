package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seat_requests")
public class SeatRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String fatherName;
    private String fatherOccupation;
    private String fatherPhone;
    private String motherName;
    private String motherOccupation;
    @Column(nullable = false, length = 500)
    private String permanentAddress;
    private String PresentAddress;


    private String lastSemesterResult;
    private String currentCGPA;

    @Column(nullable = false)
    private String familyIncome;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "hall_name")  // Maps to hall_name column in database
    private String hallName;

    @Column(name = "viva_status")  // Maps to viva_status column in database
    private Boolean vivaStatus = false;  // Default value




    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public SeatRequest() {}

    public SeatRequest(User user, String familyIncome, String permanentAddress, String reason,String hallName) {
        this.user = user;
        this.familyIncome = familyIncome;
        this.permanentAddress = permanentAddress;
        this.reason = reason;
        this.hallName=hallName;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getfamilyIncome() { return familyIncome; }
    public void setfamilyIncome(String familyIncome) { this.familyIncome = familyIncome; }
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    public String getReason() { return reason; }
    public String lastSemesterGradeSheet;
    public String picture;
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHallName() { return hallName; }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getFatherPhone() {
        return fatherPhone;
    }

    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getPresentAddress() {
        return PresentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        PresentAddress = presentAddress;
    }

    public String getLastSemesterResult() {
        return lastSemesterResult;
    }

    public void setLastSemesterResult(String lastSemesterResult) {
        this.lastSemesterResult = lastSemesterResult;
    }

    public String getCurrentCGPA() {
        return currentCGPA;
    }

    public void setCurrentCGPA(String currentCGPA) {
        this.currentCGPA = currentCGPA;
    }

    public String getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(String familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getLastSemesterGradeSheet() {
        return lastSemesterGradeSheet;
    }

    public void setLastSemesterGradeSheet(String lastSemesterGradeSheet) {
        this.lastSemesterGradeSheet = lastSemesterGradeSheet;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setHallName(String hallName) { this.hallName = hallName; }
    // In SeatRequest class
    public Boolean getVivaStatus() {
        return vivaStatus;
    }

    public void setVivaStatus(Boolean vivaStatus) {
        this.vivaStatus = vivaStatus;
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}