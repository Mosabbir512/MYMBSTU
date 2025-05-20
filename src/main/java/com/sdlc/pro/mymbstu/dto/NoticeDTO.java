package com.sdlc.pro.mymbstu.dto;

import com.sdlc.pro.mymbstu.model.NoticeType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class NoticeDTO {
    private String title;
    private String description;
    private String noticeDate;
    private NoticeType noticeType;
    private MultipartFile noticeFile;
    private LocalDate startDate;  // Add these fields
    private LocalDate endDate;
    private String hallName;  // Add this field

    // Getters and Setters
    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
// Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public MultipartFile getNoticeFile() {
        return noticeFile;
    }

    public void setNoticeFile(MultipartFile noticeFile) {
        this.noticeFile = noticeFile;
    }
}
