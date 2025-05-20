package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.service.EmailService;
import com.sdlc.pro.mymbstu.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ApplicantController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @GetMapping("/regApp")
    public String regApp() {
        return "admission/regApp";
    }



        @GetMapping("/loginApp")
    public String loginApp(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if (user == null) {
                return "admission/loginApp";
            }
        }
        model.addAttribute("user", user);
        return "redirect:/admission";
    }

    @PostMapping("/loginApp")
    public String loginApp1(@RequestParam("id") String id,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {

        User user = userService.findById(id);

        if (user != null && user.getPassword().equals(password)) {
            if ("APPLICANT".equalsIgnoreCase(user.getRole())) {
                session.setAttribute("email", user.getEmail());
                session.setAttribute("role", user.getRole());
                session.setAttribute("loggedInApp", user);
                return "redirect:/admission";
            }else {
                model.addAttribute("loggedInUser",user);
                session.setAttribute("loggedInUser", user);
                return "redirect:/admission";
            }

        }
        model.addAttribute("errorMessage", "ID or Password doesn't match.");
        return "redirect:/loginApp";
    }

    @GetMapping("/appPassword")
    public String forgotPasswordPage() {
        return "admission/appPassword";
    }

    @PostMapping("/appPassword")
    public String processForgotPassword(@RequestParam("email") String email,
                                        HttpSession session,
                                        Model model) {
        // Check if user exists
        User user = userService.findByEmail(email);

        if (user == null) {
            // User doesn't exist - create new minimal user
            user = new User();
            user.setId(emailService.extractUsernameFromEmail(email));
            user.setEmail(email);
            // Set default role or other required fields
            user.setRole("APPLICANT"); // Example default role
            userService.save(user);
        }

        // Generate verification code
        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        verificationCodes.put(email, verificationCode);

        // Set verification code in session with expiry
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("verificationCodeExpiry", System.currentTimeMillis() + 120000); // 2 minutes

        // Send email
        emailService.sendVerificationEmail(email, verificationCode);
        System.out.println("Verification code sent to " + email + ": " + verificationCode);

        session.setAttribute("email", email);
        return "redirect:/verifyCode";
    }


}
