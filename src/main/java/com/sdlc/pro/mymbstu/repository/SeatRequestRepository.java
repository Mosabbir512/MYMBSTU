package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.SeatRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRequestRepository extends JpaRepository<SeatRequest, Long> {

    Page<SeatRequest> findByStatus(String status, Pageable pageable);

    List<SeatRequest> findByVivaStatusFalse();

    Page<SeatRequest> findByVivaStatusTrue(Pageable pageable);


    // In your SeatRequestRepository interface
    List<SeatRequest> findByStatus(String status);
    List<SeatRequest> findByStatusAndHallName(String status,String hallName);
    Page<SeatRequest> findByStatusAndUser_HallName(String status, String hallName, Pageable pageable);

    @Query("SELECT sr FROM SeatRequest sr WHERE sr.status = 'PENDING' AND sr.vivaStatus = true AND sr.user.hallName = :hallName")
    Page<SeatRequest> findPendingApprovalRequests(@Param("hallName") String hallName, Pageable pageable);
    @Query("SELECT sr FROM SeatRequest sr WHERE sr.status = 'PENDING' AND sr.user.hallName = :hallName AND " +
            "(LOWER(sr.user.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(sr.user.id) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<SeatRequest> searchPendingRequests(@Param("query") String query, @Param("hallName") String hallName, Pageable pageable);}