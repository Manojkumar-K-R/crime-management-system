package com.crimemanagement.payload.response;

import java.time.LocalDate;

import com.crimemanagement.enums.ComplaintStatus;

public class ComplaintResponse {

    private String complaintNumber;
    private String title;
    private String crimeType;
    private String incidentLocation;
    private LocalDate incidentDate;
    private ComplaintStatus status;
    
    private Long officerId;
    private String officerName;

   

    public ComplaintResponse() {
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

	public void setStatus(ComplaintStatus complaintStatus) {
		this.status = complaintStatus;
	}
	
	 public Long getOfficerId() {
	        return officerId;
	    }

	    public void setOfficerId(Long officerId) {
	        this.officerId = officerId;
	    }

	    public String getOfficerName() {
	        return officerName;
	    }

	    public void setOfficerName(String officerName) {
	        this.officerName = officerName;
	    }

}