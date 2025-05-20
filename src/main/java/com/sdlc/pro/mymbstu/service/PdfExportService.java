package com.sdlc.pro.mymbstu.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sdlc.pro.mymbstu.model.AdmissionApplication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfExportService {

    // For pending applications (detailed view)
    public void addPendingApplication(Document document, AdmissionApplication application) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10);

        addHeaderRow(table, "Application #" + application.getId(), headerFont, 2);
        addDetailRow(table, "Applicant Name:", application.getApplicantName(), labelFont, valueFont);
        addDetailRow(table, "Father/Guardian:", application.getFatherGuardian(), labelFont, valueFont);
        addDetailRow(table, "Phone:", application.getPhone(), labelFont, valueFont);
        addDetailRow(table, "Address:", application.getPresentAddress(), labelFont, valueFont);
        addDetailRow(table, "SSC Result:", application.getSscResult(), labelFont, valueFont);
        addDetailRow(table, "HSC Result:", application.getHscResult(), labelFont, valueFont);
        addDetailRow(table, "Username:", application.getAppUser(), labelFont, valueFont);
        addDetailRow(table, "Status:", application.getStatus(), labelFont, valueFont);
//        addDetailRow(table, "Submission Date:", application.getSubmissionDate().toString(), labelFont, valueFont);
        document.add(table);
        document.newPage();
    }

    // For approved/canceled applications (compact view)
    public void addApprovedCanceledApplications(Document document, List<AdmissionApplication> applications) throws DocumentException {
        // Create a table with 4 columns for the compact view
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Header row
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        addCompactHeaderCell(table, "Applicant Name", headerFont);
        addCompactHeaderCell(table, "Father Name", headerFont);
        addCompactHeaderCell(table, "Username", headerFont);
        addCompactHeaderCell(table, "Status", headerFont);

        // Data rows
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 9);
        for (AdmissionApplication app : applications) {
            addCompactCell(table, app.getApplicantName(), valueFont);
            addCompactCell(table, app.getFatherGuardian(), valueFont);
            addCompactCell(table, app.getAppUser(), valueFont);
            addCompactCell(table, app.getStatus(), valueFont);
        }

        document.add(table);
    }

    // Helper methods
    private void addHeaderRow(PdfPTable table, String text, Font font, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(70, 130, 180)); // Steel blue
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addDetailRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new BaseColor(240, 240, 240));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private void addCompactHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new BaseColor(70, 130, 180)); // Steel blue
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCompactCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "N/A", font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }
}