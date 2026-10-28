package com.crimemanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.User;
import com.crimemanagement.enums.ComplaintStatus;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>,
JpaSpecificationExecutor<Complaint>  {

    List<Complaint> findByCitizen(User citizen);

    List<Complaint> findByAssignedOfficer(User assignedOfficer);

    Optional<Complaint> findByComplaintNumber(String complaintNumber);
   
    long countByStatus(ComplaintStatus status);

}