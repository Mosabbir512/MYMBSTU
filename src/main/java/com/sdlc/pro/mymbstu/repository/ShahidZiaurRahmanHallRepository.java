package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.BangabandhuSheikhMujiburRahmanHall;
import com.sdlc.pro.mymbstu.model.ShahidZiaurRahmanHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShahidZiaurRahmanHallRepository extends JpaRepository<ShahidZiaurRahmanHall, Long> {
    long countByStatusFalse();
    List<ShahidZiaurRahmanHall> findByStatusTrue();

    @Query("SELECT h.hallId FROM ShahidZiaurRahmanHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    @Query("SELECT h FROM ShahidZiaurRahmanHall h WHERE h.status = false ORDER BY h.roomNumber, h.seatNumber LIMIT 1")
    ShahidZiaurRahmanHall findFirstByStatusFalse();
    List<ShahidZiaurRahmanHall> findByRoomNumberAndStatusFalse(String roomNumber);
    Optional<ShahidZiaurRahmanHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);
    @Query("SELECT DISTINCT h.roomNumber FROM ShahidZiaurRahmanHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();
}