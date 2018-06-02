package ar.uba.fi.hoycomobackend.api.service.pushnotification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseApplication implements PushNotificationMessage {

    private FirebaseOptions firebaseOptions;

    public FirebaseApplication() throws IOException {
        try {
            FirebaseApp.getInstance();
        } catch (Exception e) {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/firebase_config.json");

            firebaseOptions = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://hoy-como-1526042483481.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
        }
    }

    public String sendMessage(Message message) {
        try {
            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            return e.getErrorCode();
        }
    }
}
