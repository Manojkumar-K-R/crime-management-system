package com.crimemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.InvestigationNote;

public interface InvestigationNoteRepository
        extends JpaRepository<InvestigationNote, Long> {

    List<InvestigationNote> findByComplaintOrderByCreatedAtAsc(Complaint complaint);
}