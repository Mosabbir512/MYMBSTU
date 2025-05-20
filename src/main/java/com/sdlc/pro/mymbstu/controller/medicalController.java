package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.model.*;
import com.sdlc.pro.mymbstu.repository.DoctorListRepository;
import com.sdlc.pro.mymbstu.repository.MedicineRepository;
import com.sdlc.pro.mymbstu.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


import com.sdlc.pro.mymbstu.model.Patient;
import com.sdlc.pro.mymbstu.model.User;
import com.sdlc.pro.mymbstu.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class medicalController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/medical")
    public String showMedicalManagementPage(@RequestParam(value = "success", required = false) String success,
                                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to get medical services");
            return "basic/login"; // Redirect to login if user isn't logged in
        }

        // Add a new Patient object to the model for the form
        model.addAttribute("patient", new Patient());
        model.addAttribute("user", user);

        // Add the success message to the model if it exists
        if (success != null) {
            model.addAttribute("successMessage", "Your appointment has been successfully received. Please wait for our response.");
        }

        return "medical/medicalHome";
    }
    // Handle form submission
    @PostMapping("/book-appointment")
    public String bookAppointment(@ModelAttribute("patient") Patient patient,
                                  HttpSession session,
                                  Model model) {
        // Get logged-in user from session
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("errorMessage", "You must log in to book an appointment.");
            return "basic/login"; // Redirect to login if user isn't logged in
        }

        // Set user details in the patient object
        patient.setUserId(user.getId().toString());
        patient.setUsername(user.getUsername());
        patient.setResponse(null); // Initialize response as null

        // Save appointment
        patientService.savePatient(patient);

        // Redirect to a success page or show a success message
        return "medical/homeAfterSubmission";
    }


    @GetMapping("/medical/dashboard")
    public String doctorDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        // This will now return appointments sorted with oldest first
        List<Patient> relevantAppointments = patientService.getRelevantAppointmentsForDoctor(user.getUsername());

        model.addAttribute("appointments", relevantAppointments);
        model.addAttribute("doctorName", user.getUsername());
        return "medical/doctorDashboard";
    }
    //    @PostMapping("/save-response")
//    public String saveResponse(
//            @RequestParam("appointmentId") Long id,
//            @RequestParam("response") String response
//    ) {
//        // If you’re no longer building a separate dosagePattern/days,
//        // just persist the “response” text exactly as received.
//        patientService.saveResponse(id, response);
//        return "redirect:/medical/dashboard?success=Response+submitted";
//    }
    @PostMapping("/save-response")
    public String saveResponse(
            @RequestParam("appointmentId") Long id,
            @RequestParam("response") String response
    ) {
        // Save the response in DB
        patientService.saveResponse(id, response);

        // Retrieve the patient
        Patient patient = patientService.findById(id);
        if (patient != null) {
            // Get the user (patient) from their user ID
            User patientUser = userService.findById(patient.getUserId());
            if (patientUser != null && patientUser.getEmail() != null) {
                // Send email
                String subject = "Your Medical Prescription from MBSTU Medical Center";
                String body = "Dear " + patient.getUsername() + ",\n\nHere is your prescription:\n\n" + response;
                emailService.sendEmail(patientUser.getEmail(), subject, body);
            }
        }

        return "redirect:/medical/dashboard?success=Response+submitted";
    }



    private String buildFinalResponse(String response, String dosage, int days) {
        StringBuilder sb = new StringBuilder();
        sb.append(response);

        if(!dosage.isEmpty()) {
            sb.append("\n\nMedication Schedule:");
            sb.append("\n- Dosage Times: ").append(dosage.replace("1", "Morning")
                    .replace("2", "Noon")
                    .replace("3", "Night"));
            sb.append("\n- Duration: ").append(days).append(" days");
        }
        return sb.toString();
    }
    @GetMapping("/view-responses")
    public String viewAppointmentHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Patient> appointments = patientService.getUserHistory(user.getId().toString());
        model.addAttribute("appointments", appointments);
        return "medical/appointmentHistory";
    }
    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping("/api/medicines")
    @ResponseBody
    public List<Medicine> searchMedicines(@RequestParam String term) {
        return medicineRepository.findByNameContainingIgnoreCase(term);
    }

    @Autowired
    private DoctorListRepository doctorListRepository;

    @GetMapping("/api/doctors")
    @ResponseBody
    public List<DoctorList> searchDoctors(@RequestParam String term) {
        return doctorListRepository.findByNameContainingIgnoreCase(term);
    }

    @GetMapping("/api/clinics")
    @ResponseBody
    public List<String> searchClinics(@RequestParam String term) {
        return doctorListRepository.findDistinctLocationsContaining(term);
    }



}
