package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service1; // shared service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor") // configurable base path + "doctor"
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service1 service; // shared utility service for token validation and filtering

    // 3. Check Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        boolean valid = service.validateToken(user, token);
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token."));
        }

        boolean available = doctorService.checkAvailability(doctorId, date);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 4. Get All Doctors
    @GetMapping
    public ResponseEntity<Map<String, List<Doctor>>> getDoctor() {
        List<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok(Map.of("doctors", doctors));
    }

    // 5. Register New Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @Validated @RequestBody Doctor doctor,
            @PathVariable String token) {

        boolean isAdmin = service.validateToken("admin", token);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized: Admin token required"));
        }

        boolean exists = doctorService.existsByEmail(doctor.getEmail());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Doctor already exists"));
        }

        doctorService.addDoctor(doctor);
        return ResponseEntity.ok(Map.of("message", "Doctor registered successfully"));
    }

    // 6. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(@Validated @RequestBody Login login) {
        Map<String, Object> loginResponse = doctorService.login(login);
        if (loginResponse == null || !(Boolean) loginResponse.getOrDefault("success", false)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
        return ResponseEntity.ok(loginResponse);
    }

    // 7. Update Doctor Info
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @Validated @RequestBody Doctor doctor,
            @PathVariable String token) {

        boolean isAdmin = service.validateToken("admin", token);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized: Admin token required"));
        }

        if (!doctorService.existsById(doctor.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Doctor not found"));
        }

        doctorService.updateDoctor(doctor);
        return ResponseEntity.ok(Map.of("message", "Doctor updated successfully"));
    }

    // 8. Delete Doctor
    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        boolean isAdmin = service.validateToken("admin", token);
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized: Admin token required"));
        }

        if (!doctorService.existsById(doctorId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Doctor not found"));
        }

        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
    }

    // 9. Filter Doctors by name, time, and specialty
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, List<Doctor>>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        List<Doctor> filteredDoctors = service.filterDoctor(name, time, speciality);
        return ResponseEntity.ok(Map.of("doctors", filteredDoctors));
    }
}
