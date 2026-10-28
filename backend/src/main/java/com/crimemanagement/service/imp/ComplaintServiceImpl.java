package com.crimemanagement.service.imp;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.Evidence;
import com.crimemanagement.entity.InvestigationNote;
import com.crimemanagement.entity.User;
import com.crimemanagement.enums.ComplaintStatus;
import com.crimemanagement.enums.RoleName;
import com.crimemanagement.payload.request.AddInvestigationNoteRequest;
import com.crimemanagement.payload.request.AssignOfficerRequest;
import com.crimemanagement.payload.request.ComplaintRequest;
import com.crimemanagement.payload.request.UpdateComplaintStatusRequest;
import com.crimemanagement.payload.response.ComplaintResponse;
import com.crimemanagement.payload.response.EvidenceResponse;
import com.crimemanagement.payload.response.InvestigationNoteResponse;
import com.crimemanagement.repository.ComplaintRepository;
import com.crimemanagement.repository.EvidenceRepository;
import com.crimemanagement.repository.InvestigationNoteRepository;
import com.crimemanagement.repository.UserRepository;
import com.crimemanagement.security.service.UserDetailsImpl;
import com.crimemanagement.service.ComplaintService;
import com.crimemanagement.service.EmailService;
import com.crimemanagement.util.ComplaintNumberGenerator;
import com.crimemanagement.specification.ComplaintSpecification;

@Service
public class ComplaintServiceImpl implements ComplaintService {
	
	@Autowired
	private InvestigationNoteRepository investigationNoteRepository;
	
	@Autowired
	private EvidenceRepository evidenceRepository;
	
	private final EmailService emailService;
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	private final ComplaintRepository complaintRepository;
	private final UserRepository userRepository;
	private final ComplaintNumberGenerator complaintNumberGenerator;
	
	public ComplaintServiceImpl(
	        ComplaintRepository complaintRepository,
	        UserRepository userRepository,
	        ComplaintNumberGenerator complaintNumberGenerator,EmailService emailService) {

	    this.complaintRepository = complaintRepository;
	    this.userRepository = userRepository;
	    this.complaintNumberGenerator = complaintNumberGenerator;
	    this.emailService = emailService;
	}

	@Override
	public ComplaintResponse createComplaint(ComplaintRequest request) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    User citizen = userRepository.findById(userDetails.getId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Complaint complaint = new Complaint();

	    complaint.setTitle(request.getTitle());
	    complaint.setDescription(request.getDescription());
	    complaint.setCrimeType(request.getCrimeType());
	    complaint.setIncidentLocation(request.getIncidentLocation());
	    complaint.setIncidentDate(request.getIncidentDate());

	    complaint.setCitizen(citizen);
	    
	    Complaint savedComplaint = complaintRepository.save(complaint);
	    
	    savedComplaint.setComplaintNumber(
	            complaintNumberGenerator.generateComplaintNumber(savedComplaint.getId()));
	    savedComplaint = complaintRepository.save(savedComplaint);
	    
	    emailService.sendComplaintCreatedEmail(citizen, complaint);
	    
	    ComplaintResponse response = new ComplaintResponse();

	    response.setComplaintNumber(savedComplaint.getComplaintNumber());
	    response.setTitle(savedComplaint.getTitle());
	    response.setCrimeType(savedComplaint.getCrimeType());
	    response.setIncidentLocation(savedComplaint.getIncidentLocation());
	    response.setIncidentDate(savedComplaint.getIncidentDate());
	    response.setStatus(savedComplaint.getStatus());

	    return response;
	}
	
