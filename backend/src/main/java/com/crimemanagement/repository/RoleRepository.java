package com.crimemanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crimemanagement.entity.Role;
import com.crimemanagement.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
Optional<Role> findByRoleName(RoleName rolename);
}
