package ar.uba.fi.hoycomobackend.api.dto;

public class UpdateCategoriaComidaDto {

    private String nombreCategoria;
    private Boolean estaActivo;

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}
