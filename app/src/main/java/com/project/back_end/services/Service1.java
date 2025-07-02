package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class Service1 {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public Service1(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /** 3. validateToken Method **/
    public ResponseEntity<String> validateToken(String token, String username) {
        try {
            if (token == null || token.isEmpty() || !tokenService.validateToken(token, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired token.");
            }
            return ResponseEntity.ok("Token is valid.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during token validation.");
        }
    }

    /** 4. validateAdmin Method **/
    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not found.");
            }
            if (!admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during admin validation.");
        }
    }

    /** 5. filterDoctor Method **/
    public List<Doctor> filterDoctor(String name, String specialty, String timePeriod) {
        // If no filters provided, return all doctors
        if ((name == null || name.isBlank()) &&
            (specialty == null || specialty.isBlank()) &&
            (timePeriod == null || timePeriod.isBlank())) {
            return doctorRepository.findAll();
        }

        // Filter based on combinations of name, specialty, and time period
        if (name != null && !name.isBlank() &&
            specialty != null && !specialty.isBlank() &&
            timePeriod != null && !timePeriod.isBlank()) {
            return doctorService.filterDoctorsByNameSpecialityAndTime(name, specialty, timePeriod);
        }

        if (name != null && !name.isBlank() && specialty != null && !specialty.isBlank()) {
            return doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        }

        if (name != null && !name.isBlank() && timePeriod != null && !timePeriod.isBlank()) {
            return doctorService.filterDoctorByNameAndTime(name, timePeriod);
        }

        if (specialty != null && !specialty.isBlank() && timePeriod != null && !timePeriod.isBlank()) {
            return doctorService.filterDoctorByTimeAndSpecialty(timePeriod, specialty);
        }

        if (name != null && !name.isBlank()) {
            return doctorService.findDoctorByName(name);
        }

        if (specialty != null && !specialty.isBlank()) {
            return doctorService.filterDoctorBySpecialty(specialty);
        }

        if (timePeriod != null && !timePeriod.isBlank()) {
            return doctorService.filterDoctorsByTime(timePeriod);
        }

        // Default: return all
        return doctorRepository.findAll();
    }

    /** 6. validateAppointment Method **/
    public int validateAppointment(Long doctorId, LocalDate appointmentDate, String appointmentTime) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return -1; // Doctor not found
        }
        Doctor doctor = doctorOpt.get();
        List<LocalTime> availableTimes = doctorService.getDoctorAvailability(doctorId, appointmentDate.toString());

        // Check if appointmentTime is in availableTimes
        if (availableTimes.contains(appointmentTime)) {
            return 1; // Valid appointment time
        } else {
            return 0; // Invalid appointment time
        }
    }

    /** 7. validatePatient Method **/
    public boolean validatePatient(String email, String phone) {
        Patient existingPatient = patientRepository.findByEmailOrPhone(email, phone);
        return existingPatient == null;
    }

    /** 8. validatePatientLogin Method **/
    public ResponseEntity<?> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found.");
            }
            if (!patient.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            String token = tokenService.generateToken(patient.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during patient login validation.");
        }
    }

    /** 9. filterPatient Method **/
    @Transactional
    public ResponseEntity<?> filterPatient(String token, String condition, String doctorName) {
        try {
            String email = tokenService.extractEmail(token);
            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
            }
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found.");
            }
            Long patientId = patient.getId();

            if ((condition == null || condition.isBlank()) && (doctorName == null || doctorName.isBlank())) {
                return ResponseEntity.ok(patientService.getPatientAppointment(patientId));
            }
            if ((condition != null && !condition.isBlank()) && (doctorName == null || doctorName.isBlank())) {
                return ResponseEntity.ok(patientService.filterByCondition(patientId, condition));
            }
            if ((condition == null || condition.isBlank()) && (doctorName != null && !doctorName.isBlank())) {
                return ResponseEntity.ok(patientService.filterByDoctor(patientId, doctorName));
            }
            if ((condition != null && !condition.isBlank()) && (doctorName != null && !doctorName.isBlank())) {
                return ResponseEntity.ok(patientService.filterByDoctorAndCondition(patientId, doctorName, condition));
            }

            return ResponseEntity.badRequest().body("Invalid filter parameters.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while filtering patient appointments.");
        }
    }
}
