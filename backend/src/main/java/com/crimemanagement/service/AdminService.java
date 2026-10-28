package com.crimemanagement.service;

import com.crimemanagement.payload.request.CreateOfficerRequest;
import com.crimemanagement.payload.response.ApiResponse;
import com.crimemanagement.payload.response.DashboardResponse;

public interface AdminService {

    ApiResponse createOfficer(CreateOfficerRequest request);
    
    DashboardResponse getDashboard();

}