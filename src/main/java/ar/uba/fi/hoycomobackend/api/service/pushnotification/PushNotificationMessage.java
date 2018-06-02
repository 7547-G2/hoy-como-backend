package ar.uba.fi.hoycomobackend.api.service.pushnotification;

import com.google.firebase.messaging.Message;

public interface PushNotificationMessage {
    void sendMessage(Message message);
}
