package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.AlemaKhatunBhashaniHallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlemaKhatunBhashaniHallService {

    private final AlemaKhatunBhashaniHallRepository hallRepository;

    public AlemaKhatunBhashaniHallService(AlemaKhatunBhashaniHallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public long getTotalAvailableSeats() {
        return hallRepository.countByStatusFalse();
    }

    public List<AlemaKhatunBhashaniHall> getAllAvailableSeats() {
        return hallRepository.findByStatusFalse();
    }

    public long getAvailableSeatsCountByRoom(String roomNumber) {
        return hallRepository.countByRoomNumberAndStatusFalse(roomNumber);
    }

    public List<AlemaKhatunBhashaniHall> getAvailableSeatsByRoom(String roomNumber) {
        return hallRepository.findByRoomNumberAndStatusFalse(roomNumber);
    }



    public AlemaKhatunBhashaniHall autoAssignSeat(User user) {
        AlemaKhatunBhashaniHall seat = hallRepository.findFirstByStatusFalse();
        if (seat != null) {
            seat.setUser(user);
            seat.setStatus(true);
            return hallRepository.save(seat);
        }
        return null;
    }
}