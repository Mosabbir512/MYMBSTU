package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String aboutPage(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "about";
    }

    @GetMapping("/about/mawlana")
    public String aboutMawlana(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "about";
    }

    @GetMapping("/about/achievements")
    public String aboutAchievements(HttpSession session, Model model)
    {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "about";
    }

    @GetMapping("/about/news")
    public String aboutNews(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);return "about";
    }

    @GetMapping("/about/events")
    public String aboutEvents(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);return "about";
    }

    @GetMapping("/about/facts")
    public String aboutFacts(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);return "about";
    }

    @GetMapping("/about/location")
    public String aboutLocation(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "about";
    }
}