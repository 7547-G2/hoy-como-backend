package ar.uba.fi.hoycomobackend.api.service.pushnotification;

import com.google.firebase.messaging.Message;

public class DevelopmentPushNotification implements PushNotificationMessage {
    @Override
    public String sendMessage(Message message) {
        return "";
    }
}
