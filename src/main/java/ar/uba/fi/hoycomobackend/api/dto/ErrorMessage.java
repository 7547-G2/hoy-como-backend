package ar.uba.fi.hoycomobackend.api.dto;

public class ErrorMessage {

    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
