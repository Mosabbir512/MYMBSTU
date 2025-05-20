package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.IDCardApplication;
import com.sdlc.pro.mymbstu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IDCardApplicationRepository extends JpaRepository<IDCardApplication, Long> {
    List<IDCardApplication> findByUser(User user);
    List<IDCardApplication> findByStatusAndHallName(String status,String hallName);

    boolean existsByUserAndStatus(User user, String status);
}