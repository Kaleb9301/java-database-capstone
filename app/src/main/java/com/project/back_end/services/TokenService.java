package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // JWT secret key, injected from application.properties or environment variable
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey signingKey;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    private void init() {
        // Initialize the signing key after jwtSecret is injected
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 3. Get the signing key used for JWT signing and validation
     */
    private SecretKey getSigningKey() {
        return signingKey;
    }

    /**
     * 4. Generate a JWT token for a given user email
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000L); // 7 days

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 5. Extract email (subject) from the JWT token
     */
    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // Log exception here if desired
            return null;
        }
    }

    /**
     * 6. Validate token based on user role and existence in DB
     *
     * @param token the JWT token to validate
     * @param role  the user role: "admin", "doctor", or "patient"
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token, String role) {
        try {
            String email = extractEmail(token);
            if (email == null) {
                return false;
            }

            switch (role.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(email) != null;
                case "doctor":
                    return doctorRepository.findByEmail(email) != null;
                case "patient":
                    return patientRepository.findByEmail(email) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            // Log exception here if desired
            return false;
        }
    }
}
