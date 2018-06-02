package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.PlatoState;

import java.util.List;

public class PlatoGetDto {
    private Long id;
    private String nombre;
    private String imagen;
    private Float precio;
    private PlatoState state;
    private Long categoria;
    private String descCategoria;
    private Integer orden;
    private List<Long> opcionalIds;


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

    public PlatoState getState() {
        return state;
    }

    public void setState(PlatoState state) {
        this.state = state;
    }

    public String getDescCategoria() {
        return descCategoria;
    }

    public void setDescCategoria(String descCategoria) {
        this.descCategoria = descCategoria;
    }

    public List<Long> getOpcionalIds() {
        return opcionalIds;
    }

    public void setOpcionalIds(List<Long> opcionalIds) {
        this.opcionalIds = opcionalIds;
    }
}
