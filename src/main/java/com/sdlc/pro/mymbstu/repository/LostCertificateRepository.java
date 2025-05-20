package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.LostCertificate;
import com.sdlc.pro.mymbstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostCertificateRepository extends JpaRepository<LostCertificate, Long> {
    List<LostCertificate> findByUser(User user);
    List<LostCertificate> findByStatus(String status);
    boolean existsByUserAndStatus(User user, String status);
}