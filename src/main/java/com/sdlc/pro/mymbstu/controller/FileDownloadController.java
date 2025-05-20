package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.SeatRequest;
import com.sdlc.pro.mymbstu.repository.SeatRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;


@Controller
public class FileDownloadController {

    @Autowired
    private SeatRequestRepository seatRequestRepository;
    private final Path fileStorageLocation = Paths.get("uploads/notices").toAbsolutePath().normalize();

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not download file: " + fileName, e);
        }
    }




    @GetMapping("/admin/seat-requests/generate-pdf")
    public ResponseEntity<InputStreamResource> generatePendingSeatApplicationsPDF() {
        // Get all pending seat requests
        List<SeatRequest> pendingRequests = seatRequestRepository.findByStatus("PENDING");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Use landscape orientation for better table display

            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Pending Seat Applications", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Create table with 8 columns
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths
            float[] columnWidths = {1.5f, 2f, 2f, 2f, 3f, 2f, 2f, 2f};
            table.setWidths(columnWidths);

            // Add table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            addTableHeader(table, "Student ID", headerFont);
            addTableHeader(table, "Name", headerFont);
            addTableHeader(table, "Father's Income", headerFont);
            addTableHeader(table, "Address", headerFont);
            addTableHeader(table, "Reason", headerFont);
            addTableHeader(table, "Hall Name", headerFont);
            addTableHeader(table, "Applied On", headerFont);
            addTableHeader(table, "Viva Status", headerFont);

            // Add table rows
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (SeatRequest request : pendingRequests) {
                addTableCell(table, request.getUser().getId(), cellFont);
                addTableCell(table, request.getUser().getUsername(), cellFont);
                addTableCell(table, request.getFamilyIncome(), cellFont);
                addTableCell(table, request.getPermanentAddress(), cellFont);
                addTableCell(table, request.getReason(), cellFont);
                addTableCell(table, request.getHallName(), cellFont);
                addTableCell(table, request.getCreatedAt().toString(), cellFont);
                addTableCell(table, request.getVivaStatus() ? "Completed" : "Pending", cellFont);
            }

            document.add(table);
            document.close();

            // Prepare response
            ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=pending_seat_applications.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setPadding(5);
        table.addCell(cell);
    }
}