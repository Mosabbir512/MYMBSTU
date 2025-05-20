package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.config.CredentialGenerator;
import com.sdlc.pro.mymbstu.dto.NoticeDTO;
import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.AdmissionApplicationRepository;
import com.sdlc.pro.mymbstu.service.EmailService;
import com.sdlc.pro.mymbstu.service.FileStorageService;
import com.sdlc.pro.mymbstu.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admission")
public class
AdmissionController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private AdmissionApplicationRepository admissionRepository;

    @GetMapping()
    public String admissionPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            user = (User) session.getAttribute("loggedInApp");
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        model.addAttribute("user", user);

        List<Notice> notices = noticeService.getActiveNoticesByType(NoticeType.PaymentAdm);
        boolean showPaymentOption = false;

        if (!notices.isEmpty()) {
            Notice notice = notices.get(0);
            Date endDate = notice.getEndDate();
            Date currentDate = new Date();
            showPaymentOption = currentDate.before(endDate);
        }

        model.addAttribute("showPaymentOption", showPaymentOption);
        return "admission/adm";
    }
    @GetMapping("/notice")
    public String notice(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user==null){
            user=(User) session.getAttribute("loggedInApp");
            if(user==null)return "redirect:/login";
        }
        model.addAttribute("user",user);

        List<Notice> admNotices = noticeService.getActiveNoticesByType(NoticeType.ADMISSION);
        admNotices.addAll(noticeService.getActiveNoticesByType(NoticeType.VivaAdm));
        admNotices.addAll(noticeService.getActiveNoticesByType(NoticeType.PaymentAdm));

        model.addAttribute("admNotices", admNotices);
        model.addAttribute("isAdmNoticePage", true);
        return "admission/notice";
    }

    @GetMapping("/applyAdmission")
    public String showApplicationForm(Model model,HttpSession session) {
        User user=(User) session.getAttribute("loggedInUser");
        if(user==null){
            user=(User) session.getAttribute("loggedInApp");
            if(user==null){
                model.addAttribute("error","first login here");
                return "redirect:/loginApp";
            }
        }
        model.addAttribute("user",user);

        model.addAttribute("application", new AdmissionApplication());
        List<Notice> notices = noticeService.getActiveNoticesByType(NoticeType.ADMISSION);

        if (!notices.isEmpty()) {
            Notice notice = notices.get(0);
            Date noticeDate = notice.getStartDate(); // Using java.util.Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(noticeDate);
            calendar.add(Calendar.DAY_OF_MONTH, 10);
            Date applicationEnd = calendar.getTime();

            model.addAttribute("noticeStarted", noticeDate);
            model.addAttribute("applicationEnd", applicationEnd);

            // Check if current date is within application window
            Date now = new Date();
            boolean canApply = now.after(noticeDate) && now.before(applicationEnd);


            AdmissionApplication application=admissionRepository.findByUser(user.getId());
            if(application!=null){
                model.addAttribute("errorMessage","You already applied ");
                model.addAttribute("canApply", false);
            }
            else {
                model.addAttribute("noticeStarted", noticeDate);
                model.addAttribute("applicationEnd", applicationEnd);
                model.addAttribute("canApply", canApply);
            }
        }

        else {
            model.addAttribute("canApply", false);
        }
        return "admission/applyAdmission";
    }

    @PostMapping("/applyAdmission")
    public String submitApplication(
            @Valid @ModelAttribute("application") AdmissionApplication application,
            BindingResult result,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("paymentPhoto") MultipartFile paymentPhoto,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null){
            user = (User) session.getAttribute("loggedInApp");
            if(user == null){
                return "redirect:/loginApp";
            }
        }model.addAttribute("user",user);


        if (result.hasErrors()) {
            return "admission/applyAdmission";
        }

        // Handle student photo upload
        if (!photo.isEmpty()) {
            try {
                String uploadDir = session.getServletContext().getRealPath("/uploads/student-photos/");
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                photo.transferTo(filePath.toFile());
                application.setPhotofile(fileName);
                application.setPhotoPath("/uploads/student-photos/" + fileName);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload photo: " + e.getMessage());
                return "admission/applyAdmission";
            }
        }

        // Handle payment photo upload
        if (!paymentPhoto.isEmpty()) {
            try {
                String uploadDir = session.getServletContext().getRealPath("/uploads/payment-receipts/");
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = System.currentTimeMillis() + "_" + paymentPhoto.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                paymentPhoto.transferTo(filePath.toFile());
                application.setPaymentPhotofile(fileName);
                application.setPaymentPhotoPath("/uploads/payment-receipts/" + fileName);
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload payment receipt: " + e.getMessage());
                return "admission/applyAdmission";
            }
        }

        try {
            // Generate credentials
            int nextSequence = admissionRepository.countBySscYear(application.getSscYear()) + 1;
            String appUser = CredentialGenerator.generateAppUser("IT", nextSequence);
            String appPass = CredentialGenerator.generateRandomPassword(8);

            // Set credentials
            application.setAppUser(appUser);
            application.setAppPass(appPass);
            application.setUser(user.getId());

            // Save application
            AdmissionApplication savedApplication = admissionRepository.save(application);

            // Send email with credentials
            sendCredentialsEmail(savedApplication.getApplicantName(),
                    savedApplication.getAppUser(),
                    savedApplication.getAppPass(),
                    user.getEmail());

            return "redirect:/admission/response?success";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to submit application: " + e.getMessage());
            return "admission/applyAdmission";
        }
    }



    @GetMapping("/response")
    public String responsePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if (user == null) {
                return "redirect:/loginApp";
            }
        }
        model.addAttribute("user", user);
        return "admission/response";
    }

    @GetMapping("/payment")
    public String paymentAdm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if (user == null) {
                return "redirect:/loginApp";
            }
        }
        model.addAttribute("user",user);
        model.addAttribute("userId", user.getId());
        return "admission/payment";
    }

    @PostMapping("/payment")
    public String processPayment(
            @RequestParam("appUser") String appUser,
            @RequestParam("appPass") String appPass,
            @RequestParam("paymentPhoto") MultipartFile paymentPhoto,
            HttpSession session,
            Model model) {

        try {
            // Get logged in user
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) {
                user = (User) session.getAttribute("loggedInApp");
                if (user == null) {
                    return "redirect:/loginApp";
                }
            }
            model.addAttribute("user",user);

            // Find application by credentials
            AdmissionApplication application = admissionRepository.findByAppUserAndAppPass(appUser, appPass);
            if (application == null || !application.getUser().equals(user.getId())) {
                model.addAttribute("error", "Invalid application credentials");
                return "admission/payment";
            }

            // Handle payment photo upload
            if (!paymentPhoto.isEmpty()) {
                String uploadDir = session.getServletContext().getRealPath("/uploads/payment-receipts/");
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = System.currentTimeMillis() + "_" + paymentPhoto.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                paymentPhoto.transferTo(filePath.toFile());

                // Update application with payment info
                application.setPaymentPhotofile(fileName);
                application.setPaymentPhotoPath("/uploads/payment-receipts/" + fileName);
                application.setPaymentConfirm("PAID");
                admissionRepository.save(application);

                return "redirect:/admission/payment/success";
            } else {
                model.addAttribute("error", "Payment proof is required");
                return "admission/payment";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Payment processing failed: " + e.getMessage());
            return "admission/payment";
        }
    }

    @GetMapping("/payment/success")
    public String paymentSuccess() {
        return "admission/payment-success";
    }
    @GetMapping("/provost1/noticegen")
    public String noticegen(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user==null){
            model.addAttribute("error","For notice Generate must be Admin user ");
            return "basic/login";
        }
        model.addAttribute("notice", new NoticeDTO());
        model.addAttribute("user", user);

        return "admission/noticegen";
    }


    @PostMapping("/provost1/noticegen")
    public String generateNotice(@ModelAttribute NoticeDTO noticeDTO,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam("file") MultipartFile file,
                                 Model model,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to create notices");
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        try {
            Notice notice = new Notice();
            notice.setTitle(noticeDTO.getTitle());
            notice.setDescription(noticeDTO.getDescription());
            notice.setNoticeType(noticeDTO.getNoticeType());

            // Convert LocalDate to java.util.Date
            if (noticeDTO.getStartDate() != null) {
                notice.setStartDate(java.sql.Date.valueOf(noticeDTO.getStartDate()));
            }

            if (noticeDTO.getEndDate() != null) {
                notice.setEndDate(java.sql.Date.valueOf(noticeDTO.getEndDate()));
            }

            if (!file.isEmpty()) {
                String fileName = fileStorageService.storeFile(file);
                notice.setFilePath(fileName);
                notice.setFileName(file.getOriginalFilename());
            }

            notice.setCreatedBy(user.getUsername());
            notice.setPublishDate(new Date()); // Current date as publish date

            noticeService.saveNotice(notice);

            redirectAttributes.addFlashAttribute("success", "Notice created successfully!");
            return "redirect:/admission/provost1/noticegen";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create notice: " + e.getMessage());
            return "redirect:/admission/provost1/noticegen";
        }
    }


    private void sendCredentialsEmail(String applicantName, String appUser, String appPass, String email) {
        try {
            String subject = "Your MBSTU Admission Credentials";
            String content = String.format(
                    "Dear %s,<br><br>" +
                            "Your admission application has been received. Here are your credentials: " +
                            "Username:%s " +
                            " Password:   %s " +
                            "Please keep these credentials safe for future reference." +
                            "Best regards," +
                            "MBSTU Admission Office",
                    applicantName, appUser, appPass);


            // emailService.sendEmail(email, subject, content);
            emailService.sendSimpleMessage(email,subject,content);

            System.out.println("Email would be sent to: " + email);
            System.out.println("Subject: " + subject);
            System.out.println("Content: " + content);

        } catch (Exception e) {
            System.err.println("Failed to send credentials email: " + e.getMessage());
        }
    }


}