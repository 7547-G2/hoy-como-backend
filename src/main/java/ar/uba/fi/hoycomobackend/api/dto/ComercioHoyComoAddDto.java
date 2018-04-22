package ar.uba.fi.hoycomobackend.api.dto;

import java.util.Set;

public class ComercioHoyComoAddDto {
    private Long id;
    private String email;
    private String nombre;
    private String razonSocial;
    private Set<TipoComidaAddDto> tipoComidaAddSet;
    private String password;
    private String imagenLogo;
    private String estado;
    private AddressDto addressDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    public String getImagenLogo() {
        return imagenLogo;
    }

    public void setImagenLogo(String imagenLogo) {
        this.imagenLogo = imagenLogo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Set<TipoComidaAddDto> getTipoComidaSet() {
        return tipoComidaAddSet;
    }

    public void setTipoComidaSet(Set<TipoComidaAddDto> tipoComidaAddSet) {
        this.tipoComidaAddSet = tipoComidaAddSet;
    }
}