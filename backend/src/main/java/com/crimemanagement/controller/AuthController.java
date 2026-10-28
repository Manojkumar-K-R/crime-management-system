package com.crimemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.crimemanagement.payload.request.RegisterRequest;
import com.crimemanagement.payload.response.ApiResponse;
import com.crimemanagement.service.AuthService;

import com.crimemanagement.payload.request.LoginRequest;
import com.crimemanagement.payload.response.LoginResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        ApiResponse response = authService.registerUser(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.loginUser(request));
    }
}