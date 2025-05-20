package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.dto.SeatAllocationDto;
import com.sdlc.pro.mymbstu.dto.UserDto;
import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/halls")
@CrossOrigin(origins = "*")
public class AdminSeatCancelController {

    @Autowired
    private JananetaAbdulMannanHallRepository jananetaHallRepo;
    @Autowired
    private BangabandhuSheikhMujiburRahmanHallRepository bangabandhuHallRepo;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SheikhRaselHallRepository raselHallRepo;
    @Autowired
    private ShahidZiaurRahmanHallRepository ziaurHallRepo;
    @Autowired
    private AlemaKhatunBhashaniHallRepository alemaHallRepo;
    @Autowired
    private BangamataHallRepository bangamataHallRepo;
    @Autowired
    private ShaheedJananiJahanaraImamHallRepository jahanaraHallRepo;


    @GetMapping("/{hallName}/allocated-seats")
    @Transactional()
    public ResponseEntity<List<SeatAllocationDto>> getAllocatedSeats(@PathVariable String hallName) {
        try {
            List<?> seats = switch (hallName) {
                case "JananetaAbdulMannanHall" -> jananetaHallRepo.findByStatusTrue();
                case "BangabandhuSheikhMujiburRahmanHall" -> bangabandhuHallRepo.findByStatusTrue();
                case "SheikhRaselHall" -> raselHallRepo.findByStatusTrue();
                case "ShahidZiaurRahmanHall" -> ziaurHallRepo.findByStatusTrue();
                case "AlemaKhatunBhashaniHall" -> alemaHallRepo.findByStatusTrue();
                case "BangamataSheikhFazilatunnesaMujibHall" -> bangamataHallRepo.findByStatusTrue();
                case "ShaheedJananiJahanaraImamHall" -> jahanaraHallRepo.findByStatusTrue();
                default -> throw new IllegalArgumentException("Invalid hall name");
            };

            List<SeatAllocationDto> dtos = seats.stream().map(seat -> {
                SeatAllocationDto dto = new SeatAllocationDto();

                if (seat instanceof JananetaAbdulMannanHall) {
                    mapJananetaHallToDto((JananetaAbdulMannanHall) seat, dto);
                } else if (seat instanceof BangabandhuSheikhMujiburRahmanHall) {
                    mapBangabandhuHallToDto((BangabandhuSheikhMujiburRahmanHall) seat, dto);
                } else if (seat instanceof SheikhRaselHall) {
                    mapSheikhRaselHallToDto((SheikhRaselHall) seat, dto);
                } else if (seat instanceof ShahidZiaurRahmanHall) {
                    mapShahidZiaurHallToDto((ShahidZiaurRahmanHall) seat, dto);
                } else if (seat instanceof AlemaKhatunBhashaniHall) {
                    mapAlemaKhatunHallToDto((AlemaKhatunBhashaniHall) seat, dto);
                } else if (seat instanceof BangamataSheikhFazilatunnesaMujibHall) {
                    mapBangamataHallToDto((BangamataSheikhFazilatunnesaMujibHall) seat, dto);
                } else if (seat instanceof ShaheedJananiJahanaraImamHall) {
                    mapJahanaraImamHallToDto((ShaheedJananiJahanaraImamHall) seat, dto);
                }

                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            //log.error("Error fetching allocated seats for hall {}: {}", hallName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    ///api/halls/${hallName}/cancel-seat/${seatId}

    @PostMapping("/{hallName}/cancel-seat/{seatId}")
    public ResponseEntity<Map<String, String>> cancelSeat1(
            @PathVariable String hallName,
            @PathVariable Long seatId) {
        try {
             String sid;
            switch (hallName) {
                case "JananetaAbdulMannanHall" -> {
                    JananetaAbdulMannanHall seat = jananetaHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Jananeta Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    jananetaHallRepo.save(seat);
                }
                case "BangabandhuSheikhMujiburRahmanHall" -> {
                    BangabandhuSheikhMujiburRahmanHall seat = bangabandhuHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Bangabandhu Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    bangabandhuHallRepo.save(seat);
                }
                case "ShahidZiaurRahmanHall" -> {
                    ShahidZiaurRahmanHall seat = ziaurHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Shahid Ziaur Hall"));
                   sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    ziaurHallRepo.save(seat);
                }
                case "SheikhRaselHall" -> {
                    SheikhRaselHall seat = raselHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Sheikh Rasel Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    raselHallRepo.save(seat);
                }
                case "AlemaKhatunBhashaniHall" -> {
                    AlemaKhatunBhashaniHall seat = alemaHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Alema Khatun Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    alemaHallRepo.save(seat);
                }
                case "BangamataSheikhFazilatunnesaMujibHall" -> {
                    BangamataSheikhFazilatunnesaMujibHall seat = bangamataHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Bangamata Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    bangamataHallRepo.save(seat);
                }
                case "ShaheedJananiJahanaraImamHall" -> {
                    ShaheedJananiJahanaraImamHall seat = jahanaraHallRepo.findById(seatId)
                            .orElseThrow(() -> new IllegalArgumentException("Seat not found in Jahanara Imam Hall"));
                    sid=seat.getUser().getId();
                    seat.setStatus(false);
                    seat.setUser(null);
                    jahanaraHallRepo.save(seat);
                }
                default -> throw new IllegalArgumentException("Invalid hall name: " + hallName);
            }
            Seat seat1=seatRepository.findSeatById(sid);
            seat1.setRoomNumber("no allocate");
            seat1.setSeatNumber("no allocate");
            seat1.setPayment("no allocate");
            seat1.setAllocationDate("no allocate");
            seatRepository.save(seat1);

            return ResponseEntity.ok(Collections.singletonMap("message", "Seat cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error cancelling seat: " + e.getMessage()));
        }
    }


    private void mapJananetaHallToDto(JananetaAbdulMannanHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapBangabandhuHallToDto(BangabandhuSheikhMujiburRahmanHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapSheikhRaselHallToDto(SheikhRaselHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapShahidZiaurHallToDto(ShahidZiaurRahmanHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapAlemaKhatunHallToDto(AlemaKhatunBhashaniHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapBangamataHallToDto(BangamataSheikhFazilatunnesaMujibHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }

    private void mapJahanaraImamHallToDto(ShaheedJananiJahanaraImamHall seat, SeatAllocationDto dto) {
        dto.setHallId(seat.getHallId());
        dto.setRoomNumber(seat.getRoomNumber());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setStatus(seat.isStatus());
        if (seat.getUser() != null) {
            dto.setUser(mapUserToDto(seat.getUser()));
        }
    }


    private UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setName(user.getUsername());
        userDto.setDepartment(user.getDepartment());
        return userDto;
    }

}