package com.crimemanagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.crimemanagement.enums.ComplaintStatus;
import com.crimemanagement.payload.request.AddInvestigationNoteRequest;
import com.crimemanagement.payload.request.AssignOfficerRequest;
import com.crimemanagement.payload.request.ComplaintRequest;
import com.crimemanagement.payload.request.UpdateComplaintStatusRequest;
import com.crimemanagement.payload.response.ComplaintResponse;
import com.crimemanagement.payload.response.EvidenceResponse;
import com.crimemanagement.payload.response.InvestigationNoteResponse;
import com.crimemanagement.service.ComplaintService;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody ComplaintRequest request) {

        ComplaintResponse response = complaintService.createComplaint(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints() {
        return ResponseEntity.ok(complaintService.getMyComplaints());
    }
    
    @PutMapping("/assign-officer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponse> assignOfficer(
            @RequestBody AssignOfficerRequest request) {

        return ResponseEntity.ok(complaintService.assignOfficer(request));
    }
    
    @GetMapping("/assigned")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<List<ComplaintResponse>> getAssignedComplaints() {

        return ResponseEntity.ok(
                complaintService.getAssignedComplaints());
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(
            @PathVariable Long id,
            @RequestBody UpdateComplaintStatusRequest request) {

        return ResponseEntity.ok(
                complaintService.updateComplaintStatus(id, request));
    }
    
    @PostMapping("/{id}/notes")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<ComplaintResponse> addInvestigationNote(
            @PathVariable Long id,
            @RequestBody AddInvestigationNoteRequest request) {

        return ResponseEntity.ok(
                complaintService.addInvestigationNote(id, request));
    }
    
    @GetMapping("/{id}/notes")
    @PreAuthorize("hasAnyRole('OFFICER','ADMIN')")
    public ResponseEntity<List<InvestigationNoteResponse>> getInvestigationNotes(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.getInvestigationNotes(id));
    }
    
    @PostMapping("/{id}/evidence")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<EvidenceResponse> uploadEvidence(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                complaintService.uploadEvidence(id, file));
    }
    
    @GetMapping("/{id}/evidence")
    @PreAuthorize("hasAnyRole('OFFICER','ADMIN')")
    public ResponseEntity<List<EvidenceResponse>> getEvidence(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.getEvidenceByComplaint(id));
    }
    
    @GetMapping("/evidence/{evidenceId}/download")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<Resource> downloadEvidence(
            @PathVariable Long evidenceId) {

        return complaintService.downloadEvidence(evidenceId);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    public ResponseEntity<List<ComplaintResponse>> searchComplaints(

            @RequestParam(required = false) ComplaintStatus status,

            @RequestParam(required = false) String crimeType,

            @RequestParam(required = false) Long officerId,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(
                complaintService.searchComplaints(
                        status,
                        crimeType,
                        officerId,
                        fromDate,
                        toDate,
                        keyword));
    }
}