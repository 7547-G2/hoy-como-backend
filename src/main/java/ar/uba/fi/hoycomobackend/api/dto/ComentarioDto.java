package ar.uba.fi.hoycomobackend.api.dto;

public class ComentarioDto {
    private Integer estrellas;
    private String comentario;

    public Integer getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Integer estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
