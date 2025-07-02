package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service1;
import com.project.back_end.services.TokenService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service1 service;
    private final TokenService tokenService;
    

    // 2. Constructor injection
    public AppointmentController(AppointmentService appointmentService, Service1 service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 3. Get appointments by date and patient name for a doctor
    @GetMapping("/doctor/{token}/{date}/{patientName}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @PathVariable String date,
            @PathVariable String patientName) {
        
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        LocalDate appointmentDate;
        try {
            appointmentDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid date format. Use yyyy-MM-dd"));
        }

        Long doctorId = tokenService.extractDoctorId(token);
        LocalDateTime start = appointmentDate.atStartOfDay();
        LocalDateTime end = appointmentDate.atTime(LocalTime.MAX);

        List<?> appointments = appointmentService.getAppointments(doctorId, start, end, patientName);
        return ResponseEntity.ok(appointments);
    }

    // 4. Book a new appointment
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(
            @RequestBody @Valid Appointment appointment,
            @PathVariable String token) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        int result = appointmentService.bookAppointment(appointment);
        switch (result) {
            case 1:
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Appointment booked successfully"));
            case 0:
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Appointment slot is already taken"));
            case -1:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid doctor ID"));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "An error occurred"));
        }
    }

    // 5. Update an existing appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(
            @RequestBody @Valid Appointment appointment,
            @PathVariable String token) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        String result = appointmentService.updateAppointment(appointment);
        if ("success".equalsIgnoreCase(result)) {
            return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", result));
        }
    }

    // 6. Cancel an appointment
    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        String result = appointmentService.cancelAppointment(appointmentId, token);
        if ("success".equalsIgnoreCase(result)) {
            return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", result));
        }
    }
}
