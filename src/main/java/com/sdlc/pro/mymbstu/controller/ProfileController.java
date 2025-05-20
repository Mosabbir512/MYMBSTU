package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfileController {
    @RequestMapping("/profile")
    public String profile(HttpSession session, Model model){
        User user=(User) session.getAttribute("loggedInUser");
        if(user==null){
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        return "/profile";
    }
}
