package com.sdlc.pro.mymbstu.repository;




import com.sdlc.pro.mymbstu.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByUserId(String userId);
    List<Patient> findByDoctorNameAndResponseIsNull(String doctorName);

    List<Patient> findAllByOrderByAppointmentDateDesc();
    List<Patient> findByResponseIsNullOrderByAppointmentDateDesc();
    List<Patient> findByDoctorNameAndResponseIsNullOrderByAppointmentDateDesc(String doctorName);


    // Change the sorting order to ASC (ascending) instead of DESC
    @Query("SELECT p FROM Patient p WHERE (p.doctorName = :doctorName OR p.doctorName = 'Any') AND p.response IS NULL ORDER BY p.appointmentDate ASC")
    List<Patient> findRelevantAppointmentsForDoctor(@Param("doctorName") String doctorName);





}

