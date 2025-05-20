package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.controller.HallReplyController;
import com.sdlc.pro.mymbstu.dto.SeatInfoDTO;
import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HallService {

    @Autowired
    private JananetaAbdulMannanHallRepository jananetaAbdulMannanHallRepository;
    @Autowired
    private SheikhRaselHallRepository sheikhRaselHallRepository;
    @Autowired
    private BangabandhuSheikhMujiburRahmanHallRepository bangabandhuSheikhMujiburRahmanHallRepository;
    @Autowired
    private ShahidZiaurRahmanHallRepository shahidZiaurRahmanHallRepository;
    @Autowired
    private AlemaKhatunBhashaniHallRepository alemaKhatunBhashaniHallRepository;
    @Autowired
    private ShaheedJananiJahanaraImamHallRepository shaheedJananiJahanaraImamHallRepository;
    @Autowired
    private BangamataHallRepository bangamataHallRepository;
    @Autowired
    private SeatRequestRepository seatRequestRepository;
    @Autowired
    private SeatRepository seatRepository;



    @Transactional
    public void assignSeat(Long requestId, String roomNumber, String seatNumber, HttpSession session) {
        // Find the seat request
        User user = (User) session.getAttribute("loggedInUser");
        String hallName = user.getHallName();
        SeatRequest request = seatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Seat request not found"));
        String uid = request.getUser().getId();


        if (hallName.equals("Sheikh Rasel Hall")) {
            SheikhRaselHall hallSeat = sheikhRaselHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            sheikhRaselHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        } else if (hallName.equals("Jananeta Abdul Mannan Hall")) {
            JananetaAbdulMannanHall hallSeat = jananetaAbdulMannanHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            jananetaAbdulMannanHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        } else if (hallName.equals("Bangabandhu Sheikh Mujibur Rahman Hall")) {
            BangabandhuSheikhMujiburRahmanHall hallSeat = bangabandhuSheikhMujiburRahmanHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            bangabandhuSheikhMujiburRahmanHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        } else if (hallName.equals("Shahid Ziaur Rahman Hall")) {
            ShahidZiaurRahmanHall hallSeat = shahidZiaurRahmanHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            shahidZiaurRahmanHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        }else if (hallName.equals("Alema Khatun Bhashani Hall")) {
            AlemaKhatunBhashaniHall hallSeat = alemaKhatunBhashaniHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            alemaKhatunBhashaniHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        }
        else if (hallName.equals("Alema Khatun Bhashani Hall")) {
            ShaheedJananiJahanaraImamHall hallSeat = shaheedJananiJahanaraImamHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            shaheedJananiJahanaraImamHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        } else if (hallName.equals("Alema Khatun Bhashani Hall")) {
            BangamataSheikhFazilatunnesaMujibHall hallSeat =bangamataHallRepository.findByRoomNumberAndSeatNumber(roomNumber, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (hallSeat.isStatus()) {
                throw new RuntimeException("Seat is already occupied");
            }
            // Assign the seat
            hallSeat.setUser(request.getUser());
            hallSeat.setStatus(true);
            bangamataHallRepository.save(hallSeat);
            Seat seat = seatRepository.findSeatById(uid);
            seat.setRoomNumber(hallSeat.getRoomNumber());
            seat.setSeatNumber(hallSeat.getSeatNumber());
            seat.setAllocationDate(String.valueOf(LocalDate.now()));
            seat.setPayment("PENDING");
            //seat.setUser(request.getUser());
            seatRepository.save(seat);
        }



        request.setStatus("APPROVED");
        seatRequestRepository.save(request);

    }


}

