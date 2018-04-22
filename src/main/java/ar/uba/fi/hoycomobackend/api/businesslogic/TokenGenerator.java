package ar.uba.fi.hoycomobackend.api.businesslogic;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class TokenGenerator {

    private SecureRandom secureRandom;

    public TokenGenerator() {
        this.secureRandom = new SecureRandom();
    }

    public String createToken() {
        byte bytes[] = new byte[20];
        secureRandom.nextBytes(bytes);
        return bytes.toString();
    }
}
