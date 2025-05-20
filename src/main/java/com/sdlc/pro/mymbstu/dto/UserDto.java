package com.sdlc.pro.mymbstu.dto;


public class UserDto {
    private String userId;
    private String name;
    private String department;

    // Constructors, getters, setters
    public UserDto() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}