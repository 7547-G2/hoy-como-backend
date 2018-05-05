package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.PlatoState;

public class PlatoUpdateDto {
    private Long id;
    private String nombre;
    private String imagen;
    private PlatoState platoState;
    private Float precio;
    private Long categoria;
    private Integer orden;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlatoState getPlatoState() {
        return platoState;
    }

    public void setPlatoState(PlatoState platoState) {
        this.platoState = platoState;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
