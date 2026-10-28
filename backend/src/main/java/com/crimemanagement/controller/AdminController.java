package com.crimemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.crimemanagement.payload.request.CreateOfficerRequest;
import com.crimemanagement.payload.response.ApiResponse;
import com.crimemanagement.payload.response.DashboardResponse;
import com.crimemanagement.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/officers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createOfficer(
            @Valid @RequestBody CreateOfficerRequest request) {

        ApiResponse response = adminService.createOfficer(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResponse> getDashboard() {

        return ResponseEntity.ok(adminService.getDashboard());
    }
}