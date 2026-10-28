package com.crimemanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crimemanagement.entity.User;
import com.crimemanagement.enums.RoleName;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
   
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    long countByRole_RoleName(RoleName roleName);
    

}