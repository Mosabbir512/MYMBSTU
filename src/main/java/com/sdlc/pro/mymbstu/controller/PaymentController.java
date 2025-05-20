package com.sdlc.pro.mymbstu.controller;

import java.text.SimpleDateFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import com.sdlc.pro.mymbstu.model.Seat;
import com.sdlc.pro.mymbstu.model.SeatRequest;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.model.PaymentDetails;
import com.sdlc.pro.mymbstu.repository.SeatRepository;
import com.sdlc.pro.mymbstu.repository.PaymentDetailsRepository;
import com.sdlc.pro.mymbstu.repository.SeatRequestRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;

@Controller
public class PaymentController {

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatRequestRepository seatRequestRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/confirm-payment")
    public String payment(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }

        Seat seat = seatRepository.findSeatById(user.getId());
        model.addAttribute("seat", seat);
        model.addAttribute("user", user);
        return "seat/payment-confirmation";
    }

    @PostMapping("/confirm-payment")
    public String processPayment(
            @RequestParam String studentName,
            @RequestParam String studentId,
            @RequestParam String userId,
            @RequestParam("paymentReceipt") MultipartFile paymentReceipt,
            HttpSession httpSession,
            Model model) throws IOException {

        User user = (User) httpSession.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = paymentReceipt.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            String filePath = uploadDir + "/" + uniqueFilename;

            // Save file to disk
            byte[] bytes = paymentReceipt.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);

            // Save payment details to database
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setStudentName(studentName);
            paymentDetails.setStudentId(studentId);
            paymentDetails.setPaymentDate(new Date()); // Set current date/time
            paymentDetails.setUserId(userId);
            paymentDetails.setHallName(user.getHallName());
            paymentDetails.setPaymentReceiptImagePath(filePath);

            Seat seat=seatRepository.findSeatById(user.getId());
            seat.setPayment("CONFIRMED");
            seatRepository.save(seat);
            paymentDetailsRepository.save(paymentDetails);

            return "redirect:/payment-success";
        } catch (Exception e) {
            model.addAttribute("error", "Error processing payment: " + e.getMessage());
            return "seat/payment-confirmation";
        }
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }
        return "seat/payment-success";
    }


    @GetMapping("/provost/paymentConfirmapplicant")
    public String paymentConfirmed(Model model,
                                   @RequestParam(required = false) String searchQuery,
                                   HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }
        String hallName = user.getHallName();
        List<PaymentDetails> payments;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Search within the provost's hall only
            payments = paymentDetailsRepository.findByHallNameAndStudentNameContainingOrHallNameAndStudentIdContaining(hallName, searchQuery, hallName, searchQuery);
        } else {
            // Get all payments for the provost's hall only
            payments = paymentDetailsRepository.findByHallName(hallName);
        }
        model.addAttribute("user",user);
        model.addAttribute("payments", payments);
        model.addAttribute("searchQuery", searchQuery);
        return "seat/confirmedApplicant";
    }



    @GetMapping("/provost/downloadReceipt/{id}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable Long id,HttpSession session) throws IOException {
        PaymentDetails payment = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        User user=(User)session.getAttribute("loggedInUser");

        Path filePath = Paths.get(payment.getPaymentReceiptImagePath());
        Resource resource = (Resource) new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }




    @GetMapping("/provost/generateAllPdf")
    public void downloadPaymentConfirmedPdf(
            @RequestParam String hallName,  // Accept hallName as parameter
            HttpServletResponse response) throws IOException, DocumentException {

        // Set response properties
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"payment-confirmed.pdf\"");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        // Fetch data using the hallName parameter
        List<PaymentDetails> payments = paymentDetailsRepository.findByHallName(hallName);

        try {
            // Rest of your PDF generation code remains the same...
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title with hallName
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Payment Confirmed List - " + hallName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Date
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph date = new Paragraph("Generated on: " + LocalDate.now(), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(10);
            document.add(date);

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 1.5f, 2f, 1.5f});

            // Table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            addTableHeader(table, "Student Name", headerFont);
            addTableHeader(table, "Student ID", headerFont);
            addTableHeader(table, "Department", headerFont);
            addTableHeader(table, "Payment Date", headerFont);

            // Table content
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");



            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            for (PaymentDetails payment : payments) {
                table.addCell(createCell(payment.getStudentName(), cellFont));
                table.addCell(createCell(payment.getStudentId(), cellFont));
                table.addCell(createCell(payment.getDepartment(), cellFont));
                table.addCell(createCell(sdf.format(payment.getPaymentDate()), cellFont));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating PDF");
            e.printStackTrace();
        }
    }
    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        return cell;
    }

    @GetMapping("/provost/unpaymentList")
    public String unpayment(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        List<SeatRequest> seatRequests = seatRequestRepository.findByStatusAndHallName("APPROVED",user.getHallName());
        List<Seat> seats = new ArrayList<>();

        for(SeatRequest seatRequest : seatRequests) {
            String id = seatRequest.getUser().getId();
            Boolean yes = seatRepository.isPaymentConfirmed(id);

            if(!yes) {
                Seat seat = seatRepository.findByUserId(id);
                if (seat != null) {
                    System.out.println(seat.getId() + " " + seat.getSessionId() + " " + seat.getRoomNumber() + " " +
                            seat.getSeatNumber() + " " + seat.getHallName() + " unpayment");
                    seats.add(seat);
                }
            }
        }

        model.addAttribute("seats", seats);
        return "seat/unpaymentList";
    }

    @PostMapping("/seats/cancel/{seatId}")
    public ResponseEntity<String> cancelSeat(@PathVariable String seatId) {
        return ResponseEntity.ok("null");
    }

}