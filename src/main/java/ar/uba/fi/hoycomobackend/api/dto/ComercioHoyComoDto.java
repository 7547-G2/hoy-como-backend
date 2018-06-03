package ar.uba.fi.hoycomobackend.api.dto;

public class ComercioHoyComoDto {

    private Long id;
    private String email;
    private String nombre;
    private String razonSocial;
    private TipoComidaDto tipoComida;
    private String password;
    private String imagenLogo;
    private String estado;
    private String motivoDeshabilitacion;
    private AddressDto addressDto;
    private Double latitud;
    private Double longitud;
    private String nombreEncargado;
    private String dniEncargado;
    private String telefonoEncargado;

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public String getDniEncargado() {
        return dniEncargado;
    }

    public void setDniEncargado(String dniEncargado) {
        this.dniEncargado = dniEncargado;
    }

    public String getTelefonoEncargado() {
        return telefonoEncargado;
    }

    public void setTelefonoEncargado(String telefonoEncargado) {
        this.telefonoEncargado = telefonoEncargado;
    }

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

    public TipoComidaDto getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(TipoComidaDto tipoComida) {
        this.tipoComida = tipoComida;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getMotivoDeshabilitacion() {
        return motivoDeshabilitacion;
    }

    public void setMotivoDeshabilitacion(String motivoDeshabilitacion) {
        this.motivoDeshabilitacion = motivoDeshabilitacion;
    }
}
