package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.Attestation;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.repository.AttestationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttestationService {

    private final AttestationRepository attestationRepository;

    public AttestationService(AttestationRepository attestationRepository) {
        this.attestationRepository = attestationRepository;
    }

    @Transactional
    public Attestation createAttestation(User user, Seat seat, String reason) {
        // Check if user already has a pending attestation
        if (attestationRepository.existsByUserAndStatus(user, "PENDING")) {
            throw new IllegalStateException("You already have a pending attestation request");
        }

        Attestation attestation = new Attestation(user, seat, reason);
        return attestationRepository.save(attestation);
    }
}