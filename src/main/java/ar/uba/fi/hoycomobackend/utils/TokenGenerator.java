package ar.uba.fi.hoycomobackend.utils;

import java.security.SecureRandom;

public class TokenGenerator {

    public static String createToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes.toString();
    }
}
