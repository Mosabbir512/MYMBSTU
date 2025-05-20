package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.model.SeatRequest;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.SeatRepository;
import com.sdlc.pro.mymbstu.repository.SeatRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatRequestService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatRequestRepository seatRequestRepository;

    @Transactional
    public SeatRequest createSeatRequest(User user, String fatherIncome, String permanentAddress, String reason) {
        SeatRequest request = new SeatRequest(user, fatherIncome, permanentAddress, reason,user.getHallName());
        return seatRequestRepository.save(request);
    }

    public Page<SeatRequest> getPendingRequests(Pageable pageable) {
        return seatRequestRepository.findByStatus("PENDING", pageable);
    }
    public Page<SeatRequest> searchPendingRequests(String query, String hallName, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return seatRequestRepository.findByStatusAndUser_HallName("PENDING", hallName, pageable);
        }
        return seatRequestRepository.searchPendingRequests(query.toLowerCase(), hallName, pageable);
    }
//    @Transactional
    public SeatRequest toggleVivaStatus(Long id) {
        SeatRequest request = seatRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat request not found with id: " + id));

        // Toggle the boolean value
        request.setVivaStatus(!request.getVivaStatus());

        return seatRequestRepository.save(request);
    }


    public void approveRequest(Long requestId) {
        SeatRequest request = seatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        seatRequestRepository.save(request);
    }

    public void rejectRequest(Long requestId) {
        SeatRequest request = seatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        String uid=request.getUser().getId();
        Seat seat1=seatRepository.findSeatById(uid);
        seat1.setSeatNumber("no allocate");
        seat1.setRoomNumber("no allocate");
        seat1.setPayment("no allocate");
        seat1.setAllocationDate("no allocate");
        seatRepository.save(seat1);
        seatRequestRepository.save(request);
    }
}