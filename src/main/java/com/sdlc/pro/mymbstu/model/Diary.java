package com.sdlc.pro.mymbstu.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "diary")
public class Diary {

    @Id
    private String email; // Unique identifier
    private String name;
    private String designation;
    private String phone;
    private String department;

    // Constructors


    public Diary(String email, String name, String designation, String phone, String department) {
        this.email = email;
        this.name = name;
        this.designation = designation;
        this.phone = phone;
        this.department = department;
    }

    public Diary() {

    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return "Diary{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
