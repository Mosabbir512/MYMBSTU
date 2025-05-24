package com.sdlc.pro.mymbstu.repository;


import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PredefinedStudentRepository extends JpaRepository<PredefinedStudent, String>{

   Optional<PredefinedStudent> findByIdPreAndNamePreAndDepartmentPreAndSessionPreAndHallPre(String idPre, String namePre,String departmentPre, String sessionPre,String hallPre);


  // Optional<PredefinedStudent> findByIdPre(String idPre);
   @Query("SELECT p FROM PredefinedStudent p WHERE LOWER(p.idPre) = LOWER(:idPre)")
   Optional<PredefinedStudent> findByIdPre(@Param("idPre") String idPre);

   List<PredefinedStudent> findAllByOrderByIdPreAsc();  // or any other field you want to sort by
   @Modifying
   @Query("DELETE FROM PredefinedStudent")
   void deleteAllData();


}
