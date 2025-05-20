package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.LostCertificate;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.repository.LostCertificateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LostCertificateService {

    private final LostCertificateRepository lostCertificateRepository;
    private final FileStorageService fileStorageService;

    public LostCertificateService(LostCertificateRepository lostCertificateRepository,
                                  FileStorageService fileStorageService) {
        this.lostCertificateRepository = lostCertificateRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public LostCertificate createLostCertificate(User user, MultipartFile gdPhoto) throws IOException {
        // Check if user already has a pending request
        if (lostCertificateRepository.existsByUserAndStatus(user, "PENDING")) {
            throw new IllegalStateException("You already have a pending lost certificate request");
        }

        // Store the GD photo
        String gdPhotoPath = fileStorageService.storeFile(gdPhoto);

        // Create and save the request
        LostCertificate request = new LostCertificate(user, gdPhotoPath);
        return lostCertificateRepository.save(request);
    }
}