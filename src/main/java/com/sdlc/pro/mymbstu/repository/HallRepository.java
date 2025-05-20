package com.sdlc.pro.mymbstu.repository;

import com.sdlc.pro.mymbstu.model.JananetaAbdulMannanHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<JananetaAbdulMannanHall, Long> {

}