package com.project.back_end.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

// 1. 'id' field:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. 'doctor' field:
    @ManyToOne
    @NotNull
    private Doctor doctor;

    // 3. 'patient' field:
    @ManyToOne
    @NotNull
    private Patient patient;

    @Future
    private LocalDateTime appointmentTime;

    @NotNull
    private int status; // 0 = scheduled, 1 = completed

  // 6. 'getEndTime' method:
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }


  // 7. 'getAppointmentDate' method:
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

  // 8. 'getAppointmentTimeOnly' method:
    private LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

  
//    - Type: private LocalTime
//    - Description:
//      - This method extracts only the time part from the appointmentTime field.
//      - It returns a LocalTime object representing just the time (without the date) of the scheduled appointment.

// 9. Constructor(s):
//    - A no-argument constructor is implicitly provided by JPA for entity creation.
//    - A parameterized constructor can be added as needed to initialize fields.
    public Appointment() {
    }

    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

// 10. Getters and Setters:
//    - Standard getter and setter methods are provided for accessing and modifying the fields: id, doctor, patient, appointmentTime, status, etc.
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

