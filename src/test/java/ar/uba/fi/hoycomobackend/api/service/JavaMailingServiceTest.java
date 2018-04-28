package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class JavaMailingServiceTest {

    private JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private JavaMailingService javaMailingService = new JavaMailingService(javaMailSender);

    @Before
    public void setUp() {
        System.setProperty("MAILER_USER", "");
        System.setProperty("MAILER_PASSWORD", "");
    }

    @Test
    public void mailingServiceCallsJavaMailSenderSendMethod() {
        Comercio comercio = createDefaultComercio();
         javaMailingService.sendMailToNewComercio(comercio);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }


}