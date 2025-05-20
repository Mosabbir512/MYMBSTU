package com.sdlc.pro.mymbstu.model;

public interface HallSeat {
    String getRoomNumber();
    String getSeatNumber();
    boolean isStatus();
    void setStatus(boolean status);
    void setUser(User user);
    User getUser();
}