	@Override
	public List<ComplaintResponse> getMyComplaints() {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

	    User citizen = userRepository.findById(userDetails.getId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    List<Complaint> complaints = complaintRepository.findByCitizen(citizen);
	    
	    return complaints.stream().map(this::mapToResponse).toList();

	}
	
	@Override
	public ComplaintResponse assignOfficer(AssignOfficerRequest request) {

	    Complaint complaint = complaintRepository.findById(request.getComplaintId())
	            .orElseThrow(() -> new RuntimeException("Complaint not found"));

	    User officer = userRepository.findById(request.getOfficerId())
	            .orElseThrow(() -> new RuntimeException("Officer not found"));

	    if (officer.getRole().getRoleName() != RoleName.ROLE_OFFICER) {
	        throw new RuntimeException("Selected user is not an officer");
	    }

	    complaint.setAssignedOfficer(officer);

	    Complaint updatedComplaint = complaintRepository.save(complaint);
	    
	    emailService.sendOfficerAssignedEmail(officer, updatedComplaint);


	    return mapToResponse(updatedComplaint);
	}

	private ComplaintResponse mapToResponse(Complaint complaint) {

	    ComplaintResponse response = new ComplaintResponse();

	    response.setComplaintNumber(complaint.getComplaintNumber());
	    response.setTitle(complaint.getTitle());
	    response.setCrimeType(complaint.getCrimeType());
	    response.setIncidentLocation(complaint.getIncidentLocation());
	    response.setIncidentDate(complaint.getIncidentDate());
	    response.setStatus(complaint.getStatus());

	    if (complaint.getAssignedOfficer() != null) {
	        response.setOfficerId(complaint.getAssignedOfficer().getId());

	        response.setOfficerName(
	                complaint.getAssignedOfficer().getFirstName()
	                + " "
	                + complaint.getAssignedOfficer().getLastName());
	    }

	    return response;
	}
	
	@Override
	public List<ComplaintResponse> getAssignedComplaints() {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    User officer = userRepository.findById(userDetails.getId())
	            .orElseThrow(() -> new RuntimeException("Officer not found"));

	    List<Complaint> complaints =
	            complaintRepository.findByAssignedOfficer(officer);

	    return complaints.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	@Override
	public ComplaintResponse updateComplaintStatus(
	        Long complaintId,
	        UpdateComplaintStatusRequest request) {

	    // Get logged-in officer
	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    User officer = userRepository.findById(userDetails.getId())
	            .orElseThrow(() -> new RuntimeException("Officer not found"));

	    // Find complaint
	    Complaint complaint = complaintRepository.findById(complaintId)
	            .orElseThrow(() -> new RuntimeException("Complaint not found"));

	    // Ensure the complaint belongs to this officer
	    if (complaint.getAssignedOfficer() == null ||
	        !complaint.getAssignedOfficer().getId().equals(officer.getId())) {

	        throw new RuntimeException(
	                "You are not authorized to update this complaint");
	    }

	    // Update status
	    complaint.setStatus(request.getStatus());

	    Complaint updatedComplaint = complaintRepository.save(complaint);
	    
	    emailService.sendStatusUpdatedEmail(
	            updatedComplaint.getCitizen(),
	            updatedComplaint);
	    
	    if (updatedComplaint.getStatus() == ComplaintStatus.RESOLVED) {
	        emailService.sendComplaintResolvedEmail(
	                updatedComplaint.getCitizen(),
	                updatedComplaint);
	    }

	    return mapToResponse(updatedComplaint);
	}
	
	private InvestigationNoteResponse mapToInvestigationNoteResponse(
	        InvestigationNote note) {

	    InvestigationNoteResponse response = new InvestigationNoteResponse();

	    response.setId(note.getId());
	    response.setNote(note.getNote());

	    response.setOfficerId(note.getOfficer().getId());

	    response.setOfficerName(
	            note.getOfficer().getFirstName() + " "
	            + note.getOfficer().getLastName());

	    response.setCreatedAt(note.getCreatedAt());

	    return response;
	}
	
	@Override
	public ComplaintResponse addInvestigationNote(
	        Long complaintId,
	        AddInvestigationNoteRequest request) {

	    // Get logged-in officer
	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    User officer = userRepository.findById(userDetails.getId())
	            .orElseThrow(() -> new RuntimeException("Officer not found"));

	    // Find complaint
	    Complaint complaint = complaintRepository.findById(complaintId)
	            .orElseThrow(() -> new RuntimeException("Complaint not found"));

	    // Verify the complaint is assigned to this officer
	    if (complaint.getAssignedOfficer() == null ||
	        !complaint.getAssignedOfficer().getId().equals(officer.getId())) {

	        throw new RuntimeException(
	                "You are not authorized to add investigation notes.");
	    }

	    // Create note
	    InvestigationNote note = new InvestigationNote();
	    note.setNote(request.getNote());
	    note.setComplaint(complaint);
	    note.setOfficer(officer);

	    investigationNoteRepository.save(note);

	    return mapToResponse(complaint);
	}
	
	@Override
	public List<InvestigationNoteResponse> getInvestigationNotes(
	        Long complaintId) {

	    Complaint complaint = complaintRepository.findById(complaintId)
	            .orElseThrow(() -> new RuntimeException("Complaint not found"));

	    return investigationNoteRepository
	            .findByComplaintOrderByCreatedAtAsc(complaint)
	            .stream()
	            .map(this::mapToInvestigationNoteResponse)
	            .toList();
	}
	
	private EvidenceResponse mapToEvidenceResponse(Evidence evidence) {

	    EvidenceResponse response = new EvidenceResponse();

	    response.setId(evidence.getId());
	    response.setFileName(evidence.getFileName());
	    response.setFileType(evidence.getFileType());
	    response.setFilePath(evidence.getFilePath());

	    response.setUploadedById(evidence.getUploadedBy().getId());

	    response.setUploadedByName(
	            evidence.getUploadedBy().getFirstName() + " "
	            + evidence.getUploadedBy().getLastName());

	    response.setUploadedAt(evidence.getUploadedAt());

	    return response;
	}
	
	
	@Override
	public EvidenceResponse uploadEvidence(Long complaintId, MultipartFile file) {

	    try {

	        // Get logged-in officer
	        Authentication authentication =
	                SecurityContextHolder.getContext().getAuthentication();

	        UserDetailsImpl userDetails =
	                (UserDetailsImpl) authentication.getPrincipal();

	        User officer = userRepository.findById(userDetails.getId())
	                .orElseThrow(() -> new RuntimeException("Officer not found"));

	        // Find complaint
	        Complaint complaint = complaintRepository.findById(complaintId)
	                .orElseThrow(() -> new RuntimeException("Complaint not found"));

	        // Verify that this officer is assigned to the complaint
	        if (complaint.getAssignedOfficer() == null ||
	            !complaint.getAssignedOfficer().getId().equals(officer.getId())) {

	            throw new RuntimeException(
	                    "You are not authorized to upload evidence.");
	        }

	        // Create uploads directory if it doesn't exist
	        Path uploadPath = Paths.get(uploadDir);

	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        // Generate unique filename
	        String storedFileName = UUID.randomUUID() + "_"
	                + file.getOriginalFilename();

	        Path filePath = uploadPath.resolve(storedFileName);

	        // Save file to disk
	        Files.copy(
	                file.getInputStream(),
	                filePath,
	                StandardCopyOption.REPLACE_EXISTING);

	        // Save metadata to database
	        Evidence evidence = new Evidence();
	        evidence.setFileName(file.getOriginalFilename());
	        evidence.setFileType(file.getContentType());
	        String fileUrl = "/uploads/" + storedFileName;
	        evidence.setFilePath(fileUrl);
	        evidence.setComplaint(complaint);
	        evidence.setUploadedBy(officer);

	        evidenceRepository.save(evidence);

	        return mapToEvidenceResponse(evidence);

	    } catch (IOException e) {
	        throw new RuntimeException("File upload failed.", e);
	    }
	}

	@Override
	public List<EvidenceResponse> getEvidenceByComplaint(Long complaintId) {

	    Complaint complaint = complaintRepository.findById(complaintId)
	            .orElseThrow(() -> new RuntimeException("Complaint not found"));

	    return evidenceRepository.findByComplaint(complaint)
	            .stream()
	            .map(this::mapToEvidenceResponse)
	            .toList();
	}

	@Override
	public ResponseEntity<Resource> downloadEvidence(Long evidenceId) {

	    Evidence evidence = evidenceRepository.findById(evidenceId)
	            .orElseThrow(() -> new RuntimeException("Evidence not found"));

	    try {

	        Path path = Paths.get(evidence.getFilePath());

	        Resource resource = new UrlResource(path.toUri());

	        if (!resource.exists()) {
	            throw new RuntimeException("File not found");
	        }

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + evidence.getFileName() + "\"")
	                .contentType(MediaType.parseMediaType(evidence.getFileType()))
	                .body(resource);

	    } catch (MalformedURLException e) {
	        throw new RuntimeException("Unable to download file", e);
	    }
	}
	
	@Override
	public List<ComplaintResponse> searchComplaints(
	        ComplaintStatus status,
	        String crimeType,
	        Long officerId,
	        LocalDate fromDate,
	        LocalDate toDate,
	        String keyword) {

	    List<Complaint> complaints = complaintRepository.findAll(
	            ComplaintSpecification.filterComplaints(
	                    status,
	                    crimeType,
	                    officerId,
	                    fromDate,
	                    toDate,
	                    keyword));

	    return complaints.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	
}