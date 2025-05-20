package com.sdlc.pro.mymbstu.service;


import com.sdlc.pro.mymbstu.model.SheikhRaselHall;
import com.sdlc.pro.mymbstu.repository.SheikhRaselHallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SheikhRaselHallService {

    private final SheikhRaselHallRepository hallRepository;

    public SheikhRaselHallService(SheikhRaselHallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public long getTotalAvailableSeats() {
        return hallRepository.countByStatusFalse();
    }

    public List<SheikhRaselHall> getAllAvailableSeats() {
        return hallRepository.findByStatusFalse();
    }

    public long getAvailableSeatsCountByRoom(String roomNumber) {
        return hallRepository.countByRoomNumberAndStatusFalse(roomNumber);
    }

    public List<SheikhRaselHall> getAvailableSeatsByRoom(String roomNumber) {
        return hallRepository.findByRoomNumberAndStatusFalse(roomNumber);
    }

    // Additional business logic methods
}