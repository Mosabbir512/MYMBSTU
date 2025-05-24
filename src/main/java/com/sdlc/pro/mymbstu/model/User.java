package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Student")
public class User {

    @Id
    private String id; // University-provided unique ID for users
    private String username; // Username
    private String password; // Password

    private String email;    // Email address
    private String phone;    // Phone number
    private String role;     // Role: "ADMIN" or "STUDENT"
    private String department; // Department for students
    private String session2;
    private String hallName;

    // Default constructor for JPA
    public User() {

    }

    // Full constructor


    public User(String id, String username, String password, String email, String phone, String role, String department, String session2, String hallName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.session2 = session2;
        this.hallName = hallName;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSession2() {
        return session2;
    }

    public void setSession2(String session2) {
        this.session2 = session2;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                ", session2='" + session2 + '\'' +
                ", hallName='" + hallName + '\'' +
                '}';
    }
}


