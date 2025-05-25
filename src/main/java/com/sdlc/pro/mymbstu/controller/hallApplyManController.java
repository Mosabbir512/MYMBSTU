package com.sdlc.pro.mymbstu.controller;


import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import com.sdlc.pro.mymbstu.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class hallApplyManController {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SeatService seatService;

    @Autowired
    private AttestationRepository attestationRepository;
    @Autowired
    private SeatRequestService seatRequestService;
    @Autowired
    private AttestationService attestationService;
    @Autowired
    private LostCertificateService lostCertificateService;

    @Autowired
    private IDCardService idCardService;
    @Autowired
    private IDCardApplicationRepository idCardApplicationRepository;
    @Autowired
    private LostCertificateRepository lostCertificateRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private HallReplyController hallReplyController;

    // Display the seat management page
    @GetMapping("/about_hall_man")
    public String showSeatManagementPage(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String search,
                                         HttpSession session,
                                         Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to see seat information.");
            return "basic/login";
        }

        if (user.getId().equalsIgnoreCase("admin_1") || user.getId().equalsIgnoreCase("admin_2")) {
            return "redirect:/diary";
        }

        // Check if admin
        if ("provost".equalsIgnoreCase(user.getRole()) ||
                "staf".equalsIgnoreCase(user.getRole())) {
            String hallName=user.getHallName();
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<SeatRequest> seatRequests = seatRequestService.searchPendingRequests(search, hallName, pageable);
            List<IDCardApplication> idCardApplications = idCardApplicationRepository.findByStatusAndHallName("PENDING",hallName);
            List<Attestation> attestationApplications = attestationRepository.findByStatus("PENDING");
            List<LostCertificate> lostCertificateApplications = lostCertificateRepository.findByStatus("PENDING");
            List<Token> recentTokens = tokenRepository.findByVerifiedFalseOrderByMealDateDesc();
            model.addAttribute("recentTokens", recentTokens);
            model.addAttribute("idCardApplications", idCardApplications);
            model.addAttribute("lostCertificateApplications", lostCertificateApplications);
            model.addAttribute("attestationApplications", attestationApplications);
            model.addAttribute("seatRequests", seatRequests);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", seatRequests.getTotalPages());
            model.addAttribute("currentSearch", search);
        }

        Seat seat = seatService.findSeatById(user.getId())
                .orElse(new Seat(user, "", "Pending", "Pending", "Pending", "Pending", "Pending"));

        model.addAttribute("user", user);
        model.addAttribute("seat", seat);

        return "seat/seatManagement";
    }

    @GetMapping("/seat-allocation")
    public String showSeatAllocation(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Find seat - properly handle Optional
        Optional<Seat> seatOptional = seatService.findSeatById(user.getId());

        if (!seatOptional.isPresent()) {
            // No seat found - you might want to redirect to a different page
            model.addAttribute("errorMessage", "No seat allocation found for your account.");
            return "seat/seat-allocation"; // Note: removed leading slash
        }

        System.out.println(seatOptional.toString());
        // Seat found - add to model
        List<Notice> notices=noticeService.getActiveNoticesByTypeAndHall(NoticeType.PAYMENT,user.getHallName());
        model.addAttribute("seat", seatOptional.get());
        if(notices.size()!=0){
            Notice noticePayment=notices.get(0);
                Date date = noticePayment.getEndDate();
                if (date != null && date.before(new Date())) { // endDate < currentDate
                    model.addAttribute("notice", "yes");
                }
                else {
                    model.addAttribute("notice", "no");
                }
        }
        else {
            model.addAttribute("notice", "no");
        }
        model.addAttribute("user", user);
        return "seat/seat-allocation";
    }

    @GetMapping("/applySeat")
    public String requestSeatPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to see seat information.");
            return "basic/login";
        }
        model.addAttribute("user", user);
        String hallName=user.getHallName();

        List<Notice> notices = noticeService.getActiveNoticesByTypeAndHall(NoticeType.SeatAllotment,hallName);
        if (!notices.isEmpty()) {
            Notice notice = notices.get(0);
            Date noticeDate = notice.getStartDate(); // Using java.util.Date

            model.addAttribute("noticeStarted", notice.getStartDate());
            model.addAttribute("applicationEnd", notice.getEndDate());
            // Check if current date is within application window
            Date now = new Date();
            boolean canApply = now.after(noticeDate) && now.before(notice.getEndDate());
            model.addAttribute("canApply", canApply);

            Optional<Seat> seat = seatService.findSeatById(user.getId());
            if (seat.isPresent()) {
                if ("pending".equalsIgnoreCase(seat.get().getRoomNumber())) {
                    model.addAttribute("errorMessage", "You have already applied for a seat.");
                    return "seat/applicationSuccess";
                } else if (!"no allocate".equalsIgnoreCase(seat.get().getRoomNumber())) {
                    model.addAttribute("errorMessage",
                            "You already have a seat. Your Hall: " + seat.get().getHallName() +
                                    ", Room No: " + seat.get().getRoomNumber() +
                                    ", Seat No: " + seat.get().getSeatNumber());
                    return "seat/applicationSuccess";
                }
            }
        } else {
            model.addAttribute("canApply", false);
        }
        return "seat/applySeat";
    }

    @Autowired
    private SeatRequestRepository seatRequestRepository;

    @Transactional
    @PostMapping("/applySeat")
    public String handleRequestSeat(
            @RequestParam String fatherName,
            @RequestParam String fatherOccupation,
            @RequestParam String fatherPhone,
            @RequestParam String motherName,
            @RequestParam String motherOccupation,
            @RequestParam String familyIncome,
            @RequestParam String permanentAddress,
            @RequestParam(required = false) String presentAddress,
            @RequestParam String lastSemesterResult,
            @RequestParam String currentCGPA,
            @RequestParam String hallName,
            @RequestParam String reason,
            @RequestParam("lastSemesterGradeSheet") MultipartFile lastSemesterGradeSheet,
            @RequestParam("picture") MultipartFile picture,
            HttpSession session, Model model) throws IOException {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to see seat information.");
            return "basic/login";
        }


        // Check if user already has a seat
        Optional<Seat> seat = seatService.findSeatById(user.getId());

        if (seat.isPresent()) {
            if ("pending".equalsIgnoreCase(seat.get().getRoomNumber())) {
                model.addAttribute("errorMessage", "You have already applied for a seat.");
                return "seat/applicationSuccess";
            } else if (!"no allocate".equalsIgnoreCase(seat.get().getRoomNumber())) {
                model.addAttribute("errorMessage",
                        "You already have a seat. Your Hall: " + seat.get().getHallName() +
                                ", Room No: " + seat.get().getRoomNumber() +
                                ", Seat No: " + seat.get().getSeatNumber());
                return "seat/applicationSuccess";
            }
        }

        try {
            // Process file uploads
            String gradeSheetPath = fileStorageService.storeFile(lastSemesterGradeSheet);
            String picturePath = fileStorageService.storeFile(picture);

            // Create seat request
            SeatRequest seatRequest = new SeatRequest();
            seatRequest.setUser(user);
            seatRequest.setFatherName(fatherName);
            seatRequest.setFatherOccupation(fatherOccupation);
            seatRequest.setFatherPhone(fatherPhone);
            seatRequest.setMotherName(motherName);
            seatRequest.setMotherOccupation(motherOccupation);
            seatRequest.setFamilyIncome(familyIncome);
            seatRequest.setPermanentAddress(permanentAddress);
            seatRequest.setPresentAddress(presentAddress != null ? presentAddress : permanentAddress);
            seatRequest.setLastSemesterResult(lastSemesterResult);
            seatRequest.setCurrentCGPA(currentCGPA);
            seatRequest.setHallName(hallName);
            seatRequest.setReason(reason);
            seatRequest.setLastSemesterGradeSheet(gradeSheetPath);
            seatRequest.setPicture(picturePath);
            seatRequestRepository.save(seatRequest);
            //seatRequestService.createSeatRequest(seatRequest);

            // Update seat status to pending if exists
            if (seat.isPresent() && "no allocate".equalsIgnoreCase(seat.get().getRoomNumber())) {
                seat.get().setSeatNumber("PENDING");
                seat.get().setRoomNumber("PENDING");
                seat.get().setAllocationDate("PENDING");
                seat.get().setPayment("NULL");
                seat.get().setUser(user);
                seatService.saveSeat(seat.get());
            }

            String successMessage = String.format("Dear %s, your application for a Seat has been submitted successfully. Our team will process your request shortly.", user.getUsername());
            model.addAttribute("successMessage", successMessage);
            return "seat/applicationSuccess";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error processing your application: " + e.getMessage());
            return "seat/applicationSuccess";
        }
    }


    @GetMapping("/applicationSuccess")
    public String applicationSuccessPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        // System.out.println(user);
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to see seat information.");
            return "basic/login"; // Redirect to login if user isn't logged in
        }
        return "seat/applicationSuccess"; // Renders the success page
    }

    @GetMapping("/applyID")
    public String requestIDCardPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to apply for an ID card.");
            return "basic/login";
        }
        model.addAttribute("user", user);
        return "seat/applyID";
    }

    @PostMapping("/applyID")
    public String handleIDCardRequest(
            @RequestParam String idCardType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam String fatherName,
            @RequestParam String motherName,
            @RequestParam String permanentAddress,
            @RequestParam("photo") MultipartFile photo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must log in to apply for an ID card.");
            return "redirect:/login";
        }

        try {
            IDCardApplication application = idCardService.createIDCardApplication(
                    user, idCardType, dob, fatherName, motherName, permanentAddress, photo
            );

            String successMessage = String.format(
                    "Dear %s, your application for a %s ID card has been submitted successfully. " +
                            "Application ID: %d. Our team will process your request shortly.",
                    user.getUsername(),
                    idCardType.equalsIgnoreCase("new") ? "new" : "replacement",
                    application.getUser().getUsername()
            );

            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/applicationSuccess";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/applyID";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to process your photo. Please try again.");
            return "redirect:/applyID";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred. Please try again later.");
            return "redirect:/applyID";
        }
    }

    @GetMapping("/applyAttestation")
    public String requestAttestationPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to apply for an attestation.");
            return "basic/login";
        }

        Seat seat = seatService.findSeatById(user.getId())
                .orElse(new Seat(user, "", "Pending", "Pending", "Pending", "Pending", "Pending"));

        model.addAttribute("user", user);
        model.addAttribute("seat", seat);
        return "seat/applyAttestation";
    }

    @PostMapping("/applyAttestation")
    public String handleAttestationRequest(
            @RequestParam String reason,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must log in to apply for an attestation.");
            return "redirect:/login";
        }

        try {
            Seat seat = seatService.findSeatById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("No seat allocation found for your account"));

            Attestation attestation = attestationService.createAttestation(user, seat, reason);

            String successMessage = String.format(
                    "Dear %s, your attestation request has been submitted successfully. " +
                            "Request ID: %d. Our team will process your request shortly.",
                    user.getUsername(),
                    attestation.getId()
            );

            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/applicationSuccess";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/applyAttestation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred. Please try again later.");
            return "redirect:/applyAttestation";
        }
    }

    @GetMapping("/applyLostCertificate")
    public String requestLostCertificatePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to apply for a lost certificate.");
            return "basic/login";
        }
        model.addAttribute("user", user);
        return "seat/applyLostCertificate";
    }

    @PostMapping("/applyLostCertificate")
    public String handleLostCertificateRequest(
            @RequestParam("gdPhoto") MultipartFile gdPhoto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must log in to apply for a lost certificate.");
            return "redirect:/login";
        }

        try {
            LostCertificate request = lostCertificateService.createLostCertificate(user, gdPhoto);

            String successMessage = String.format(
                    "Dear %s, your lost certificate application has been submitted successfully. " +
                            "Request ID: %d. Our team will process your request shortly.",
                    user.getUsername(),
                    request.getId()
            );

            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/applicationSuccess";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to process your GD photo. Please try again.");
            return "redirect:/applyLostCertificate";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/applyLostCertificate";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "An unexpected error occurred. Please try again later.");
            return "redirect:/applyLostCertificate";
        }
    }

    @GetMapping("/complain")
    public String showComplaintForm(Model model,HttpSession session) {
        User user=(User)session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        if (!model.containsAttribute("complaint")) {
            model.addAttribute("complaint", new Complaint());
        }
        return "seat/complain";
    }

    @PostMapping("/complain")
    public String submitComplaint(@ModelAttribute Complaint complaint,
                                  HttpSession session,Model model,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }



        complaint.setComplainant(user.getUsername());
        complaint.setComplainDate(LocalDate.now());
        complaintRepository.save(complaint);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/complain";
    }
}
