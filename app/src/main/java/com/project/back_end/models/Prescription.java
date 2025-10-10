package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {



// 1. 'id' field:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

// 2. 'patientName' field:
    @NotNull
    @Size(min = 3, max = 100)
    private String patientName;

// 3. 'appointmentId' field:
    @NotNull
    private Long appointmentId;


// 4. 'medication' field:
    @NotNull
    @Size(min = 3, max = 100)
    private String medication;


// 5. 'dosage' field:
    @NotNull
    private String dosage;


// 6. 'doctorNotes' field:
    @Size(max = 200)
    private String doctorNotes;


// 7. Constructors:
//    - The class includes a no-argument constructor (default constructor) and a parameterized constructor that initializes the fields: patientName, medication, dosage, doctorNotes, and appointmentId.
    public Prescription() {
    }

    public Prescription(String id, String patientName, String medication, String dosage, String doctorNotes, Long appointmentId) {
        this.id = id;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
        this.appointmentId = appointmentId;
    }

// 8. Getters and Setters:
//    - Standard getter and setter methods are provided for all fields: id, patientName, medication, dosage, doctorNotes, and appointmentId.
//    - These methods allow access and modification of the fields of the Prescription class.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }


}
