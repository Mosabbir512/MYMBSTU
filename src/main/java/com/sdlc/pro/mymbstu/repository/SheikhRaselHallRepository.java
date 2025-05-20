package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.BangabandhuSheikhMujiburRahmanHall;
import com.sdlc.pro.mymbstu.model.JananetaAbdulMannanHall;
import com.sdlc.pro.mymbstu.model.SheikhRaselHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SheikhRaselHallRepository extends JpaRepository<SheikhRaselHall, Long> {


    long countByStatusFalse();
    List<SheikhRaselHall> findByStatusTrue();
    List<SheikhRaselHall> findByStatusFalse();
    @Query("SELECT h.hallId FROM SheikhRaselHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    @Query("SELECT h FROM SheikhRaselHall  h WHERE h.status = false ORDER BY h.roomNumber, h.seatNumber LIMIT 1")
    SheikhRaselHall findFirstByStatusFalse();
    long countByRoomNumberAndStatusFalse(String roomNumber);
    List<SheikhRaselHall> findByRoomNumberAndStatusFalse(String roomNumber);

    Optional<SheikhRaselHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);

    @Query("SELECT DISTINCT h.roomNumber FROM SheikhRaselHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();

}