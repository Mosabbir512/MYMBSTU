package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.Attestation;
import com.sdlc.pro.mymbstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttestationRepository extends JpaRepository<Attestation, Long> {
    List<Attestation> findByUser(User user);
    List<Attestation> findByStatus(String status);
    boolean existsByUserAndStatus(User user, String status);
}