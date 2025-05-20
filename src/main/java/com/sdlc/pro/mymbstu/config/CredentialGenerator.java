package com.sdlc.pro.mymbstu.config;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class CredentialGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    public static String generateAppUser(String departmentCode, int sequenceNumber) {
        // Format: DepartmentCode + Year + 3-digit sequence (e.g., IT24001)
        int currentYear = LocalDate.now().getYear() % 100; // Gets last 2 digits of year
        return String.format("%s%02d%03d", departmentCode, currentYear, sequenceNumber);
    }

    public static String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return password.toString();
    }
}