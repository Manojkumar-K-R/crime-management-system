package com.crimemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.Evidence;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByComplaint(Complaint complaint);
}