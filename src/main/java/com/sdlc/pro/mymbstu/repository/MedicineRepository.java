package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String prefix);
}