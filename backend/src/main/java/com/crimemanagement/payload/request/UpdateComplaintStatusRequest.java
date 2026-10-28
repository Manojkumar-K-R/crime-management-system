package com.crimemanagement.payload.request;

import com.crimemanagement.enums.ComplaintStatus;

public class UpdateComplaintStatusRequest {

    private ComplaintStatus status;

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }
}