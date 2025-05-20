package com.sdlc.pro.mymbstu.controller;


import com.sdlc.pro.mymbstu.model.AdmissionApplication;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.AdmissionApplicationRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RequestMapping("/admission/management")
public class AdmisisonManagement1Controller {


    @Autowired
    private AdmissionApplicationRepository applicationRepository;



    @GetMapping
    public String managementPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model, HttpSession session) {

        User user=(User)session.getAttribute("loggedInUser");
        if(user==null){
            user=(User)session.getAttribute("loggedInApp");
            if(user==null){
                return "redirect:/login";
            }
        }
        model.addAttribute("user",user);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<AdmissionApplication> applications;

        if (name != null && !name.isEmpty()) {
            applications = applicationRepository.findByStatusAndApplicantNameContainingIgnoreCase(
                    "PENDING", name, pageable);
        } else {
            applications = applicationRepository.findAllByStatusOrderByIdDesc("PENDING", pageable);
        }

        model.addAttribute("applications", applications);
        return "/admission/management";
    }

    @PostMapping("/approve/{id}")
    public String approveApplication(@PathVariable Long id, Model model, HttpSession session) {

        User  user=(User)session.getAttribute("loggedInUser");
        if(user==null){
            user=(User)session.getAttribute("loggedInApp");
            if(user==null){
                return "redirect:/login";
            }
        } model.addAttribute("user",user);
        AdmissionApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
        application.setStatus("APPROVED");
        applicationRepository.save(application);
        return "redirect:/admission/management";
    }

    @PostMapping("/cancel/{id}")
    public String cancelApplication(@PathVariable Long id,
                                    Model model,HttpSession session) {

        User  user=(User)session.getAttribute("loggedInUser");
        if(user==null){
            user=(User)session.getAttribute("loggedInApp");
            if(user==null){
                return "redirect:/login";
            }
        }
        model.addAttribute("user",user);
        AdmissionApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
        application.setStatus("CANCEL");
        applicationRepository.save(application);
        return "redirect:/admission/management";
    }

    @GetMapping("/approve")
    public String approvedApplicationsPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model,HttpSession session) {

        User  user=(User)session.getAttribute("loggedInUser");
        if(user==null){
            user=(User)session.getAttribute("loggedInApp");
            if(user==null){
                return "redirect:/login";
            }
        }
        model.addAttribute("user",user);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<AdmissionApplication> applications;

        if (name != null && !name.isEmpty()) {
            applications = applicationRepository.findByStatusAndApplicantNameContainingIgnoreCase(
                    "APPROVED", name, pageable);
        } else {
            applications = applicationRepository.findAllByStatusOrderByIdDesc("APPROVED", pageable);
        }

        model.addAttribute("applications", applications);
        return "admission/approve-list";
    }

    @GetMapping("/approve/download")
    public void downloadApprovedApplications(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=approved_applications.csv");

        List<AdmissionApplication> applications = applicationRepository.findByStatus("APPROVED");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Applicant Name,Application User,Father/Guardian,Status");

            for (AdmissionApplication app : applications) {
                writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\"",
                        app.getApplicantName(),
                        app.getAppUser(),
                        app.getFatherGuardian(),
                        app.getStatus()));
            }
        }
    }

    @GetMapping("/download-all")
    public void downloadAllApplications(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=applications.csv");

        List<AdmissionApplication> applications = applicationRepository.findByStatus("PENDING");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Applicant Name,Father/Guardian,Phone,SSC Result,HSC Result,Graduation Result");

            for (AdmissionApplication app : applications) {
                writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        app.getApplicantName(),
                        app.getFatherGuardian(),
                        app.getPhone(),
                        app.getSscResult(),
                        app.getHscResult(),
                        app.getGradResult()));
            }
        }
    }
}
