package com.example.bookify.model;

import com.example.bookify.enumerations.NotificationType;
import com.google.gson.annotations.Expose;

import java.util.Map;

public class NotificationSettingsDTO {
    @Expose
    private Map<NotificationType, Boolean> notificationType;

    public Map<NotificationType, Boolean> getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Map<NotificationType, Boolean> notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationSettingsDTO() {
    }

    public NotificationSettingsDTO(Map<NotificationType, Boolean> notificationType) {
        this.notificationType = notificationType;
    }
}
