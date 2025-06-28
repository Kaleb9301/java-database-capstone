package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity  // @Entity annotation explained in your comment
public class Appointment {

    // 1. 'id' field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. 'doctor' field
    @ManyToOne
    @NotNull(message = "Doctor must not be null")
    private Doctor doctor;

    // 3. 'patient' field
    @ManyToOne
    @NotNull(message = "Patient must not be null")
    private Patient patient;

    // 4. 'appointmentTime' field
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    // 5. 'status' field
    @NotNull(message = "Status must not be null")
    private Integer status;  // 0 = scheduled, 1 = completed

    // 6. getEndTime method
    @Transient
    public LocalDateTime getEndTime() {
        if (appointmentTime != null) {
            return appointmentTime.plusHours(1);
        }
        return null;
    }

    // 7. getAppointmentDate method
    @Transient
    public LocalDate getAppointmentDate() {
        if (appointmentTime != null) {
            return appointmentTime.toLocalDate();
        }
        return null;
    }

    // 8. getAppointmentTimeOnly method
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        if (appointmentTime != null) {
            return appointmentTime.toLocalTime();
        }
        return null;
    }

    // 9. Constructors
    public Appointment() {}

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, Integer status) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // 10. Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
