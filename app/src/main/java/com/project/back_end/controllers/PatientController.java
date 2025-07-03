package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Service1 service;

    // 2. Constructor injection for dependencies
    public PatientController(PatientService patientService, Service1 service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 3. Get patient details by token
    @GetMapping("/details/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }
        return ResponseEntity.ok(patientService.getPatientDetails(token));
    }

    // 4. Create new patient (registration)
    @PostMapping("/register")
    public ResponseEntity<?> createPatient(@RequestBody @Valid Patient patient) {
        // Check if patient already exists (email/phone uniqueness)
        boolean validPatient = service.validatePatient(patient.getEmail(), patient.getPhone());
        if (!validPatient) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Patient with email or phone already exists"));
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Patient registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to register patient"));
        }
    }

    // 5. Patient login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login loginRequest) {
        Map<String, Object> response = service.validatePatientLogin(loginRequest.getEmail(), loginRequest.getPassword());
        int status = (int) response.getOrDefault("status", 500);
        return ResponseEntity.status(status).body(response);
    }

    // 6. Get appointments for patient
    @GetMapping("/appointments/{patientId}/{token}/{role}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String token,
            @PathVariable String role) {

        if (!service.validateToken(token, role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return ResponseEntity.ok(patientService.getPatientAppointment(patientId));
    }

    // 7. Filter patient appointments by condition and doctor name
    @GetMapping("/appointments/filter")
    public ResponseEntity<?> filterPatientAppointment(
            @RequestParam String condition,
            @RequestParam(required = false) String name,
            @RequestParam String token) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        Map<String, Object> filteredAppointments = service.filterPatient(condition, name, token);
        return ResponseEntity.ok(filteredAppointments);
    }
}
