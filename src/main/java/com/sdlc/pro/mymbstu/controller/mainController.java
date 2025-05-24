package com.sdlc.pro.mymbstu.controller;


import com.sdlc.pro.mymbstu.model.Diary;
import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.DiaryRepository;
import com.sdlc.pro.mymbstu.repository.PredefinedStudentRepository;
import com.sdlc.pro.mymbstu.repository.UserRepository;
import com.sdlc.pro.mymbstu.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class mainController {

    @Autowired
    private SeatService seatService;
    @Autowired
    private SeatRequestService seatRequestService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;
    @Autowired
    private PredefinedStudentService predefinedStudentService;

    private final Map<String, String> verificationCodes = new HashMap<>();

    @PostConstruct
    public void init() {
        createAdminIfNotExists("admin", "admin", "1234", "admin@gmail.com", "01710203040", "ADMIN", "ICT", "", "");
        createAdminIfNotExists("admin_1", "admin_1", "1234", "admin@gmail.com", "01710203040", "ADMIN", "ICT", "", "");
        createAdminIfNotExists("provostSRH", "provostSRH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "SRH", "", "Sheikh Rasel Hall");
        createAdminIfNotExists("provostBSMRH", "provostBSMRH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "BSMRH", "", "Bangabandhu Sheikh Mujibur Rahman Hall");
        createAdminIfNotExists("provostJAMH", "provostJAMH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "JAMH", "", "Jananeta Abdul Mannan Hall");
        createAdminIfNotExists("provostSZRH", "provostSZRH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "SZRH", "", "Shahid Ziaur Rahman Hall");
        createAdminIfNotExists("provostAKBH", "provostAKBH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "AKBH", "", "Alema Khatun Bhashani Hall");
        createAdminIfNotExists("provostSJJIH", "provostSJJIH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "SJJIH", "", "Shaheed Janani Jahanara Imam Hall");
        createAdminIfNotExists("provostBSFMH", "provostBSFMH", "1234", "provost20@gmail.com", "01710203040", "PROVOST", "BSFMH", "", "Bangamata Sheikh Fazilatunnesa Mujib Hall");
        createAdminIfNotExists("stafSRH", "stafSRH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Sheikh Rasel Hall");
        createAdminIfNotExists("stafBSMRH", "stafBSMRH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Bangabandhu Sheikh Mujibur Rahman Hall");
        createAdminIfNotExists("stafJAMH", "stafJAMH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Jananeta Abdul Mannan Hall");
        createAdminIfNotExists("stafSZRH", "stafSZRH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Shahid Ziaur Rahman Hal");
        createAdminIfNotExists("stafAKBH", "stafAKBH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Alema Khatun Bhashani Hall");
        createAdminIfNotExists("stafSJJIH", "stafSJJIH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Shaheed Janani Jahanara Imam Hall");
        createAdminIfNotExists("stafBSFMH", "stafBSFMH", "1234", "staf01@gmail.com", "01720304050", "staf", "SRH", "", "Bangamata Sheikh Fazilatunnesa Mujib Hall");
        createAdminIfNotExists("doctor_1", "Dr. Ahmad Hossain Siddiqui", "1234", "ahmadhossain@gmail.com", "", "ADMIN", "Senior Consultant", "", "");
        createAdminIfNotExists("doctor_2", "Dr. Kawsar Ahmad", "1234", "kawsarahmad@gmail.com", "", "ADMIN", "Chief Medical Officer", "", "");
        createAdminIfNotExists("doctor_3", "Dr. Lipa Debnath", "1234", "lipadebnath@gmail.com", "", "ADMIN", "Emergency Specialist", "", "");
        createAdminIfNotExists("doctor_4", "Dr. Harun-Ur-Rashid Rasel", "1234", "harunrashid@gmail.com", "", "ADMIN", "Oncology Specialist", "", "");
        createAdminIfNotExists("doctor_5", "Dr. Nur Md. Kawser Abid", "1234", "nurmdkawser@gmail.com", "", "ADMIN", "Full-time Physician", "", "");
        createAdminIfNotExists("doctor_6", "Dr. Tahmina Yasmin", "1234", "tahminayasmin@gmail.com", "", "ADMIN", "Specialist", "", "");


        insertIfNotExists("anowarkabir.ict@mbstu.ac.bd", "Md. Anowar Kabir", "Lecturer", "01747345676", "ICT");
        insertIfNotExists("mahedihasan.ict@mbstu.ac.bd", "Mahedi Hasan", "Lecturer", "01747345676", "ICT");
        insertIfNotExists("zia@mbstu.ac.bd", "Mr. Ziaur Rahman", "Associate Professor", "1738652939", "ICT");
        insertIfNotExists("abir.hossain@mbstu.ac.bd", "Dr. Md. Abir Hossain", "Associate Professor", "01719257385", "ICT");
        insertIfNotExists("badrul.ict@gmail.com", "Mr.  Mohammad Badrul Alam Miah", "Professor", "01712796569", "ICT");
        insertIfNotExists("monirmorshed.ict@mbstu.ac.bd", "Monir Morshed", "Professor", "01793667714", "ICT");
        insertIfNotExists("nargis_ict@mbstu.ac.bd", "Mst. Nargis Aktar", "Professor", "01701876194", "ICT");
        insertIfNotExists("sajjad@mbstu.ac.bd", "Dr Sajjad Waheed", "Professor", "01787064807", "ICT");
        insertIfNotExists("shahin.mbstu@mbstu.com", "Dr Muhammad Shahin Uddin", "Professor", "01759852318", "ICT");

        insertIfNotExists("shahjahan.ict@gmail.com", "Mr. Md. Shahjahan Sheraj", "Assistant Registrar", "01763369632", "ICT");
        insertIfNotExists("mustafakamalmbstu@gamil.com", "Mr. Mohammad Mustafa Kamal", "Assistant Registrar", "01635275137", "ICT");
        insertIfNotExists("tmahamid1980@gmail.com", "Mr. T.M.A. Hamid", "Section Officer", "01718556580", "ICT");
        insertIfNotExists("rafikulislamict79@gmail.com", "Mohammad Rafikul Islam ", "Section Officer (Seminar Library)", "01718556580", "ICT");


    }

    private void insertIfNotExists(String email, String name, String designation, String phone, String department) {
        if (!diaryRepository.existsById(email)) {
            Diary diary = new Diary(email, name, designation, phone, department);
            diaryRepository.save(diary);
        }
    }

    private void createAdminIfNotExists(String id, String username, String password, String email, String phone, String role, String department, String session, String hallName) {
        if (userService.findById(id) == null) {
            User adminUser = new User(id, username, password, email, phone, role, department, session, hallName);
            userService.save(adminUser);
        }
    }

    // Home page
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
        }
        model.addAttribute("user", user);
        return "home";
    }

    @Autowired
    PredefinedStudentRepository predefinedStudentRepository;
    @Autowired
    private UserRepository userRepository;


    // Login page
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "successMessage", required = false) String successMessage,
            @RequestParam(value = "errorMessage", required = false) String errorMessage,
            HttpSession session,
            Model model) {

//        List<PredefinedStudent> allStudents = predefinedStudentService.getAllStudents();
//        System.out.println("ID | Name | Department | Session | Hall");
//        User user=userService.findById("IT20019");
//        System.out.println(user.toString());
//        user.setHallName("Sheikh Rasel Hall");
//        userService.save(user);
//        System.out.println("----------------------------------------");
//        for (PredefinedStudent student : allStudents) {
//            System.out.printf("%s | %s | %s | %s | %s%n",
//                    student.getIdPre(),
//                    student.getNamePre(),
//                    student.getDepartmentPre(),
//                    student.getSessionPre(),
//                    student.getHallPre());
//        }


        String email = (String) session.getAttribute("email");
        if (email != null && userService.findByEmail(email) != null) {
            return "redirect:/home"; // Redirect logged-in users to home page
        }

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "basic/login"; // Show login page only for non-authenticated users
    }


    @PostMapping("/login")
    public String login(
            @RequestParam("id") String id,
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
            }

            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getRole());
            session.setAttribute("loggedInUser", user);


            if (id.matches("(?i)doctor_[1-6]")) {
                return "redirect:/medical/dashboard";
            }

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/home";
                // return "redirect:/adminHome";
            } else {
                return "redirect:/home";
            }
        }

        model.addAttribute("errorMessage", "ID or Password doesn't match.");
        return "basic/login";
    }

    @Autowired
    DiaryRepository diaryRepository;

    // Registration page
    @GetMapping("/register")
    public String registerPage() {
        return "basic/register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("id") String id,


            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,


            HttpSession session,
            Model model) {
        // @RequestParam("username") String username,


//        if (phone.length() < 11) {
//            model.addAttribute("errorMessage", "Phone number must be at least 11 characters long.");
//            return "basic/register";
//        }

        Optional<PredefinedStudent> oldStu = predefinedStudentRepository.findByIdPre(id);

        if (oldStu.isEmpty()) {
            model.addAttribute("errorMessage", "No student found with the given ID.(Invalid student Id)");
            return "basic/register";

            //System.out.println("No student found with the given ID.");

        }
        // @RequestParam("username") String username,
        PredefinedStudent student = oldStu.get();
        String username = student.getNamePre();

        // @RequestParam("department") ,
        String department = student.getDepartmentPre();
        // @RequestParam("session2") String session2,
        String session2 = student.getSessionPre();
//        @RequestParam("hallName") String hallName,
        String hallName = student.getHallPre();


        // @RequestParam("email") String email,

        String email = id.toLowerCase() + "@mbstu.ac.bd";
        //  @RequestParam("phone") String phone,
        // Get the Optional<String> from the repository
        Optional<String> phoneOptional = diaryRepository.findPhoneByEmailCaseInsensitive(email);

// Assign to a String variable (with fallback if not found)
        String phone = phoneOptional.orElse("01834060206"); // Default value if absent


        if (userService.findById(id) != null) {
            model.addAttribute("errorMessage", "Id(user) already exists.");
            return "basic/register";
        }


//        if (!(email.endsWith("@gmail.com") || email.endsWith("@mbstu.ac.bd"))) {
//            model.addAttribute("errorMessage", "Email must end with @gmail.com or @mbstu.ac.bd");
//            return "basic/register";
//        }


        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "basic/register";
        }

