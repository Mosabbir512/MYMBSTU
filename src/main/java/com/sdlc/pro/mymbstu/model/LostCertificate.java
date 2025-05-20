package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lost_certificate_requests")
public class LostCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String gdPhotoPath;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(nullable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    // Constructors
    public LostCertificate() {}

    public LostCertificate(User user, String gdPhotoPath) {
        this.user = user;
        this.gdPhotoPath = gdPhotoPath;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getGdPhotoPath() { return gdPhotoPath; }
    public void setGdPhotoPath(String gdPhotoPath) { this.gdPhotoPath = gdPhotoPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
}