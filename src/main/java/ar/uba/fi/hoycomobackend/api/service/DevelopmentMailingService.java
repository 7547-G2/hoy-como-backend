package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DevelopmentMailingService implements MailingService {

    private static Logger LOGGER = LoggerFactory.getLogger(DevelopmentMailingService.class);

    @Override
    public void sendMailToNewComercio(Comercio comercio) {
        LOGGER.info("Using dummy development mailing service.");
    }
}
