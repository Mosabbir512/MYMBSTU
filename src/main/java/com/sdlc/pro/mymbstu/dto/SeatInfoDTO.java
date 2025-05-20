package com.sdlc.pro.mymbstu.dto;

public

class SeatInfoDTO {
    private String seatNumber;
    private boolean status;

    public SeatInfoDTO(String seatNumber, boolean status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getters and Setters
    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

