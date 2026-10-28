package com.crimemanagement.payload.response;

public class DashboardResponse {

    private long totalComplaints;
    private long pendingComplaints;
    private long underInvestigation;
    private long resolvedComplaints;
    private long closedComplaints;

    private long totalCitizens;
    private long totalOfficers;

    public long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public long getPendingComplaints() {
        return pendingComplaints;
    }

    public void setPendingComplaints(long pendingComplaints) {
        this.pendingComplaints = pendingComplaints;
    }

    public long getUnderInvestigation() {
        return underInvestigation;
    }

    public void setUnderInvestigation(long underInvestigation) {
        this.underInvestigation = underInvestigation;
    }

    public long getResolvedComplaints() {
        return resolvedComplaints;
    }

    public void setResolvedComplaints(long resolvedComplaints) {
        this.resolvedComplaints = resolvedComplaints;
    }

    public long getClosedComplaints() {
        return closedComplaints;
    }

    public void setClosedComplaints(long closedComplaints) {
        this.closedComplaints = closedComplaints;
    }

    public long getTotalCitizens() {
        return totalCitizens;
    }

    public void setTotalCitizens(long totalCitizens) {
        this.totalCitizens = totalCitizens;
    }

    public long getTotalOfficers() {
        return totalOfficers;
    }

    public void setTotalOfficers(long totalOfficers) {
        this.totalOfficers = totalOfficers;
    }
}