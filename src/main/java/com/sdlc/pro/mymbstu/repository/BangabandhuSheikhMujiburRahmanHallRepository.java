package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.BangabandhuSheikhMujiburRahmanHall;
import com.sdlc.pro.mymbstu.model.JananetaAbdulMannanHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BangabandhuSheikhMujiburRahmanHallRepository extends JpaRepository<BangabandhuSheikhMujiburRahmanHall, Long> {
    long countByStatusFalse();

    @Query("SELECT DISTINCT h.roomNumber FROM BangabandhuSheikhMujiburRahmanHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();
    List<BangabandhuSheikhMujiburRahmanHall> findByStatusTrue();
    @Query("SELECT h.hallId FROM BangabandhuSheikhMujiburRahmanHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    @Query("SELECT h FROM BangabandhuSheikhMujiburRahmanHall h WHERE h.status = false ORDER BY h.roomNumber, h.seatNumber LIMIT 1")
    BangabandhuSheikhMujiburRahmanHall findFirstByStatusFalse();
    List<BangabandhuSheikhMujiburRahmanHall> findByStatusFalse();
    long countByRoomNumberAndStatusFalse(String roomNumber);
    Optional<BangabandhuSheikhMujiburRahmanHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);

    List<BangabandhuSheikhMujiburRahmanHall> findByRoomNumberAndStatusFalse(String roomNumber);
}