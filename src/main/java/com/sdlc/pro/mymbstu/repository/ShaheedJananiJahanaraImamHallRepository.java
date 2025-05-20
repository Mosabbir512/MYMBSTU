package com.sdlc.pro.mymbstu.repository;


import com.sdlc.pro.mymbstu.model.AlemaKhatunBhashaniHall;
import com.sdlc.pro.mymbstu.model.JananetaAbdulMannanHall;
import com.sdlc.pro.mymbstu.model.ShaheedJananiJahanaraImamHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShaheedJananiJahanaraImamHallRepository extends JpaRepository<ShaheedJananiJahanaraImamHall,Long> {
    long countByStatusFalse();

    List<ShaheedJananiJahanaraImamHall> findByStatusFalse();
    List<ShaheedJananiJahanaraImamHall> findByStatusTrue();
    @Query("SELECT h.hallId FROM ShaheedJananiJahanaraImamHall h WHERE h.status = false ORDER BY h.hallId ASC LIMIT 1")
    Long findFirstAvailableSeatId();

    @Query("SELECT DISTINCT h.roomNumber FROM ShahidZiaurRahmanHall h WHERE h.status = false")
    List<String> findDistinctRoomNumbersWithAvailableSeats();

       ShaheedJananiJahanaraImamHall findFirstByStatusFalse();
    Optional<ShaheedJananiJahanaraImamHall> findByRoomNumberAndSeatNumber(String roomNumber, String seatNumber);

}
