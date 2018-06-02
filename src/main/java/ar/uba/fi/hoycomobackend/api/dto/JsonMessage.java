package ar.uba.fi.hoycomobackend.api.dto;

public class JsonMessage {
    private String message;

    public JsonMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
