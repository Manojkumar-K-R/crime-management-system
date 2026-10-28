package com.crimemanagement.service.imp;

import org.springframework.stereotype.Service;

import com.crimemanagement.service.AdminService;
import com.crimemanagement.payload.request.CreateOfficerRequest;
import com.crimemanagement.payload.response.ApiResponse;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.crimemanagement.entity.Role;
import com.crimemanagement.entity.User;
import com.crimemanagement.enums.RoleName;
import com.crimemanagement.repository.RoleRepository;
import com.crimemanagement.repository.UserRepository;
import com.crimemanagement.repository.ComplaintRepository;

import com.crimemanagement.enums.ComplaintStatus;
import com.crimemanagement.payload.response.DashboardResponse;

@Service
public class AdminServiceImpl implements AdminService {
	
	private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(ComplaintRepository complaintRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

this.complaintRepository = complaintRepository;
this.userRepository = userRepository;
this.roleRepository = roleRepository;
this.passwordEncoder = passwordEncoder;
}

    @Override
    public ApiResponse createOfficer(CreateOfficerRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already exists.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already exists.");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return new ApiResponse(false, "Phone number already exists.");
        }

        Role officerRole = roleRepository.findByRoleName(RoleName.ROLE_OFFICER)
                .orElseThrow(() -> new RuntimeException("ROLE_OFFICER not found"));

        User officer = new User();

        officer.setFirstName(request.getFirstName());
        officer.setLastName(request.getLastName());
        officer.setUsername(request.getUsername());
        officer.setEmail(request.getEmail());
        officer.setPhone(request.getPhone());
        officer.setPassword(passwordEncoder.encode(request.getPassword()));
        officer.setEnabled(true);
        officer.setRole(officerRole);

        userRepository.save(officer);

        return new ApiResponse(true, "Officer created successfully.");
    }
    
    @Override
    public DashboardResponse getDashboard() {

        DashboardResponse response = new DashboardResponse();

        response.setTotalComplaints(
                complaintRepository.count());

        response.setPendingComplaints(
                complaintRepository.countByStatus(ComplaintStatus.PENDING));

        response.setUnderInvestigation(
                complaintRepository.countByStatus(
                        ComplaintStatus.UNDER_INVESTIGATION));

        response.setResolvedComplaints(
                complaintRepository.countByStatus(
                        ComplaintStatus.RESOLVED));

        response.setClosedComplaints(
                complaintRepository.countByStatus(
                        ComplaintStatus.CLOSED));

        response.setTotalCitizens(
                userRepository.countByRole_RoleName(
                        RoleName.ROLE_CITIZEN));

        response.setTotalOfficers(
                userRepository.countByRole_RoleName(
                        RoleName.ROLE_OFFICER));

        return response;
    }
}