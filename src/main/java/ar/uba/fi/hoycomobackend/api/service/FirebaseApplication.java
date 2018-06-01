package ar.uba.fi.hoycomobackend.api.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseApplication {

    private FirebaseOptions firebaseOptions;

    public FirebaseApplication() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebase_config.json");

        firebaseOptions = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://hoy-como-1526042483481.firebaseio.com")
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);

        // Retrieve services by passing the defaultApp variable...
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance(firebaseApp);
        FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance(firebaseApp);

    }

    public void sendMessage() {
        String registrationToken = "f2JmhojhF5E:APA91bEgS3_4m_80lN1IB5LufxoOIngbKsWEk002YPQfqp3PuSfmbZiImxkzXg0rRHOnN5aZXKprnNsZyd-L2Lt0PfKU0cxjQW5MdU0F65YfYrPJg2bHek9eKXZlES42CY5S9RZu67Be";

        // See documentation on defining a message payload.
        com.google.firebase.messaging.Message message = Message.builder()
                .putData("Data", "Dale charly")
                .setToken(registrationToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }


        String topic = "/topics/allDevices";

// See documentation on defining a message payload.
        Message message2 = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setTopic(topic)
                .build();

// Send a message to the devices subscribed to the provided topic.
        try {
            FirebaseMessaging.getInstance().send(message2);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

    }
}
