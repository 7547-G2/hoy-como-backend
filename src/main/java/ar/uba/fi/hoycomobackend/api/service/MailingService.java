package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;

public interface MailingService {
    void sendMailToNewComercio(Comercio comercio);
}
