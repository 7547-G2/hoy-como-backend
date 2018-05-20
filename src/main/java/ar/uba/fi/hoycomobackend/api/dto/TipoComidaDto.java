package ar.uba.fi.hoycomobackend.api.dto;

public class TipoComidaDto {
    private Long id;
    private String tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public TipoComidaDto removeNulls() {
        if (id == null) {
            id = 0L;
        }
        if (tipo == null) {
            tipo = "";
        }
        return this;
    }
}
