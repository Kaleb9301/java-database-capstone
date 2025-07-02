package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final Service1 service;

    // 2. Constructor Injection for Service
    public AdminController(Service1 service) {
        this.service = service;
    }

    // 3. Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {
        // Delegates login validation to the service layer
        return service.validateAdmin(admin.getUsername(), admin.getPassword());
    }
}
