package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
    List<PaymentDetails> findByStudentNameContainingOrStudentIdContaining(String nameQuery, String idQuery);

    // New methods for hall-based filtering
    List<PaymentDetails> findByHallName(String hallName);

    List<PaymentDetails> findByHallNameAndStudentNameContainingOrHallNameAndStudentIdContaining(String hallName1, String nameQuery, String hallName2, String idQuery);
}