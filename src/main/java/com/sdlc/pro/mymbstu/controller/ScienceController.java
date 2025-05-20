package com.sdlc.pro.mymbstu.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ScienceController {

    // Endpoint to serve the science page
    @GetMapping("/science")
    public String sciencePage() {
        return "science"; // Returns science.html (Thymeleaf template)
    }

    // Endpoint to view the PDF in a new tab
    @GetMapping("/view/science.pdf")
    public ResponseEntity<Resource> viewPdf() {
        try {
            // Path to the PDF file
            Path filePath = Paths.get("src/main/resources/static/science.pdf").toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Serve the PDF inline (to display in the browser)
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found: science.pdf");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while reading the file", e);
        }
    }

    // Endpoint to download the PDF directly
    @GetMapping("/download/science.pdf")
    public ResponseEntity<Resource> downloadPdf() {
        try {
            // Path to the PDF file
            Path filePath = Paths.get("src/main/resources/static/science.pdf").toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Serve the PDF as an attachment (to force download)
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found: science.pdf");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while reading the file", e);
        }
    }
}