package com.example.bookify.model.user;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class ReportedUserDetailsDTO {
    @Expose
    private Long id;
    @Expose
    private String reason;
    @Expose
    private Date created;
    @Expose
    private UserDTO reportedUser;
    @Expose
    private UserDTO createdBy;

    public ReportedUserDetailsDTO(Long id, String reason, Date created, UserDTO reportedUser, UserDTO createdBy) {
        this.id = id;
        this.reason = reason;
        this.created = created;
        this.reportedUser = reportedUser;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDTO getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(UserDTO reportedUser) {
        this.reportedUser = reportedUser;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public ReportedUserDetailsDTO() {
    }
}
