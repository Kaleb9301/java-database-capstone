package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Appointment {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotNull
private Doctor doctor;

@ManyToOne
@NotNull
private Patient patient;

@NotNull
private LocalDateTime appointmentTime;

@NotNull
private int status;

// 6. 'getEndTime' method:
//    - Type: private LocalDateTime
//    - Description:
//      - This method is a transient field (not persisted in the database).
//      - It calculates the end time of the appointment by adding one hour to the start time (appointmentTime).
//      - It is used to get an estimated appointment end time for display purposes.
public LocalDateTime getEndTime() {
    return appointmentTime.plusHours(1);
}

// 7. 'getAppointmentDate' method:
//    - Type: private LocalDate
//    - Description:
//      - This method extracts only the date part from the appointmentTime field.
//      - It returns a LocalDate object representing just the date (without the time) of the scheduled appointment.
public LocalDate getAppointmentDate() {
    return appointmentTime.toLocalDate();
}

// 8. 'getAppointmentTimeOnly' method:
//    - Type: private LocalTime
//    - Description:
//      - This method extracts only the time part from the appointmentTime field.
//      - It returns a LocalTime object representing just the time (without the date) of the scheduled appointment.
public LocalTime getAppointmentTimeOnly() {
    return appointmentTime.toLocalTime();
}

// 9. Constructor(s):
//    - A no-argument constructor is implicitly provided by JPA for entity creation.
//    - A parameterized constructor can be added as needed to initialize fields.
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

@Override
public String toString() {
    return "Appointment{" +
            "id=" + id +
            ", doctor=" + doctor +
            ", patient=" + patient +
            ", appointmentTime=" + appointmentTime +
            ", status=" + status +
            '}';
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Appointment)) return false;

    Appointment that = (Appointment) o;

    if (status != that.status) return false;
    if (!id.equals(that.id)) return false;
    if (!doctor.equals(that.doctor)) return false;
    if (!patient.equals(that.patient)) return false;
    return appointmentTime.equals(that.appointmentTime);
}

@Override
public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + doctor.hashCode();
    result = 31 * result + patient.hashCode();
    result = 31 * result + appointmentTime.hashCode();
    result = 31 * result + status;
    return result;
}
}
