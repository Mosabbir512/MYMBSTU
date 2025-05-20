package com.sdlc.pro.mymbstu.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sdlc.pro.mymbstu.model.AdmissionApplication;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.AdmissionApplicationRepository;
import com.sdlc.pro.mymbstu.service.PdfExportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admission/management")
public class AdmissionManagementController {

    private final AdmissionApplicationRepository applicationRepository;
    private final PdfExportService pdfExportService;

    public AdmissionManagementController(AdmissionApplicationRepository applicationRepository,
                                         PdfExportService pdfExportService) {
        this.applicationRepository = applicationRepository;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping
    public String viewApplications(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null) {
                return "redirect:/login";
            }
        }
        model.addAttribute("user", user);

        Page<AdmissionApplication> applications = applicationRepository.findAllByStatusOrderByIdDesc(
                "PENDING", PageRequest.of(page, size));

        model.addAttribute("applications", applications);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applications.getTotalPages());
        model.addAttribute("viewType", "pending");

        return "admission/management";
    }

    @GetMapping("/approved")
    public String viewApprovedApplications(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return "redirect:/login";
        }
        model.addAttribute("user", user);

        Page<AdmissionApplication> applications = applicationRepository.findAllByStatusOrderByIdDesc(
                "APPROVED", PageRequest.of(page, size));

        model.addAttribute("applications", applications);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applications.getTotalPages());
        model.addAttribute("viewType", "approved");

        return "admission/management";
    }

    @GetMapping("/canceled")
    public String viewCanceledApplications(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return "redirect:/login";
        }
        model.addAttribute("user", user);

        Page<AdmissionApplication> applications = applicationRepository.findAllByStatusOrderByIdDesc(
                "CANCELED", PageRequest.of(page, size));

        model.addAttribute("applications", applications);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applications.getTotalPages());
        model.addAttribute("viewType", "canceled");

        return "admission/management";
    }

    @PostMapping("/approve/{id}")
    public String approveApplication(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return "redirect:/login";
        }
        model.addAttribute("user", user);
        applicationRepository.findById(id).ifPresent(application -> {
            application.setStatus("APPROVED");
            applicationRepository.save(application);
        });
        return "redirect:/admission/management";
    }

    @PostMapping("/cancel/{id}")
    public String cancelApplication(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return "redirect:/login";
        }
        model.addAttribute("user", user);
        applicationRepository.findById(id).ifPresent(application -> {
            application.setStatus("CANCELED");
            applicationRepository.save(application);
        });
        return "redirect:/admission/management";
    }

    @GetMapping("/export/all-pdf")
    public ResponseEntity<StreamingResponseBody> exportAllApplicationsAsPdf(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_admission_applications.pdf")
                .body(outputStream -> {
                    try {
                        Document document = new Document(PageSize.A4.rotate()); // Landscape for better layout
                        PdfWriter.getInstance(document, outputStream);
                        document.open();

                        int page = 0;
                        int batchSize = 20;
                        Page<AdmissionApplication> applicationPage;

                        do {
                            applicationPage = applicationRepository.findAllByOrderByIdDesc(
                                    PageRequest.of(page, batchSize));

                            for (AdmissionApplication application : applicationPage.getContent()) {
                                try {
                                    pdfExportService.addPendingApplication(document, application);
                                    document.newPage();
                                } catch (Exception e) {
                                    System.err.println("Error processing application " + application.getId() + ": " + e.getMessage());
                                }
                            }
                            page++;
                        } while (applicationPage.hasNext());

                        document.close();
                    } catch (DocumentException e) {
                        throw new RuntimeException("Failed to generate PDF", e);
                    }
                });
    }

    @GetMapping("/export/approved-pdf")
    public ResponseEntity<StreamingResponseBody> exportApprovedApplicationsAsPdf(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null) return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=approved_applications.pdf")
                .body(outputStream -> {
                    try {
                        Document document = new Document(PageSize.A4);
                        PdfWriter.getInstance(document, outputStream);
                        document.open();

                        int page = 0;
                        int batchSize = 100; // Larger batch size since we're showing many per page
                        Page<AdmissionApplication> applicationPage;
                        List<AdmissionApplication> allApplications = new ArrayList<>();

                        // Collect all applications first
                        do {
                            applicationPage = applicationRepository.findAllByStatusOrderByIdDesc(
                                    "APPROVED", PageRequest.of(page, batchSize));
                            allApplications.addAll(applicationPage.getContent());
                            page++;
                        } while (applicationPage.hasNext());

                        // Add all to PDF in one table
                        pdfExportService.addApprovedCanceledApplications(document, allApplications);

                        document.close();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate PDF", e);
                    }
                });
    }

    @GetMapping("/export/canceled-pdf")
    public ResponseEntity<StreamingResponseBody> exportCanceledApplicationsAsPdf(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null) return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=canceled_applications.pdf")
                .body(outputStream -> {
                    try {
                        Document document = new Document(PageSize.A4);
                        PdfWriter.getInstance(document, outputStream);
                        document.open();

                        int page = 0;
                        int batchSize = 100;
                        Page<AdmissionApplication> applicationPage;
                        List<AdmissionApplication> allApplications = new ArrayList<>();

                        do {
                            applicationPage = applicationRepository.findAllByStatusOrderByIdDesc(
                                    "CANCELED", PageRequest.of(page, batchSize));
                            allApplications.addAll(applicationPage.getContent());
                            page++;
                        } while (applicationPage.hasNext());

                        pdfExportService.addApprovedCanceledApplications(document, allApplications);

                        document.close();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate PDF", e);
                    }
                });
    }

    @GetMapping("/view-photo/{type}/{id}")
    public ResponseEntity<?> viewPhoto(@PathVariable String type, @PathVariable Long id, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("loggedInApp");
            if(user == null)
                return ResponseEntity.status(403).build();
        }
        return applicationRepository.findById(id)
                .map(application -> {
                    try {
                        String photoPath = type.equals("passport") ? application.getPhotoPath() : application.getPaymentPhotoPath();
                        if (photoPath == null || photoPath.isEmpty()) {
                            return ResponseEntity.notFound().build();
                        }

                        Path path = Paths.get(session.getServletContext().getRealPath("/") + photoPath);
                        if (!Files.exists(path)) {
                            return ResponseEntity.notFound().build();
                        }

                        byte[] photoBytes = Files.readAllBytes(path);
                        return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(photoBytes);
                    } catch (IOException e) {
                        return ResponseEntity.status(500).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}