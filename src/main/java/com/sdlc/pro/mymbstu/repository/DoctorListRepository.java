
package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.DoctorList;
import com.sdlc.pro.mymbstu.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorListRepository extends JpaRepository<DoctorList, Long> {
    //  List<Medicine> findByNameContainingIgnoreCase(String prefix);

    List<DoctorList> findByNameContainingIgnoreCase(String name);

    // 2. Search only locations (distinct values that contain the keyword)
    @Query("SELECT DISTINCT d.location FROM DoctorList d WHERE LOWER(d.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findDistinctLocationsContaining(@Param("keyword") String keyword);
}