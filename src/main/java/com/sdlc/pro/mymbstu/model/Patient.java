// Patient.java (Model Class)
package com.sdlc.pro.mymbstu.model;


import jakarta.persistence.*;

import java.time.LocalDate;



@Entity
@Table(name = "appointments")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String username;
    private String problem;

    @Column(columnDefinition = "TEXT")
    private String problemDetails;

    private String doctorName;
    private LocalDate appointmentDate;
    @Column(columnDefinition = "TEXT")
    private String response;

    // Constructor without 'id' (used when saving a new patient)
    public Patient(String userId, String username, String problem, String problemDetails, String doctorName, LocalDate appointmentDate, String response) {
        this.userId = userId;
        this.username = username;
        this.problem = problem;
        this.problemDetails = problemDetails;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.response = response;
    }

    // Constructor with 'id' (useful for fetching from DB)
    public Patient(Long id, String userId, String username, String problem, String problemDetails, String doctorName, LocalDate appointmentDate, String response) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.problem = problem;
        this.problemDetails = problemDetails;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.response = response;
    }

    // No-arg constructor (required by JPA)
    public Patient() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getProblem() { return problem; }
    public void setProblem(String problem) { this.problem = problem; }

    public String getProblemDetails() { return problemDetails; }
    public void setProblemDetails(String problemDetails) { this.problemDetails = problemDetails; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}

