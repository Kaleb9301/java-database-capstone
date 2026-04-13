package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorService {

// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.


// 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.

//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return List.of();
        }

        Doctor doctor = doctorOpt.get();
        List<String> availableSlots = doctor.getAvailableTimes();
        if (availableSlots == null || availableSlots.isEmpty()) {
            return List.of();
        }

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
            doctorId,
            date.atStartOfDay(),
            date.atTime(LocalTime.MAX)
        );
        var bookedSlots = new java.util.HashSet<>(
            appointments.stream()
                .map(a -> a.getAppointmentTime() != null ? a.getAppointmentTime().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) : null)
                .filter(java.util.Objects::nonNull)
                .toList()
        );

        return availableSlots.stream()
            .filter(slot -> slot != null && !bookedSlots.contains(slot))
            .sorted(java.util.Comparator.comparing(java.time.LocalTime::parse))
            .toList();
    }


// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.
    public int saveDoctor(Doctor doctor) {
        try {
            var existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
            if (existingDoctor != null) {
                return -1; // Conflict: Doctor with the same email already exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }


// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.
    public int updateDoctor(com.project.back_end.models.Doctor doctor) {
        try {
            var existingDoctor = doctorRepository.findById(doctor.getId());
            if (existingDoctor.isEmpty()) {
                return -1; // Doctor not found
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 
    @Transactional
    public List<com.project.back_end.models.Doctor> getDoctors() {
        return doctorRepository.findAll();
    }
// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.
    public int deleteDoctor(Long doctorId) {
        try {
            var existingDoctor = doctorRepository.findById(doctorId);
            if (existingDoctor.isEmpty()) {
                return -1; // Doctor not found
            }
            doctorRepository.delete(existingDoctor.get());
            appointmentRepository.deleteAllByDoctorId(doctorId);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }

// 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.
    public ResponseEntity<Map<String, String>> validateDoctor(String email, String password) {
        try {
            var doctor = doctorRepository.findByEmail(email);
            if (doctor != null) {
                if (doctor.getPassword().equals(password)) {
                    String token = tokenService.generateToken(doctor.getEmail());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Doctor not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.
    @Transactional
    public Map<String, Object> findDoctorByName(String name) {
        var doctors = doctorRepository.findByNameLike("%" + name + "%");
        return Map.of("doctors", doctors);
    }


// 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.
    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        var doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filteredDoctors);
    }

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        // Implement filtering logic based on time (AM/PM)
        return doctors.stream().filter(doctor -> {
            return doctor.getAvailableTimes().stream().anyMatch(slot -> {
                if (amOrPm.equalsIgnoreCase("AM")) {
                    return LocalTime.parse(slot).isBefore(LocalTime.NOON);
                } else if (amOrPm.equalsIgnoreCase("PM")) {
                    return LocalTime.parse(slot).isAfter(LocalTime.NOON) || LocalTime.parse(slot).equals(LocalTime.NOON);
                }
                return false;
            });
        }).toList();
    }



// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).
    @Transactional
    public Map<String, Object> filterDoctorsByNameAndTime(String name, String amOrPm) {
        var doctors = doctorRepository.findByNameLike("%" + name + "%");
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filteredDoctors);
    }

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.
    @Transactional
    public Map<String, Object> filterDoctorsByNameAndSpecialty(String name, String specialty) {
        var doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return Map.of("doctors", doctors);
    }


// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).
    @Transactional
    public Map<String, Object> filterDoctorsBySpecialtyAndTime(String specialty, String amOrPm) {
        var doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filteredDoctors);
    }

// 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.
    @Transactional
    public Map<String, Object> filterDoctorsBySpecialty(String specialty) {
        var doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return Map.of("doctors", doctors);
    }

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.
    @Transactional
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        var doctors = doctorRepository.findAll();
        List<Doctor> filteredDoctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", filteredDoctors);
    }

    public Map<String, Object> getAllDoctors() {
        var doctors = doctorRepository.findAll();
        return Map.of("doctors", doctors);
    }

   
}
