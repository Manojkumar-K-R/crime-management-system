package com.crimemanagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.crimemanagement.enums.ComplaintStatus;
import com.crimemanagement.payload.request.AddInvestigationNoteRequest;
import com.crimemanagement.payload.request.AssignOfficerRequest;
import com.crimemanagement.payload.request.ComplaintRequest;
import com.crimemanagement.payload.request.UpdateComplaintStatusRequest;
import com.crimemanagement.payload.response.ComplaintResponse;
import com.crimemanagement.payload.response.EvidenceResponse;
import com.crimemanagement.payload.response.InvestigationNoteResponse;



public interface ComplaintService {

    ComplaintResponse createComplaint(ComplaintRequest request);
    
    List<ComplaintResponse> getMyComplaints();
    
    ComplaintResponse assignOfficer(AssignOfficerRequest request);
    
    List<ComplaintResponse> getAssignedComplaints();
    
    ComplaintResponse updateComplaintStatus(
            Long complaintId,
            UpdateComplaintStatusRequest request);
    
    ComplaintResponse addInvestigationNote(
            Long complaintId,
            AddInvestigationNoteRequest request);

    List<InvestigationNoteResponse> getInvestigationNotes(
            Long complaintId);
    
    EvidenceResponse uploadEvidence(
            Long complaintId,
            MultipartFile file);

    List<EvidenceResponse> getEvidenceByComplaint(
            Long complaintId);
    
    ResponseEntity<Resource> downloadEvidence(Long evidenceId);
    
    List<ComplaintResponse> searchComplaints(
            ComplaintStatus status,
            String crimeType,
            Long officerId,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword);
}