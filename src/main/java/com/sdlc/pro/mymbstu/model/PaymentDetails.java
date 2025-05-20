package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String studentId;



    private String department;
    private String paymentReceiptImagePath;
    private String userId;

    private String hallName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    // Getters and Setters
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getPaymentReceiptImagePath() {
        return paymentReceiptImagePath;
    }

    public void setPaymentReceiptImagePath(String paymentReceiptImagePath) {
        this.paymentReceiptImagePath = paymentReceiptImagePath;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}