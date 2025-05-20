package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.JananetaAbdulMannanHall;
import com.sdlc.pro.mymbstu.model.SheikhRaselHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JananetaAbdulMannanHallRepository extends JpaRepository<JananetaAbdulMannanHall,Long> {

    long countByStatusFalse();
    List<JananetaAbdulMannanHall> findByStatusTrue();

    List<SheikhRaselHall> findByRoomNumberAndStatusFalse(String roomNumber);
    @Query("SELECT h.hallId FROM JananetaAbdulMannanHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    @Query("SELECT h FROM JananetaAbdulMannanHall h WHERE h.status = false ORDER BY h.roomNumber, h.seatNumber LIMIT 1")
    JananetaAbdulMannanHall findFirstByStatusFalse();

    @Query("SELECT DISTINCT h.roomNumber FROM JananetaAbdulMannanHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();

    Optional<JananetaAbdulMannanHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);

    List<JananetaAbdulMannanHall> findByRoomNumber(String roomNumber);
}
