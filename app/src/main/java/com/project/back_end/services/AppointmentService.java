package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.TokenService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;
 
    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            TokenService tokenService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    // 4. Book Appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 5. Update Appointment
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updated) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment existing = optional.get();
        if (!existing.getPatient().getId().equals(updated.getPatient().getId()))
            return "Unauthorized update attempt";

        List<Appointment> conflicts = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                updated.getDoctor().getId(),
                updated.getAppointmentTime().minusMinutes(59),
                updated.getAppointmentTime().plusMinutes(59)
        );
        if (!conflicts.isEmpty()) return "Doctor unavailable at the selected time";

        updated.setId(appointmentId);
        appointmentRepository.save(updated);
        return "Appointment updated successfully";
    }

    // 6. Cancel Appointment
    @Transactional
    public String cancelAppointment(Long appointmentId, String token) {
        Long patientId = tokenService.extractPatientId(token); // Assume this method exists and is correct

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment appointment = optional.get();
        if (!appointment.getPatient().getId().equals(patientId))
            return "Unauthorized cancel attempt";

        appointmentRepository.deleteById(appointmentId);
        return "success";
    }


    // 7. Get Appointments by Doctor and Date with optional patient name filter
    @Transactional
    public List<Appointment> getAppointments(Long doctorId, LocalDateTime start, LocalDateTime end, String patientName) {
        if (patientName == null || patientName.isBlank()) {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        } else {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, start, end);
        }
    }

    // 8. Change Appointment Status
    @Transactional
    public String changeStatus(Long appointmentId, int status) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment appointment = optional.get();
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return "Status updated successfully";
    }
}
