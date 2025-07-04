package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. Appointments for doctor between time range
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // 2. Appointments for doctor + patient name (partial match) between time range
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);

    // 3. Delete all appointments for a doctor
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    // 4. Get all appointments for a patient
    List<Appointment> findByPatientId(Long patientId);

    // 5. Appointments by patient + status, ordered by time
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // 6. Filter by doctor name (like) and patientId
    List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatientId(String doctorName, Long patientId);

    // 7. Filter by doctor name, patientId, and status
    List<Appointment> findByDoctor_NameContainingIgnoreCaseAndPatientIdAndStatus(
            String doctorName, Long patientId, int status);

    // 8. Update status
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatusById(@Param("status") int status, @Param("id") long id);
    // This requires @Query to work — see note below
}
