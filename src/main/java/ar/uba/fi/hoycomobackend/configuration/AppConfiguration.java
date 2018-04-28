package ar.uba.fi.hoycomobackend.configuration;

import ar.uba.fi.hoycomobackend.api.service.DevelopmentMailingService;
import ar.uba.fi.hoycomobackend.api.service.JavaMailingService;
import ar.uba.fi.hoycomobackend.api.service.MailingService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        return modelMapper;
    }

    @Bean
    @Profile({"dev", "localprod"})
    public MailingService devMailingService() {
        return new DevelopmentMailingService();
    }

    @Bean
    @Profile("prod")
    public MailingService productionMailingService() {
        JavaMailSender javaMailingService = new JavaMailSenderImpl();
        return new JavaMailingService(javaMailingService);
    }

}
