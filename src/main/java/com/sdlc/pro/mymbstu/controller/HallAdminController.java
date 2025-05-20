package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import com.sdlc.pro.mymbstu.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class HallAdminController {

    @Autowired private SeatRepository seatRepository;
    @Autowired private SeatRequestRepository seatRequestRepository;
    @Autowired private JananetaAbdulMannanHallRepository jananetaHallRepo;
    @Autowired private BangabandhuSheikhMujiburRahmanHallRepository bangabandhuHallRepo;
    @Autowired private SheikhRaselHallRepository raselHallRepo;
    @Autowired private ShahidZiaurRahmanHallRepository ziaurHallRepo;
    @Autowired private AlemaKhatunBhashaniHallRepository alemaHallRepo;
    @Autowired private BangamataHallRepository bangamataHallRepo;
    @Autowired private ShaheedJananiJahanaraImamHallRepository jahanaraHallRepo;
    @Autowired private EmailService emailService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/admin/seat-approval/{requestId}")
    @Transactional
    public ResponseEntity<?> approveSeat(@PathVariable Long requestId, Model model, HttpSession session) {
        try {
            SeatRequest request = seatRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Seat request not found"));

            User user = request.getUser();
            Seat seat = Optional.ofNullable(seatRepository.findSeatById(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Seat not found"));


            String hallName = request.getHallName().trim();
            switch (hallName.toLowerCase()) {
                case "jananeta abdul mannan hall":
                    allocateJananetaHall(seat, user);
                    break;
                case "bangabandhu sheikh mujibur rahman hall":
                    allocateBangabandhuHall(seat, user);
                    break;
                case "sheikh rasel hall":
                    allocateSheikhRaselHall(seat, user);
                    break;
                case "shahid ziaur rahman hall":
                    allocateZiaurRahmanHall(seat, user);
                    break;
                case "alema khatun bhashani hall":
                    allocateAlemaKhatunHall(seat, user);
                    break;
                case "bangamata sheikh fazilatunnesa mujib hall":
                    allocateBangamataHall(seat, user);
                    break;
                case "shaheed janani jahanara imam hall":
                    allocateJahanaraImamHall(seat, user);
                    break;
                default:
                    throw new RuntimeException("Unknown hall: " + hallName);
            }

            updateAvailableSeatsModel(model);
            seatRequestRepository.delete(request);
            model.addAttribute("success", "Seat allocated successfully");
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            // return "seat/seat_approve";
            // return "redirect:/about_hall_man?scrollToAdmin=true&approvalError=" + e.getMessage();
            return ResponseEntity.badRequest().body(e.getMessage());
            //return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void allocateJananetaHall(Seat seat, User user) {
        JananetaAbdulMannanHall hall = jananetaHallRepo.findFirstByStatusFalse();
        //.orElseThrow(() -> new RuntimeException("No available seats in Jananeta Hall"));
        updateSeatAndHall(seat, user, hall);
        jananetaHallRepo.save(hall);
    }

    private void allocateBangabandhuHall(Seat seat, User user) {
        BangabandhuSheikhMujiburRahmanHall hall = bangabandhuHallRepo.findFirstByStatusFalse();
        //.orElseThrow(() -> new RuntimeException("No available seats in Bangabandhu Hall"));
        updateSeatAndHall(seat, user, hall);
        bangabandhuHallRepo.save(hall);
    }

    // Similar methods for other halls...
    private void allocateSheikhRaselHall(Seat seat, User user) {
        SheikhRaselHall hall = raselHallRepo.findFirstByStatusFalse();
        // .orElseThrow(() -> new RuntimeException("No available seats in Sheikh Rasel Hall"));
        updateSeatAndHall(seat, user, hall);
        raselHallRepo.save(hall);
    }

    private void allocateZiaurRahmanHall(Seat seat, User user) {
        ShahidZiaurRahmanHall hall = ziaurHallRepo.findFirstByStatusFalse();
        //.orElseThrow(() -> new RuntimeException("No available seats in Ziaur Rahman Hall"));
        updateSeatAndHall(seat, user, hall);
        ziaurHallRepo.save(hall);
    }

    private void allocateAlemaKhatunHall(Seat seat, User user) {
        AlemaKhatunBhashaniHall hall = alemaHallRepo.findFirstByStatusFalse();
        // .orElseThrow(() -> new RuntimeException("No available seats in Alema Khatun Hall"));
        updateSeatAndHall(seat, user, hall);
        alemaHallRepo.save(hall);
    }

    private void allocateBangamataHall(Seat seat, User user) {
        BangamataSheikhFazilatunnesaMujibHall hall = bangamataHallRepo.findFirstByStatusFalse();
        //.orElseThrow(() -> new RuntimeException("No available seats in Bangamata Hall"));
        updateSeatAndHall(seat, user, hall);
        bangamataHallRepo.save(hall);
    }

    private void allocateJahanaraImamHall(Seat seat, User user) {
        ShaheedJananiJahanaraImamHall hall = jahanaraHallRepo.findFirstByStatusFalse();
        //  .orElseThrow(() -> new RuntimeException("No available seats in Jahanara Imam Hall"));
        updateSeatAndHall(seat, user, hall);
        jahanaraHallRepo.save(hall);
    }

    // Common update logic
    private void updateSeatAndHall(Seat seat, User user, Object hall) {
        try {

            seat.setRoomNumber((String) hall.getClass().getMethod("getRoomNumber").invoke(hall));
            seat.setSeatNumber((String) hall.getClass().getMethod("getSeatNumber").invoke(hall));
            seat.setAllocationDate(LocalDateTime.now().toString());
            seat.setPayment(LocalDateTime.now().plusYears(2).toString());
            seatRepository.save(seat);

            // Update hall information
            hall.getClass().getMethod("setUser", User.class).invoke(hall, user);
            hall.getClass().getMethod("setStatus", boolean.class).invoke(hall, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update seat and hall information", e);
        }
    }

    // Update model with available seats count
    private void updateAvailableSeatsModel(Model model) {
        model.addAttribute("jamnAvailableSeats", jananetaHallRepo.countByStatusFalse());
        model.addAttribute("bsmrhAvailableSeats", bangabandhuHallRepo.countByStatusFalse());
        model.addAttribute("srhAvailableSeats", raselHallRepo.countByStatusFalse());
        model.addAttribute("szrhAvailableSeats", ziaurHallRepo.countByStatusFalse());
        model.addAttribute("akbhAvailableSeats", alemaHallRepo.countByStatusFalse());
        model.addAttribute("bsfmhAvailableSeats", bangamataHallRepo.countByStatusFalse());
        model.addAttribute("sjjihAvailableSeats", jahanaraHallRepo.countByStatusFalse());
        model.addAttribute("lastUpdated", LocalDateTime.now());
    }

    private void sendApprovalEmail(User user, Seat seat) {
        String subject = "Hall Seat Allocation Approved";
        String message = "Dear " + user.getUsername() + ",\n\n" +
                "Congratulations! Your hall seat application has been approved.\n\n" +
                "Hall: " + seat.getHallName() + "\n" +
                "Room: " + seat.getRoomNumber() + "\n" +
                "Seat: " + seat.getSeatNumber() + "\n\n" +
                "Please visit the hall office to complete your admission process.\n\n" +
                "Regards,\n" +
                "MBSTU Hall Management";

        emailService.sendSimpleMessage(user.getEmail(), subject, message);
    }



    @PostMapping("/admin/seat-rejection/{requestId}")
    @Transactional
    public ResponseEntity<?> rejectSeat(@PathVariable Long requestId, Model model,HttpSession session) {
        try {
            SeatRequest request = seatRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Seat request not found"));
            User user = request.getUser();
            Seat seat = Optional.ofNullable(seatRepository.findSeatById(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            seat.setRoomNumber("no allocate");
            seat.setSeatNumber("no allocate");
            seat.setAllocationDate("no allocate");
            seat.setPayment("no allocate");
            seatRepository.save(seat);

            seatRequestRepository.delete(request);

            // Send rejection email
            sendRejectionEmail(user);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void sendRejectionEmail(User user) {
        String subject = "Hall Seat Application Rejected";
        String message = "Dear " + user.getUsername() + ",\n\n" +
                "We regret to inform you that your hall seat application has been rejected due to insufficient seats available.\n\n" +
                "We sincerely apologize for this inconvenience. Please try again next semester when more seats may become available.\n\n" +
                "Thank you for your understanding.\n\n" +
                "Regards,\n" +
                "MBSTU Hall Management";

        System.out.println(subject+" "+message);
        emailService.sendSimpleMessage(user.getEmail(), subject, message);
    }

    // Similar methods for other halls...






}