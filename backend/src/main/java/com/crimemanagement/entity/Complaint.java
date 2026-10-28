package com.crimemanagement.entity;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crimemanagement.enums.ComplaintStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {

    public List<InvestigationNote> getInvestigationNotes() {
		return investigationNotes;
	}

	public void setInvestigationNotes(List<InvestigationNote> investigationNotes) {
		this.investigationNotes = investigationNotes;
	}

	public List<Evidence> getEvidences() {
		return evidences;
	}

	public void setEvidences(List<Evidence> evidences) {
		this.evidences = evidences;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String complaintNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String crimeType;

    @Column(nullable = false)
    private String incidentLocation;

    @Column(nullable = false)
    private LocalDate incidentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id")
    private User assignedOfficer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
 private List<InvestigationNote> investigationNotes = new ArrayList<>();
    
    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Evidence> evidences = new ArrayList<>();
    
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = ComplaintStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public Complaint(Long id, String complaintNumber, String title, String description, String crimeType,
			String incidentLocation, LocalDate incidentDate, ComplaintStatus status, User citizen, User assignedOfficer,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.complaintNumber = complaintNumber;
		this.title = title;
		this.description = description;
		this.crimeType = crimeType;
		this.incidentLocation = incidentLocation;
		this.incidentDate = incidentDate;
		this.status = status;
		this.citizen = citizen;
		this.assignedOfficer = assignedOfficer;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Complaint() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComplaintNumber() {
		return complaintNumber;
	}

	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCrimeType() {
		return crimeType;
	}

	public void setCrimeType(String crimeType) {
		this.crimeType = crimeType;
	}

	public String getIncidentLocation() {
		return incidentLocation;
	}

	public void setIncidentLocation(String incidentLocation) {
		this.incidentLocation = incidentLocation;
	}

	public LocalDate getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(LocalDate incidentDate) {
		this.incidentDate = incidentDate;
	}

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public User getCitizen() {
		return citizen;
	}

	public void setCitizen(User citizen) {
		this.citizen = citizen;
	}

	public User getAssignedOfficer() {
		return assignedOfficer;
	}

	public void setAssignedOfficer(User assignedOfficer) {
		this.assignedOfficer = assignedOfficer;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
	
    
    