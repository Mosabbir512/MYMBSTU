package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    List<Complaint> findAllByOrderByComplainDateDesc();
}
