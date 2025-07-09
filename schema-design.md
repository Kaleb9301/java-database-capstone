# Smart Clinic Management System - Schema Design

---

## MySQL Database Design

In MySQL, we store structured, relational data that requires strict validation and relationships. The core tables are:

### Table: patients
- id: INT, Primary Key, Auto Increment  
- first_name: VARCHAR(50), NOT NULL  
- last_name: VARCHAR(50), NOT NULL  
- email: VARCHAR(100), UNIQUE, NOT NULL  
- phone: VARCHAR(15), NULL  
- date_of_birth: DATE, NULL  
- created_at: DATETIME, NOT NULL, default CURRENT_TIMESTAMP  
- updated_at: DATETIME, NULL, on update CURRENT_TIMESTAMP  

*Notes:*  
- Email should be unique to avoid duplicate accounts.  
- On patient deletion, appointments can be either cascaded deleted or marked as orphaned depending on business logic.

---

### Table: doctors
- id: INT, Primary Key, Auto Increment  
- first_name: VARCHAR(50), NOT NULL  
- last_name: VARCHAR(50), NOT NULL  
- specialization: VARCHAR(100), NOT NULL  
- email: VARCHAR(100), UNIQUE, NOT NULL  
- phone: VARCHAR(15), NULL  
- created_at: DATETIME, NOT NULL, default CURRENT_TIMESTAMP  
- updated_at: DATETIME, NULL, on update CURRENT_TIMESTAMP  

*Notes:*  
- Doctors may not have overlapping appointments (business logic validation required in app).  

---

### Table: appointments
- id: INT, Primary Key, Auto Increment  
- patient_id: INT, Foreign Key → patients(id), NOT NULL  
- doctor_id: INT, Foreign Key → doctors(id), NOT NULL  
- appointment_time: DATETIME, NOT NULL  
- status: ENUM('Scheduled', 'Completed', 'Cancelled'), NOT NULL, default 'Scheduled'  
- created_at: DATETIME, NOT NULL, default CURRENT_TIMESTAMP  
- updated_at: DATETIME, NULL, on update CURRENT_TIMESTAMP  

*Notes:*  
- Appointment time must be validated to avoid overlaps for a doctor.  
- Cascading deletes for patients or doctors should be carefully handled.

---

### Table: admin
- id: INT, Primary Key, Auto Increment  
- username: VARCHAR(50), UNIQUE, NOT NULL  
- password_hash: VARCHAR(255), NOT NULL  
- email: VARCHAR(100), UNIQUE, NOT NULL  
- created_at: DATETIME, NOT NULL, default CURRENT_TIMESTAMP  
- updated_at: DATETIME, NULL, on update CURRENT_TIMESTAMP  

*Notes:*  
- Passwords must be stored hashed with a strong algorithm (e.g., bcrypt).  

---

### (Optional) Table: clinic_locations
- id: INT, Primary Key, Auto Increment  
- name: VARCHAR(100), NOT NULL  
- address: TEXT, NOT NULL  
- phone: VARCHAR(15), NULL  
- created_at: DATETIME, NOT NULL, default CURRENT_TIMESTAMP  
- updated_at: DATETIME, NULL, on update CURRENT_TIMESTAMP  

---

### (Optional) Table: payments
- id: INT, Primary Key, Auto Increment  
- appointment_id: INT, Foreign Key → appointments(id), NOT NULL  
- amount: DECIMAL(10,2), NOT NULL  
- payment_date: DATETIME, NOT NULL  
- payment_method: VARCHAR(50), NOT NULL  
- status: ENUM('Pending', 'Completed', 'Failed'), NOT NULL, default 'Pending'  

---

## MongoDB Collection Design

In MongoDB, we store flexible, nested, or optional data that may evolve or vary per document. The collections complement the relational model by storing data such as free-form notes, prescriptions, and logs.

### Collection: prescriptions

Example Document:

```json
{
  "_id": "ObjectId('64abc123456abcdef7890')",
  "patientId": 101,
  "doctorId": 12,
  "appointmentId": 505,
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration_days": 7
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "frequency": "as needed",
      "duration_days": 5
    }
  ],
  "doctorNotes": "Take antibiotics with food. Avoid alcohol.",
  "refillCount": 1,
  "issuedDate": "2025-06-01T10:30:00Z",
  "pharmacy": {
    "name": "HealthPlus Pharmacy",
    "address": "123 Main St, Cityville"
  },
  "tags": ["antibiotic", "pain relief"]
}
