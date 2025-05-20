package com.sdlc.pro.mymbstu.config;

import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class Check {
    @Autowired
    private UserService userService;



    public boolean check(HttpSession session, Model model){
        boolean ans=true;
        // Retrieve email and role from session
        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        // Check if email exists and fetch the user from the service
        if (email == null) {
            ans=false;
        }

        User user=null;
        if(ans)
        user = userService.findByEmail(email);

        if (user == null) {
            ans=false;
        }

        // Add user information to the model
        if(ans)
        model.addAttribute("user", user);

        // Redirect based on the user's role
        if ("ADMIN".equalsIgnoreCase(role)) {
            //return "home"; // Admin-specific homepage
            return ans;
        } else {

            return ans;
            //return "home"; // Student-specific homepage
        }
    }
}
