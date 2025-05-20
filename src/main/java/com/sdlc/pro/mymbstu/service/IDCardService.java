package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.IDCardApplication;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.IDCardApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class IDCardService {

    private final IDCardApplicationRepository applicationRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    public IDCardService(IDCardApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public IDCardApplication createIDCardApplication(User user, String idCardType,
                                                     LocalDate dob, String fatherName,
                                                     String motherName, String permanentAddress,
                                                     MultipartFile photo) throws IOException {

        // Check if user already has a pending application
        if (applicationRepository.existsByUserAndStatus(user, "PENDING")) {
            throw new IllegalStateException("You already have a pending ID card application");
        }

        // Generate unique filename for the photo
        String originalFilename = photo.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save the photo to the filesystem
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(photo.getInputStream(), filePath);

        // Create and save the application
        IDCardApplication application = new IDCardApplication(
                user, idCardType, dob, fatherName, motherName, permanentAddress,
                filePath.toString(), user.getHallName()
        );

        return applicationRepository.save(application);
    }
}