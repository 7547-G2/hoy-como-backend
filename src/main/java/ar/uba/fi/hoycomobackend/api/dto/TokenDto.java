package ar.uba.fi.hoycomobackend.api.dto;

public class TokenDto {

    private Long comercioId;
    private String token;
    private String name;
    private String estadoComercio;
    private String motivoDeshabilitacion;
    private String logoComercio;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstadoComercio() {
        return estadoComercio;
    }

    public void setEstadoComercio(String estadoComercio) {
        this.estadoComercio = estadoComercio;
    }

    public String getMotivoDeshabilitacion() {
        return motivoDeshabilitacion;
    }

    public void setMotivoDeshabilitacion(String motivoDeshabilitacion) {
        this.motivoDeshabilitacion = motivoDeshabilitacion;
    }

    public String getLogoComercio() {
        return logoComercio;
    }

    public void setLogoComercio(String logoComercio) {
        this.logoComercio = logoComercio;
    }
}
