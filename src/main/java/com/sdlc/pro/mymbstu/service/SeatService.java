package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;


    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat findSeatByUserId(String userId) {
        return seatRepository.findById(userId).orElse(null);
    }

    public Optional<Seat> findSeatById(String id) {
        return Optional.ofNullable(seatRepository.findById(id).orElse(null));
    }

    public void saveSeat(Seat newSeat) {
        seatRepository.save(newSeat);
    }
}