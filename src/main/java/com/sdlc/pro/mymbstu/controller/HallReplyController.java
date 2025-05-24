package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.dto.NoticeDTO;
import com.sdlc.pro.mymbstu.dto.SeatAssignmentDTO;
import com.sdlc.pro.mymbstu.dto.SeatInfoDTO;
import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.*;
import com.sdlc.pro.mymbstu.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.model.SeatRequest;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.SeatRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class HallReplyController {
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private JananetaAbdulMannanHallRepository jananetaAbdulMannanHallRepository;
    @Autowired
    private SheikhRaselHallRepository sheikhRaselHallRepository;
    @Autowired
    private BangabandhuSheikhMujiburRahmanHallRepository bangabandhuSheikhMujiburRahmanHallRepository;
    @Autowired
    private ShahidZiaurRahmanHallRepository shahidZiaurRahmanHallRepository;
    @Autowired
    private AlemaKhatunBhashaniHallRepository alemaKhatunBhashaniHallRepository;
    @Autowired
    private ShaheedJananiJahanaraImamHallRepository shaheedJananiJahanaraImamHallRepository;
    @Autowired
    private BangamataHallRepository bangamataHallRepository;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PdfExportService pdfExportService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private AttestationRepository attestationRepository;
    @Autowired
    private LostCertificateRepository lostCertificateRepository;
    @Autowired
    private IDCardApplicationRepository idCardApplicationRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private SeatRequestService seatRequestService;

    @Autowired
    private HallService hallService;


    @GetMapping("/notice")
    public String showHallNotices(Model model, HttpSession session, HttpServletRequest servletRequest) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to see seat information.");
            return "basic/login";
        }
        String hallName = user.getHallName();
        List<Notice> hallNotices = noticeService.getActiveNoticesByTypeAndHall(NoticeType.SeatViva, hallName);
        hallNotices.addAll(noticeService.getActiveNoticesByTypeAndHall(NoticeType.SeatAllotment, hallName));
        hallNotices.addAll(noticeService.getActiveNoticesByTypeAndHall(NoticeType.PAYMENT, hallName));


        hallNotices.forEach(notice -> {
            if (notice.getFilePath() != null) {
                String filePath = notice.getFilePath();
                String separator = filePath.contains("/") ? "/" : "\\";
                String fileName = filePath.substring(filePath.lastIndexOf(separator) + 1);
                notice.setFileName(fileName);
            }
        });

        model.addAttribute("noticeSeat", hallNotices);
        model.addAttribute("isHallNoticePage", true);
        model.addAttribute("user", user);
        return "notices/noticeSeat";
    }

    @GetMapping("/download-notice/{noticeId}")
    public ResponseEntity<Resource> downloadNoticeFile(@PathVariable Long noticeId) {
        Notice notice = noticeService.getNoticeById(noticeId);

        if (notice == null || notice.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(notice.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String filename = notice.getFileName() != null ? notice.getFileName() : resource.getFilename();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + filename + "\"")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/notice/admin/noticegen")
    public String showNoticeGenerationForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("notice", new NoticeDTO());
        model.addAttribute("user", user);
        return "notices/noticegen";
    }

    @PostMapping("/notice/admin/noticegen")
    public String generateNotice(@ModelAttribute NoticeDTO noticeDTO,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes,
                                 Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        try {
            Notice notice = new Notice();
            notice.setTitle(noticeDTO.getTitle());
            notice.setDescription(noticeDTO.getDescription());
            notice.setNoticeType(noticeDTO.getNoticeType());
            if (noticeDTO.getStartDate() != null)
                notice.setStartDate(java.sql.Date.valueOf(noticeDTO.getStartDate()));
            notice.setEndDate(java.sql.Date.valueOf(noticeDTO.getEndDate()));
            notice.setHallName(user.getHallName()); // Set hall name from user

            if (!file.isEmpty()) {
                String fileName = fileStorageService.storeFile(file);
                notice.setFilePath(fileName);
                notice.setFileName(file.getOriginalFilename());
            }

            notice.setCreatedBy(user.getUsername());
            notice.setPublishDate(new Date());
            noticeService.saveNotice(notice);
            model.addAttribute("user", user);

            redirectAttributes.addFlashAttribute("success", "Notice created successfully!");
            return "redirect:/notice/admin/noticegen";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create notice: " + e.getMessage());
            return "redirect:/notice/admin/noticegen";
        }
    }

    @Autowired
    private SeatRequestRepository seatRequestRepository;

    @GetMapping("/provost/seat-approval")
    public String seatApprovalPage1(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String search,
                                    Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Pageable pageable = PageRequest.of(page - 1, size);
        String hallName = user.getHallName();
        Page<SeatRequest> seatRequests = seatRequestRepository.findPendingApprovalRequests(hallName, pageable);
        model.addAttribute("seatRequests", seatRequests);
        model.addAttribute("currentPage", page);
        model.addAttribute("user", user);
        model.addAttribute("totalPages", seatRequests.getTotalPages());
        model.addAttribute("currentSearch", search);

        return "seat/approval";
    }


    // API endpoint for toggling viva status
    @PostMapping("/provost/seat-requests/{id}/toggle-viva")
    @ResponseBody
    public ResponseEntity<?> toggleVivaStatus(@PathVariable Long id) {
        try {
            boolean newStatus = seatRequestService.toggleVivaStatus(id).getVivaStatus();
            return ResponseEntity.ok(Map.of("vivaStatus", newStatus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to toggle viva status"));
        }
    }

    @GetMapping("/api/hall/available-rooms")
    public ResponseEntity<?> getAvailableRooms(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must log in first to access this resource");
        }
        try {
            // Get the appropriate repository based on hall name
            JpaRepository<?, Long> repository = repository(user.getHallName());
            // Use reflection to call the common method
            Method method = repository.getClass().getMethod("findDistinctRoomNumbersWithAvailableSeats");
            @SuppressWarnings("unchecked")
            List<String> rooms = (List<String>) method.invoke(repository);

            return ResponseEntity.ok(rooms);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching room data");
        }
    }


    @GetMapping("/api/hall/available-seats")
    public ResponseEntity<List<SeatInfoDTO>> getAvailableSeatsInRoom(@RequestParam String room, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        String hallName = user.getHallName();

        try {
            JpaRepository<?, Long> repository = repository(hallName);
            List<?> seats = new ArrayList<>();
            if (hallName.equals("Sheikh Rasel Hall")) {
                seats = ((SheikhRaselHallRepository) repository).findByRoomNumberAndStatusFalse(room);
            } else if (hallName.equals("Jananeta Abdul Mannan Hall")) {
                seats = ((JananetaAbdulMannanHallRepository) repository).findByRoomNumberAndStatusFalse(room);
            } else if (hallName.equals("Bangabandhu Sheikh Mujibur Rahman Hall")) {
                seats = ((BangabandhuSheikhMujiburRahmanHallRepository) repository).findByRoomNumberAndStatusFalse(room);
            } else {

            }

            List<SeatInfoDTO> seatInfo = seats.stream()
                    .map(seat -> {
                        if (seat instanceof SheikhRaselHall) {
                            SheikhRaselHall s = (SheikhRaselHall) seat;
                            return new SeatInfoDTO(s.getSeatNumber(), s.isStatus());
                        } else if (seat instanceof JananetaAbdulMannanHall) {
                            JananetaAbdulMannanHall j = (JananetaAbdulMannanHall) seat;
                            return new SeatInfoDTO(j.getSeatNumber(), j.isStatus());
                        } else if (seat instanceof BangabandhuSheikhMujiburRahmanHall) {
                            BangabandhuSheikhMujiburRahmanHall b = (BangabandhuSheikhMujiburRahmanHall) seat;
                            return new SeatInfoDTO(b.getSeatNumber(), b.isStatus());
                        } else {
                            JananetaAbdulMannanHall j = (JananetaAbdulMannanHall) seat;
                            return new SeatInfoDTO(j.getSeatNumber(), j.isStatus());
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(seatInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/api/seat-requests/{requestId}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId) {
        try {
            seatRequestService.approveRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/seat-requests/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId) {
        try {
            seatRequestService.rejectRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/seat-requests/{requestId}/assign")
    public ResponseEntity<?> assignSeat(
            @PathVariable Long requestId,
            @RequestBody SeatAssignmentDTO assignment, HttpSession session) {
        try {
            hallService.assignSeat(requestId, assignment.getRoomNumber(), assignment.getSeatNumber(), session);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping("/provost/seat-cancellation")
    public String seatCancellationPage(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) String search,
                                       Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Pageable pageable = PageRequest.of(page - 1, size);
//      Page<SeatRequest> cancellationRequests = seatRequestService.getCancellationRequests(search, pageable);
        Page<SeatRequest> cancellationRequests = seatRequestService.getPendingRequests(pageable);

        model.addAttribute("cancellationRequests", cancellationRequests);
        model.addAttribute("currentPage", page);
        model.addAttribute("user", user);
        model.addAttribute("totalPages", cancellationRequests.getTotalPages());
        model.addAttribute("currentSearch", search);
        model.addAttribute("hall", user.getHallName());


        return "seat/adminHallShow";
    }

//    @GetMapping("/api/hall/provost/seat-cancellation")
//    public String hall(Model model, HttpSession session) {
//        return "seat/adminHallShow";
//    }


    // ID Card Approval
    @PostMapping("/admin/id-card/approve/{id}")
    public String approveIdCard(@PathVariable Long id) {
        IDCardApplication application = idCardApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID Card Application ID: " + id));

        application.setStatus("APPROVED");
        idCardApplicationRepository.save(application);

        return "redirect:/about_hall_man?scrollToAdmin=true";
    }

    // Attestation Approval
    @PostMapping("/admin/attestation/approve/{id}")
    public String approveAttestation(@PathVariable Long id) {
        Attestation application = attestationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Attestation Application ID: " + id));

        application.setStatus("APPROVED");
        attestationRepository.save(application);

        return "redirect:/about_hall_man?scrollToAdmin=true";
    }


    // Lost Certificate Approval
    @PostMapping("/admin/lost-certificate/approve/{id}")
    public String approveLostCertificate(@PathVariable Long id) {
        LostCertificate application = lostCertificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Lost Certificate Application ID: " + id));

        application.setStatus("APPROVED");
        lostCertificateRepository.save(application);

        return "redirect:/about_hall_man?scrollToAdmin=true";
    }


    @GetMapping("/provost/allottees")
    public String allottees(HttpSession session, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {

        // Get logged in user (provost)
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        String hallName = user.getHallName();
        // Get all approved seat requests with pagination
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<SeatRequest> approvedRequests = seatRequestRepository.findByStatusAndUser_HallName("APPROVED", hallName, pageable);
        //String uId=approvedRequests.getContent().get(0).getUser().getId();
        List<SeatRequest> content = approvedRequests.getContent();
        Map<Long, Seat> seatMap = new HashMap<>();

        for (SeatRequest request : content) {

            Seat seat = seatRepository.findByUserId(request.getUser().getId());
            if (seat != null) {

                seatMap.put(request.getId(), seat);
            }
        }

        model.addAttribute("requests", content);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", approvedRequests.getTotalPages());
        model.addAttribute("totalItems", approvedRequests.getTotalElements());

        return "seat/allotees";
    }

    @GetMapping("/provost/allottees/download")
    public void downloadAllotteesPdf(
            HttpServletResponse response,
            HttpSession session,
            @RequestParam String hallName) throws IOException, DocumentException {

        // Use hallName parameter instead of getting from session
        List<SeatRequest> approvedRequests = seatRequestRepository.findByStatusAndHallName("APPROVED", hallName);
        // Get seat information for all approved users
        Map<Long, Seat> seatMap = new HashMap<>();
        for (SeatRequest request : approvedRequests) {
            Seat seat = seatRepository.findByUserId(request.getUser().getId());
            if (seat != null) {
                seatMap.put(request.getId(), seat);
            }
        }

        // Create PDF document
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Allottees List", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Date
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph date = new Paragraph("Generated on: " + LocalDate.now().toString(), dateFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        date.setSpacingAfter(10);
        document.add(date);

        // Create 7-column table (Name, ID, Department, Session, Room No, Seat No, Hall)
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {2f, 1.5f, 2f, 1.5f, 1.5f, 1.5f, 1.5f};
        table.setWidths(columnWidths);

        // Table Headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        addTableHeader(table, "Name", headerFont);
        addTableHeader(table, "ID", headerFont);
        addTableHeader(table, "Department", headerFont);
        addTableHeader(table, "Session", headerFont);
        addTableHeader(table, "Room No", headerFont);
        addTableHeader(table, "Seat No", headerFont);
        addTableHeader(table, "Hall", headerFont);

        // Table Rows
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (SeatRequest request : approvedRequests) {
            Seat seat = seatMap.get(request.getId());

            table.addCell(createCell(request.getUser().getUsername(), cellFont));
            table.addCell(createCell(request.getUser().getId(), cellFont));
            table.addCell(createCell(request.getUser().getDepartment(), cellFont));
            table.addCell(createCell(request.getUser().getSession2(), cellFont));
            table.addCell(createCell(seat != null ? seat.getRoomNumber() : "N/A", cellFont));
            table.addCell(createCell(seat != null ? seat.getSeatNumber() : "N/A", cellFont));
            table.addCell(createCell(seat != null ? seat.getHallName() : "N/A", cellFont));
        }

        document.add(table);
        document.close();
    }

    // Add table header cell
    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // cell.setBackgroundColor(BaseColor.LIGHT_GRAY);  // Optional: highlight header
        table.addCell(cell);
    }

    // Create a normal cell with wrapping and alignment
    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(false);  // Allow content wrapping
        return cell;
    }


    public <T> JpaRepository<T, Long> repository(String hallName) {
        if (hallName == null) {
            throw new IllegalArgumentException("Hall name cannot be null");
        }
        switch (hallName) {
            case "Sheikh Rasel Hall":
                return (JpaRepository<T, Long>) sheikhRaselHallRepository;
            case "Jananeta Abdul Mannan Hall":
                return (JpaRepository<T, Long>) jananetaAbdulMannanHallRepository;
            case "Bangabandhu Sheikh Mujibur Rahman Hall":
                return (JpaRepository<T, Long>) bangabandhuSheikhMujiburRahmanHallRepository;

            // other cases
            default:
                throw new IllegalArgumentException("Unknown hall: " + hallName);
        }
    }


    @GetMapping("/viewcomplain")
    public String viewComplaints(Model model,HttpSession session) {
        User user=(User)session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        model.addAttribute("complaints", complaintRepository.findAllByOrderByComplainDateDesc());
        return "seat/view_complaints";
    }

    // Toggle solved status POST mapping
    @PostMapping("/toggle-solved/{id}")
    public String toggleSolvedStatus(@PathVariable Long id,Model model,HttpSession session) {
        User user=(User)session.getAttribute("loggedInUser");
        model.addAttribute("user",user);
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid complaint Id:" + id));
        complaint.setSolved(!complaint.isSolved());
        complaintRepository.save(complaint);
        return "redirect:/viewcomplain";
    }


}