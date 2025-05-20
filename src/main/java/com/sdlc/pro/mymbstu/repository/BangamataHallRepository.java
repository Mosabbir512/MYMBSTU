package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.BangamataSheikhFazilatunnesaMujibHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BangamataHallRepository extends JpaRepository<BangamataSheikhFazilatunnesaMujibHall, Long> {
    long countByStatusFalse();
    List<BangamataSheikhFazilatunnesaMujibHall> findByStatusTrue();

    @Query("SELECT h.hallId FROM BangamataSheikhFazilatunnesaMujibHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();
    BangamataSheikhFazilatunnesaMujibHall findFirstByStatusFalse();
    @Query("SELECT DISTINCT h.roomNumber FROM BangamataSheikhFazilatunnesaMujibHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();
    List<BangamataSheikhFazilatunnesaMujibHall> findByStatusFalse();
    long countByRoomNumberAndStatusFalse(String roomNumber);
    List<BangamataSheikhFazilatunnesaMujibHall> findByRoomNumberAndStatusFalse(String roomNumber);
    Optional<BangamataSheikhFazilatunnesaMujibHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);
}