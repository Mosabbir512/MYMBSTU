package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.AdmissionApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionApplicationRepository extends JpaRepository<AdmissionApplication, Long> {


    Page<AdmissionApplication> findByStatusAndApplicantNameContainingIgnoreCase(String status, String name, Pageable pageable);
    int countBySscYear(String sscYear);


    AdmissionApplication findByAppUserAndAppPass(String appUser, String appPass);
    List<AdmissionApplication> findByStatus(String  status);

    Page<AdmissionApplication> findAllByOrderByIdDesc(Pageable pageable);

    Page<AdmissionApplication> findAllByStatusOrderByIdDesc(String status, Pageable pageable);
    AdmissionApplication findByUser(String user);

}