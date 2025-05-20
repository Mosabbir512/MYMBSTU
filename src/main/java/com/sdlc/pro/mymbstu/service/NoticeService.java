package com.sdlc.pro.mymbstu.service;


import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;


    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getActiveNoticesByTypeAndHall(NoticeType noticeType, String hallName) {
        return noticeRepository.findByNoticeTypeAndHallNameAndActiveTrueOrderByPublishDateDesc(noticeType, hallName);
    }
    public List<Notice> getActiveNoticesByType(NoticeType noticeType) {
        return noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(noticeType);
    }

    public Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElse(null);
    }

    public List<Notice> getAllActiveNotices() {
        return noticeRepository.findByActiveTrueOrderByPublishDateDesc();
    }

    public Notice saveNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    // Other service methods as needed
}