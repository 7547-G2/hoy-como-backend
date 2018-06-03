package ar.uba.fi.hoycomobackend.api.dto;

import java.util.List;

public class PostPedidoDto {
    private Long facebook_id;
    private Long store_id;
    private List<OrderDto> orden;
    private Double total;
    private String address;
    private String floor;
    private String dep;
    private Double lat;
    private Double lng;
    private String medioPago;
    private String nombreTC;
    private String numeroTC;
    private String fechaTC;
    private String codigoTC;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(Long facebook_id) {
        this.facebook_id = facebook_id;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public List<OrderDto> getOrden() {
        return orden;
    }

    public void setOrden(List<OrderDto> orden) {
        this.orden = orden;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getNombreTC() {
        return nombreTC;
    }

    public void setNombreTC(String nombreTC) {
        this.nombreTC = nombreTC;
    }

    public String getNumeroTC() {
        return numeroTC;
    }

    public void setNumeroTC(String numeroTC) {
        this.numeroTC = numeroTC;
    }

    public String getFechaTC() {
        return fechaTC;
    }

    public void setFechaTC(String fechaTC) {
        this.fechaTC = fechaTC;
    }

    public String getCodigoTC() {
        return codigoTC;
    }

    public void setCodigoTC(String codigoTC) {
        this.codigoTC = codigoTC;
    }
}
