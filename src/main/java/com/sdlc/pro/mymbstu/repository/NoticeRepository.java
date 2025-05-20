package com.sdlc.pro.mymbstu.repository;




import com.sdlc.pro.mymbstu.model.Notice;
import com.sdlc.pro.mymbstu.model.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByNoticeTypeAndHallNameAndActiveTrueOrderByPublishDateDesc(NoticeType noticeType, String hallName);
    List<Notice> findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType noticeType);
    List<Notice> findByActiveTrueOrderByPublishDateDesc();

}