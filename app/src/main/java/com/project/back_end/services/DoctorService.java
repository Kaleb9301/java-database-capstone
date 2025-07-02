package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<LocalTime> getDoctorAvailability(Long doctorId, String date) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) return Collections.emptyList();

        Doctor doctor = optionalDoctor.get();
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                java.time.LocalDate.parse(date).atStartOfDay(),
                java.time.LocalDate.parse(date).atTime(23, 59)
        );

        Set<LocalTime> bookedTimes = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().withSecond(0).withNano(0))
                .collect(Collectors.toSet());

        return doctor.getAvailableTimes().stream()
                .map(LocalTime::parse)
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) return -1;
        doctorRepository.save(doctor);
        return 1;
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) return -1;
        appointmentRepository.deleteAllByDoctorId(id);
        doctorRepository.deleteById(id);
        return 1;
    }

    @Transactional
    public String validateDoctor(Login login) {
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            return "Invalid email or password";
        }
        return tokenService.generateToken(doctor.getEmail());
    }

    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike("%" + name + "%");
    }

    @Transactional
    public List<Doctor> filterDoctorsByNameSpecialityAndTime(String name, String specialty, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTime(doctors, timePeriod);
    }

    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String timePeriod) {
        return doctors.stream().filter(d -> d.getAvailableTimes().stream()
                .map(LocalTime::parse)
                .anyMatch(time -> isInTimePeriod(time, timePeriod)))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return filterDoctorByTime(doctors, timePeriod);
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecialty(String timePeriod, String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTime(doctors, timePeriod);
    }

    @Transactional
    public List<Doctor> filterDoctorBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorsByTime(String timePeriod) {
        return filterDoctorByTime(doctorRepository.findAll(), timePeriod);
    }

    private boolean isInTimePeriod(LocalTime time, String period) {
        if ("AM".equalsIgnoreCase(period)) {
            return time.isBefore(LocalTime.NOON);
        } else if ("PM".equalsIgnoreCase(period)) {
            return !time.isBefore(LocalTime.NOON);
        }
        return false;
    }
}
