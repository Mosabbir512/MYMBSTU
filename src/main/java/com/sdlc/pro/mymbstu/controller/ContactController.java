package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contactPage(HttpSession session, Model model) {

        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "contact"; // Returns the contact.html page
    }


    @GetMapping("/contact/aboutDevelopers")
    public String aboutDevelopers(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/contact#aboutDevelopers"; // Redirects to the contact page and scrolls to the About Developers section
    }

    @GetMapping("/contact/feedback")
    public String feedback(HttpSession session, Model model) {
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        return "redirect:/contact#feedback-form"; // Redirects to the contact page and scrolls to the Feedback Form section
    }

    @PostMapping("/submit-feedback")
    public String submitFeedback(HttpSession session, Model model,@RequestParam String name, @RequestParam String email, @RequestParam String message) {
        // Process the feedback (e.g., save to database, send email, etc.)
        User user=(User) session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        System.out.println("Feedback received from: " + name + " (" + email + ")");
        System.out.println("Message: " + message);

        return "redirect:/contact"; // Redirects back to the contact page after submission
    }
}