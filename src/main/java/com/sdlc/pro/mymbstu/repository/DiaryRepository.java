package com.sdlc.pro.mymbstu.repository;


import com.sdlc.pro.mymbstu.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, String> {

    List<Diary> findByNameContainingIgnoreCaseOrDesignationContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            String name, String designation, String phone, String email);

    Optional<Diary> findByPhone(String phone);

    List<Diary> findByNameContainingOrEmailContaining(String name, String email);

    @Query("SELECT d.phone FROM Diary d WHERE LOWER(d.email) = LOWER(:email)")
    Optional<String> findPhoneByEmailCaseInsensitive(@Param("email") String email);

}



