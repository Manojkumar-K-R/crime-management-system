package com.crimemanagement.service;

import com.crimemanagement.payload.request.LoginRequest;
import com.crimemanagement.payload.request.RegisterRequest;
import com.crimemanagement.payload.response.ApiResponse;
import com.crimemanagement.payload.response.LoginResponse;

public interface AuthService {

    ApiResponse registerUser(RegisterRequest request);
    
    LoginResponse loginUser(LoginRequest request);

}