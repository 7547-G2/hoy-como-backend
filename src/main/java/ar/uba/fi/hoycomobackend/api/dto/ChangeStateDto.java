package ar.uba.fi.hoycomobackend.api.dto;

public class ChangeStateDto {
    private String state;
    private String motivoDeshabilitacion;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMotivoDeshabilitacion() {
        return motivoDeshabilitacion;
    }

    public void setMotivoDeshabilitacion(String motivoDeshabilitacion) {
        this.motivoDeshabilitacion = motivoDeshabilitacion;
    }
}
