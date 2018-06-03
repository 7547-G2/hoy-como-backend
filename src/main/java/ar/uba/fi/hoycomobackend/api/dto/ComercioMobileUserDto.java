package ar.uba.fi.hoycomobackend.api.dto;

public class ComercioMobileUserDto {

    private Long id;
    private String nombre;
    private TipoComidaDto tipoComida;
    private String imagenLogo;
    private String estado;
    private String rating;
    private String leadTime;
    private String precioMinimo;
    private String precioMaximo;
    private Integer descuento;
    private AddressDto addressDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public String getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(String precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public String getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(String precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public TipoComidaDto getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(TipoComidaDto tipoComida) {
        this.tipoComida = tipoComida;
    }

    public ComercioMobileUserDto removeNulls() {
        if (id == null)
            id = 0L;
        if (nombre == null)
            nombre = "";
        if (tipoComida == null) {
            tipoComida = new TipoComidaDto();
            tipoComida = tipoComida.removeNulls();
        }
        if (imagenLogo == null)
            imagenLogo = "";
        if (estado == null)
            estado = "";
        if (rating == null)
            rating = "";
        if (leadTime == null)
            leadTime = "";
        if (precioMinimo == null)
            precioMinimo = "";
        if (precioMaximo == null)
            precioMaximo = "";
        if (addressDto == null) {
            addressDto = new AddressDto();
            addressDto = addressDto.removeNulls();
        }

        return this;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }
}
