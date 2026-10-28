package com.crimemanagement.service;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.User;

public interface EmailService {

	void sendComplaintCreatedEmail(User citizen, Complaint complaint);
	
	void sendOfficerAssignedEmail(User officer, Complaint complaint);
	
	void sendStatusUpdatedEmail(User citizen, Complaint complaint);
	
	void sendComplaintResolvedEmail(User citizen, Complaint complaint);
	
    void sendEmail(String to,
                   String subject,
                   String body);

}