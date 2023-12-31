package com.example.bookify.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class ReportedUserDTO {
    @Expose
    private String reason;
    @Expose
    private Date created;
    @Expose
    private Long reportedUser;
    @Expose
    private Long createdBy;

    public ReportedUserDTO() {}

    public ReportedUserDTO(String reason, Date created, Long reportedUser, Long createdBy) {
        this.reason = reason;
        this.created = created;
        this.reportedUser = reportedUser;
        this.createdBy = createdBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Long reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
