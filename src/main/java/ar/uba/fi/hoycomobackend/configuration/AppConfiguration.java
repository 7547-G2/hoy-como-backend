package ar.uba.fi.hoycomobackend.configuration;

import ar.uba.fi.hoycomobackend.api.service.DevelopmentMailingService;
import ar.uba.fi.hoycomobackend.api.service.JavaMailingService;
import ar.uba.fi.hoycomobackend.api.service.MailingService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        modelMapper.getConfiguration().setPropertyCondition(
                (MappingContext<Object, Object> pContext) -> pContext.getSource() != null);

        return modelMapper;
    }

    @Bean
    @Profile({"dev", "localprod"})
    public MailingService devMailingService() {
        return new DevelopmentMailingService();
    }

    @Bean
    @Profile("prod")
    public MailingService productionMailingService(JavaMailSender javaMailSender) {
        MailingService javaMailingService = new JavaMailingService(javaMailSender);
        return javaMailingService;
    }

}
