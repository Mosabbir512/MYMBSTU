package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;
@Entity
@Table(name = "doctorlist")

public class DoctorList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)

    private String name;
    private String location;

    public DoctorList(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public DoctorList() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
