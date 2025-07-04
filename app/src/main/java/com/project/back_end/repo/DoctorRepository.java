package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Find a doctor by their email
    Doctor findByEmail(String email);

    // Find doctors whose name contains the search string (case-sensitive)
    List<Doctor> findByNameLike(String name);

    // Find doctors by name and specialty (case-insensitive)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    // Find doctors by specialty (case-insensitive)
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
