package ar.uba.fi.hoycomobackend.api.dto;

import java.util.List;

public class TipoComidaDto {
    private Long id;
    private String tipo;
    private List<Long> comercioId;

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

    public List<Long> getComercioId() {
        return comercioId;
    }

    public void setComercioId(List<Long> comercioId) {
        this.comercioId = comercioId;
    }
}
