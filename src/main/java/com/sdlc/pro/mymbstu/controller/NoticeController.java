package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.NoticeType;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.NoticeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notices")
public class NoticeController {
    @Autowired
    private NoticeRepository noticeRepository;

    // Main notices page
    @GetMapping
    public String noticesHome(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");

        //String  hallName=user.getHallName();
        model.addAttribute("academicNotices",noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType.ACADEMIC));
        model.addAttribute("adminNotices",noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType.ADMINISTRATION));
        model.addAttribute("eventNotices",noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType.EVENT));
        model.addAttribute("tenderNotices",noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType.TENDER));
        model.addAttribute("resultNotices",noticeRepository.findByNoticeTypeAndActiveTrueOrderByPublishDateDesc(NoticeType.RESULT));
        model.addAttribute("user",user);
        model.addAttribute("pageTitle", "Notices - MBSTU Portal");
        return "notices/notice";
    }

    // Academic Notices
    @GetMapping("/academic")
    public String academicNotices(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/notices#academic";

    }

    // Administration Notices
    @GetMapping("/administration")
    public String administrationNotices(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/notices#administration";

    }
    // Event Notices
    @GetMapping("/events")
    public String eventNotices(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/notices#events";
    }

    @GetMapping("/results")
    public String examResults(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/notices#results";
    }

    @GetMapping("/tenders")
    public String tenders(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/notices#tenders";
    }


    @GetMapping("noticeAdmin")
    public String noticeAdmin(HttpSession session,Model model){
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "/notices/noticeAdmin";
    }

}