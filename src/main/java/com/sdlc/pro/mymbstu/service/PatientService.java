// PatientService.java
package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.model.Patient;
import com.sdlc.pro.mymbstu.repository.PatientRepository;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }


    public List<Patient> getUserHistory(String userId) {
        List<Patient> appointments = patientRepository.findByUserId(userId);
        appointments.sort(Comparator.comparing(Patient::getAppointmentDate).reversed());
        return appointments;
    }
    // New methods for doctor dashboard
    public List<Patient> getAppointmentsByDoctorName(String doctorName) {
        return patientRepository.findByDoctorNameAndResponseIsNull(doctorName);
    }

    public void saveResponse(Long id, String response) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setResponse(response);
        patientRepository.save(patient);
    }
    public List<Patient> getUnrepliedAppointments() {
        return patientRepository.findByResponseIsNullOrderByAppointmentDateDesc();
    }
    public List<Patient> getAllAppointments() {
        return patientRepository.findAllByOrderByAppointmentDateDesc();
    }

    public List<Patient> getUnrepliedAppointmentsForDoctor(String doctorName) {
        return patientRepository.findByDoctorNameAndResponseIsNullOrderByAppointmentDateDesc(doctorName);
    }

    public List<Patient> getRelevantAppointmentsForDoctor(String doctorName) {
        return patientRepository.findRelevantAppointmentsForDoctor(doctorName);
    }
    public Patient findById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }


}