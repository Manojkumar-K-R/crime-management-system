package com.crimemanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.crimemanagement.entity.Role;
import com.crimemanagement.enums.RoleName;
import com.crimemanagement.repository.RoleRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.crimemanagement.entity.User;
import com.crimemanagement.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(RoleRepository roleRepository,
	                       UserRepository userRepository,
	                       PasswordEncoder passwordEncoder) {

	    this.roleRepository = roleRepository;
	    this.userRepository = userRepository;
	    this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {

	    createRoleIfNotExists(RoleName.ROLE_ADMIN);
	    createRoleIfNotExists(RoleName.ROLE_OFFICER);
	    createRoleIfNotExists(RoleName.ROLE_CITIZEN);

	    createDefaultAdmin();
	}
	
	private void createDefaultAdmin() {

	    if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
	        return;
	    }

	    Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
	            .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

	    User admin = new User();
	    admin.setFirstName("System");
	    admin.setLastName("Administrator");
	    admin.setUsername("admin");
	    admin.setEmail("admin@gmail.com");
	    admin.setPhone("9999999999");
	    admin.setPassword(passwordEncoder.encode("Admin@123"));
	    admin.setEnabled(true);
	    admin.setRole(adminRole);

	    userRepository.save(admin);

	    System.out.println("Default Admin Created Successfully.");
	}

    private void createRoleIfNotExists(RoleName roleName) {

        if (roleRepository.findByRoleName(roleName).isEmpty()) {

            Role role = new Role();
            role.setRoleName(roleName);

            roleRepository.save(role);

            System.out.println(roleName + " created.");

        }

    }

}