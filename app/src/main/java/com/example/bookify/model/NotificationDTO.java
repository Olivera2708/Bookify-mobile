package com.example.bookify.model;

import com.example.bookify.enumerations.NotificationType;
import com.google.gson.annotations.Expose;

public class NotificationDTO {
    @Expose
    private Long id;
    @Expose
    private String description;
    @Expose
    private NotificationType notificationType;
    @Expose
    private String created;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, String description, NotificationType notificationType, String created) {
        this.id = id;
        this.description = description;
        this.notificationType = notificationType;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
