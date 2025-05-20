package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.SeatRequest;
import com.sdlc.pro.mymbstu.service.SeatRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/seat-requests")
public class SeatRequestController {

    @Autowired
    private SeatRequestService seatRequestService;

    @PostMapping("/{id}/toggle-viva")
    public ResponseEntity<?> toggleVivaStatus(@PathVariable Long id) {
        try {
            SeatRequest updatedRequest = seatRequestService.toggleVivaStatus(id);
            return ResponseEntity.ok(updatedRequest.getVivaStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling viva status");
        }
    }

}