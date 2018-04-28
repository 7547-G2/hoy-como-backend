package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JavaMailingService implements MailingService {

    private JavaMailSender javaMailSender;

    @Autowired
    public JavaMailingService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendMailToNewComercio(Comercio comercio) {
        String to = comercio.getEmail();
        String subject = "Bienvenido a Hoy Como " + comercio.getNombre();
        String text = "Javi abrí un issue para cambiar el contenido de este mensaje";

        sendSimpleMessage(to, subject, text);
    }
}
