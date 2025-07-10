package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document
public class Prescription {

  // @Document annotation:
//    - Marks the class as a MongoDB document (a collection in MongoDB).
//    - The collection name is specified as "prescriptions" to map this class to the "prescriptions" collection in MongoDB.

// 1. 'id' field:
//    - Type: private String
//    - Description:
//      - Represents the unique identifier for each prescription.
//      - The @Id annotation marks it as the primary key in the MongoDB collection.
//      - The id is of type String, which is commonly used for MongoDB's ObjectId as it stores IDs as strings in the database.
@Id
private String id;

// 2. 'patientName' field:
//    - Type: private String
//    - Description:
//      - Represents the name of the patient receiving the prescription.
//      - The @NotNull annotation ensures that the patient name is required.
//      - The @Size(min = 3, max = 100) annotation ensures that the name length is between 3 and 100 characters, ensuring a reasonable name length.
@NotNull
@Size(min = 3, max = 100)
private String patientName;

// 3. 'appointmentId' field:
//    - Type: private Long
//    - Description:
//      - Represents the ID of the associated appointment where the prescription was given.
//      - The @NotNull annotation ensures that the appointment ID is required for the prescription.
@NotNull
private Long appointmentId;

// 4. 'medication' field:
//    - Type: private String
//    - Description:
//      - Represents the medication prescribed to the patient.
//      - The @NotNull annotation ensures that the medication name is required.
//      - The @Size(min = 3, max = 100) annotation ensures that the medication name is between 3 and 100 characters, which ensures meaningful medication names.
@NotNull
@Size(min = 3, max = 100)
private String medication;

// 5. 'dosage' field:
//    - Type: private String
//    - Description:
//      - Represents the dosage information for the prescribed medication.
//      - The @NotNull annotation ensures that the dosage information is provided.
@NotNull
private String dosage;

// 6. 'doctorNotes' field:
//    - Type: private String
//    - Description:
//      - Represents any additional notes or instructions from the doctor regarding the prescription.
//      - The @Size(max = 200) annotation ensures that the doctor's notes do not exceed 200 characters, providing a reasonable limit for additional notes.
@Size(max = 200)
private String doctorNotes;

// 7. Constructors:
//    - The class includes a no-argument constructor (default constructor) and a parameterized constructor that initializes the fields: patientName, medication, dosage, doctorNotes, and appointmentId.
public Prescription(String id, String patientName, String medication, String dosage, String doctorNotes, Long appointmentId) {
    // Default constructor required by JPA
    this.id = id; // Convert Long to String for MongoDB ObjectId
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

public Long getAppointmentId() {
    return appointmentId;
}

public void setAppointmentId(Long appointmentId) {
    this.appointmentId = appointmentId;
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

@Override
public String toString() {
    return "Prescription{" +
            "id='" + id + '\'' +
            ", patientName='" + patientName + '\'' +
            ", appointmentId=" + appointmentId +
            ", medication='" + medication + '\'' +
            ", dosage='" + dosage + '\'' +
            ", doctorNotes='" + doctorNotes + '\'' +
            '}';
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Prescription)) return false;

    Prescription that = (Prescription) o;

    if (!id.equals(that.id)) return false;
    if (!patientName.equals(that.patientName)) return false;
    if (!appointmentId.equals(that.appointmentId)) return false;
    if (!medication.equals(that.medication)) return false;
    if (!dosage.equals(that.dosage)) return false;
    return doctorNotes != null ? doctorNotes.equals(that.doctorNotes) : that.doctorNotes == null;
}

@Override
public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + patientName.hashCode();
    result = 31 * result + appointmentId.hashCode();
    result = 31 * result + medication.hashCode();
    result = 31 * result + dosage.hashCode();
    result = 31 * result + (doctorNotes != null ? doctorNotes.hashCode() : 0);
    return result;
}


}
