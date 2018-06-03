package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.Opcion;

import java.util.Set;

public class PlatoMobileUserDto {
    private Long id_plato;
    private String nombre;
    private String imagen;
    private Float precio;
    private Set<Opcion> opcionales;

    public Long getId_plato() {
        return id_plato;
    }

    public void setId_plato(Long id_plato) {
        this.id_plato = id_plato;
    }

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

    public Set<Opcion> getOpcionales() {
        return opcionales;
    }

    public void setOpcionales(Set<Opcion> opcionales) {
        this.opcionales = opcionales;
    }
}
