package com.sdlc.pro.mymbstu.repository;


import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PredefinedStudentRepository extends JpaRepository<PredefinedStudent, String>{

   Optional<PredefinedStudent> findByIdPreAndNamePreAndDepartmentPreAndSessionPreAndHallPre(String idPre, String namePre,String departmentPre, String sessionPre,String hallPre);
   Optional<PredefinedStudent> findByIdPre(String idPre);


}
