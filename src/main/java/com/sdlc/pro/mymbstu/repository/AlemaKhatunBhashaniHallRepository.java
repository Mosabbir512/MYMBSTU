package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlemaKhatunBhashaniHallRepository extends JpaRepository<AlemaKhatunBhashaniHall, Long> {

    long countByStatusFalse();
    List<AlemaKhatunBhashaniHall> findByStatusFalse();
    List<AlemaKhatunBhashaniHall> findByStatusTrue();

    @Query("SELECT h.hallId FROM AlemaKhatunBhashaniHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    @Query("SELECT DISTINCT h.roomNumber FROM AlemaKhatunBhashaniHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();
    long countByRoomNumberAndStatusFalse(String roomNumber);
    List<AlemaKhatunBhashaniHall> findByRoomNumberAndStatusFalse(String roomNumber);


    Optional<AlemaKhatunBhashaniHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);

    @Query("SELECT DISTINCT h.roomNumber FROM AlemaKhatunBhashaniHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersByStatusFalse();

    @Query("SELECT h FROM AlemaKhatunBhashaniHall h WHERE h.status = false ORDER BY h.roomNumber, h.seatNumber LIMIT 1")
    AlemaKhatunBhashaniHall findFirstByStatusFalse();
}