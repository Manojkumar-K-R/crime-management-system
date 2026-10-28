package com.crimemanagement.service.imp;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.crimemanagement.entity.Role;
import com.crimemanagement.entity.User;
import com.crimemanagement.enums.RoleName;
import com.crimemanagement.payload.request.LoginRequest;
import com.crimemanagement.payload.request.RegisterRequest;
import com.crimemanagement.payload.response.ApiResponse;
import com.crimemanagement.payload.response.LoginResponse;
import com.crimemanagement.repository.RoleRepository;
import com.crimemanagement.repository.UserRepository;
import com.crimemanagement.security.jwt.JwtUtils;
import com.crimemanagement.service.AuthService;
import com.crimemanagement.security.service.UserDetailsImpl;


@Service
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {

this.userRepository = userRepository;
this.roleRepository = roleRepository;
this.passwordEncoder = passwordEncoder;
this.authenticationManager = authenticationManager;
this.jwtUtils = jwtUtils;
}
    
    @Override
    public LoginResponse loginUser(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                		new UsernamePasswordAuthenticationToken(
                		        request.getUsernameOrEmail(),
                		        request.getPassword()));

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtils.generateToken(userDetails.getUsername());

        return new LoginResponse(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority());
    }

    @Override
    public ApiResponse registerUser(RegisterRequest request) {

        // Check username
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already exists.");
        }

        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already exists.");
        }

        // Check phone
        if (userRepository.existsByPhone(request.getPhone())) {
            return new ApiResponse(false, "Phone number already exists.");
        }

        // Get Citizen Role
        Role citizenRole = roleRepository.findByRoleName(RoleName.ROLE_CITIZEN)
                .orElseThrow(() -> new RuntimeException("ROLE_CITIZEN not found"));

        // Create User
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Encrypt Password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(citizenRole);
        user.setEnabled(true);

        // Save User
        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully.");
    }

}