package ar.uba.fi.hoycomobackend.api.service.pushnotification;

import com.google.firebase.messaging.Message;

public interface PushNotificationMessage {
    String sendMessage(Message message);
}
