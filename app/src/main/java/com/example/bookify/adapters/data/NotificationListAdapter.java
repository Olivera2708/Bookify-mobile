package com.example.bookify.adapters.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookify.R;
import com.example.bookify.enumerations.NotificationType;
import com.example.bookify.model.NotificationDTO;
import com.example.bookify.services.NotificationsForegroundService;

import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<NotificationDTO> {
    private List<NotificationDTO> notifications;

    public NotificationListAdapter(@NonNull Context context, List<NotificationDTO> resource) {
        super(context, R.layout.notification, resource);
        this.notifications = resource;
    }

    @Override
    public int getCount() {
        return this.notifications.size();
    }

    @Nullable
    @Override
    public NotificationDTO getItem(int position) {
        return this.notifications.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationDTO notification = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification, parent, false);
        TextView notificationMessage = convertView.findViewById(R.id.notification_text);
        ImageView notificationIcon = convertView.findViewById(R.id.notification_icon);

        if(notification != null){
            notificationMessage.setText(notification.getDescription());
            setIcon(notification.getNotificationType(), notificationIcon);
        }
        return convertView;
    }

    private void setIcon(NotificationType notificationType, ImageView notificationIcon){
        int id = NotificationsForegroundService.getIcon(notificationType);
        notificationIcon.setImageResource(id);
    }
}