//        if (userService.findByEmail(email) != null) {
//            model.addAttribute("errorMessage", "Email already exists.");
//            return "basic/register";
//        }


//        if(predefinedStudentService.findStudentById(id)==null)
//        {
//            model.addAttribute("errorMessage", "Please,Enter a valid Student ID");
//            return "basic/register";
//        }

//        if (predefinedStudentService.searchPredefinedStudent(id,username,department,session2,hallName).isEmpty()) {
//            Optional<PredefinedStudent> predefinedStudent=predefinedStudentService.searchPredefinedStudent(id,username,department,session2,hallName);
//            System.out.println("inserted info:\n\n\n");
//
//            System.out.println(id+" "+username+" "+department+" "+session2+" "+hallName);
//            System.out.println("\n\ndatabase info:");
//            PredefinedStudent p=predefinedStudentService.findStudentById(id);
//            System.out.println(p.getIdPre()+" "+p.getNamePre()+" "+p.getHallPre()+" "+p.getSessionPre());
//                model.addAttribute("errorMessage", "Provided information does not match with any legal student's Information.Please,Enter your information correctly");
//                return "basic/register";
//            }


        User newUser = new User(id, username, password, email, phone, "STUDENT", department, session2, hallName);
//        userService.save(newUser);
        session.setAttribute("newUser", newUser);


        if (seatService.findSeatById(id).isEmpty()) {
            Seat newSeat = new Seat();
            newSeat.setId(id);
            newSeat.setHallName(hallName);
            newSeat.setSessionId(session2);
            newSeat.setRoomNumber("no allocate");
            newSeat.setSeatNumber("no allocate");
            newSeat.setAllocationDate("no allocate");
            newSeat.setPayment("no allocate");
            seatService.saveSeat(newSeat);
            model.addAttribute("seat12345", newSeat);
        }

        //session.setAttribute("loggedInUser", newUser);

        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        verificationCodes.put(email, verificationCode);


        System.out.println("Verification code sent to " + email + ": " + verificationCode);
        emailService.sendVerificationEmail(email, verificationCode);

        session.setAttribute("email", email);

        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("verificationCodeExpiry", System.currentTimeMillis() + 120000);


        return "redirect:/insertCode";
    }


    @GetMapping("/insertCode")
    public String insertCodePage() {
        return "basic/insertCode";
    }

    @PostMapping("/insertCode")
    public String processInsertCode(@RequestParam("code") String code, HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        String storedCode = verificationCodes.get(email);

        if (storedCode != null && storedCode.equals(code)) {
            User user = (User) session.getAttribute("newUser");
            if (user != null) {
                userService.save(user);
                session.setAttribute("loggedInUser", user);
            }
            verificationCodes.remove(email);
            return "redirect:/login?successMessage=Registration successful! Please log in.";
        }

        model.addAttribute("errorMessage", "Invalid verification code.");
        return "basic/insertCode";
    }


    @GetMapping("/forgotPassword")
    public String forgotPasswordPage() {
        return "basic/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, HttpSession session, Model model) {
        User user = userService.findByEmail(email);

        if (user == null || "ADMIN".equalsIgnoreCase(user.getRole())) {
            model.addAttribute("errorMessage", "Email not registered.");
            return "basic/forgotPassword"; // Use forward here, not redirect
        }

        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        verificationCodes.put(email, verificationCode);
        // Set verification code in session with expiry
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("verificationCodeExpiry", System.currentTimeMillis() + 120000);
        emailService.sendVerificationEmail(email, verificationCode);
        System.out.println("Verification code sent to " + email + ": " + verificationCode);

        session.setAttribute("email", email);
        return "redirect:/verifyCode";
    }


    @GetMapping("/verifyCode")
    public String verifyCodePage(Model model, HttpSession session) {
        String errorMessage = (String) session.getAttribute("errorMessage");

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "basic/verifyCode";
    }

    @PostMapping("/verifyCode")
    public String processVerifyCode(@RequestParam("code") String code, HttpSession session, Model model) {
        // When verifying the code

        Long expiryTime = (Long) session.getAttribute("verificationCodeExpiry");
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            // Code has expired
            session.removeAttribute("verificationCode");
            session.removeAttribute("verificationCodeExpiry");
            model.addAttribute("errorMessage", "Invalid verification code's time expired" + ".");
            return "basic/verifyCode";
            // Handle expired case
        } else {

            // Code is still valid
            String storedCode = (String) session.getAttribute("verificationCode");
            if (storedCode != null && storedCode.equals(code)) {
                return "redirect:/resetPassword";
            }
        }

        String email = (String) session.getAttribute("email");
        String storedCode = verificationCodes.get(email);

        if (storedCode != null && storedCode.equals(code)) {

            return "redirect:/resetPassword";
        }

        model.addAttribute("errorMessage", "Invalid verification code.");
        return "basic/verifyCode";
    }


    @GetMapping("/resetPassword")
    public String resetPasswordPage(Model model, HttpSession session) {
        String errorMessage = (String) session.getAttribute("errorMessage");


        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "basic/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "basic/resetPassword";
        }

        if (newPassword.length() < 8) {
            model.addAttribute("errorMessage", "Password must be at least 8 characters long.");
            return "basic/resetPassword";
        }

        String email = (String) session.getAttribute("email");
        User user = userService.findByEmail(email);


        if (user != null) {
            user.setPassword(newPassword);
            userService.save(user);
            if (user.getId().equals(emailService.extractUsernameFromEmail(email))) {
                session.setAttribute("loggedInApp", user);
                return "redirect:/loginApp?successMessage=Password reset successfully.";
            } else {
                session.setAttribute("loggedInUser", user);
                return "redirect:/login?successMessage=Password reset successfully.";
            }

        }

        model.addAttribute("errorMessage", "Failed to reset password.");
        return "basic/resetPassword";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        //return "redirect:/login?successMessage=You have been logged out.";
        return "redirect:/home";
    }


    @GetMapping("/logout")
    public String logout1(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }


}
