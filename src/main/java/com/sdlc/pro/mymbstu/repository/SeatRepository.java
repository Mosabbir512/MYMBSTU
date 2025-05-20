package com.sdlc.pro.mymbstu.repository;


import com.sdlc.pro.mymbstu.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, String> {
    Optional<Seat> findById(String id);
    Seat findSeatById(String id);
    Seat findByUserId(String userId);
    // Method to check if payment is "CONFIRMED" for a specific seat ID
    @Query("SELECT CASE WHEN s.payment = 'CONFIRMED' THEN true ELSE false END FROM Seat s WHERE s.id = :seatId")
    Boolean isPaymentConfirmed(String seatId);

}