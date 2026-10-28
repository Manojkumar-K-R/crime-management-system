package com.crimemanagement.service.imp;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.crimemanagement.entity.Complaint;
import com.crimemanagement.entity.User;
import com.crimemanagement.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to,
                          String subject,
                          String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    @Override
    public void sendComplaintCreatedEmail(User citizen, Complaint complaint) {

        String subject = "Complaint Registered Successfully";

        String body =
                "Dear " + citizen.getFirstName() + ",\n\n" +
                "Your complaint has been registered successfully.\n\n" +
                "Complaint Number: " + complaint.getComplaintNumber() + "\n" +
                "Title: " + complaint.getTitle() + "\n" +
                "Crime Type: " + complaint.getCrimeType() + "\n" +
                "Status: " + complaint.getStatus() + "\n\n" +
                "We will keep you updated on the progress.\n\n" +
                "Regards,\n" +
                "Crime Management Department";

        sendEmail(citizen.getEmail(), subject, body);
    }
    
    
    
    @Override
    public void sendOfficerAssignedEmail(User officer, Complaint complaint) {

        String subject = "New Complaint Assigned";

        String body =
                "Dear " + officer.getFirstName() + ",\n\n" +
                "A new complaint has been assigned to you.\n\n" +
                "Complaint Number: " + complaint.getComplaintNumber() + "\n" +
                "Title: " + complaint.getTitle() + "\n" +
                "Crime Type: " + complaint.getCrimeType() + "\n\n" +
                "Please log in to the Crime Management System to start the investigation.\n\n" +
                "Regards,\n" +
                "Crime Management Department";

        sendEmail(officer.getEmail(), subject, body);
    }
    
    
    @Override
    public void sendStatusUpdatedEmail(User citizen, Complaint complaint) {

        String subject = "Complaint Status Updated";

        String body =
                "Dear " + citizen.getFirstName() + ",\n\n" +
                "Your complaint status has been updated.\n\n" +
                "Complaint Number: " + complaint.getComplaintNumber() + "\n" +
                "Title: " + complaint.getTitle() + "\n" +
                "New Status: " + complaint.getStatus() + "\n\n" +
                "Regards,\n" +
                "Crime Management Department";

        sendEmail(citizen.getEmail(), subject, body);
    }
    
    @Override
    public void sendComplaintResolvedEmail(User citizen, Complaint complaint) {

        String subject = "Complaint Resolved";

        String body =
                "Dear " + citizen.getFirstName() + ",\n\n" +
                "We are pleased to inform you that your complaint has been resolved.\n\n" +
                "Complaint Number: " + complaint.getComplaintNumber() + "\n" +
                "Title: " + complaint.getTitle() + "\n" +
                "Crime Type: " + complaint.getCrimeType() + "\n" +
                "Final Status: " + complaint.getStatus() + "\n\n" +
                "Thank you for using the Crime Management System.\n\n" +
                "Regards,\n" +
                "Crime Management Department";

        sendEmail(citizen.getEmail(), subject, body);
    }
}