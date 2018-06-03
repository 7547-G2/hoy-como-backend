package ar.uba.fi.hoycomobackend.api.dto;

import java.util.List;

public class OrderDto {
    private Long id_plato;
    private Integer cantidad;
    private Double sub_total;
    private String obs;
    private List<Long> opcionales;

    public Long getId_plato() {
        return id_plato;
    }

    public void setId_plato(Long id_plato) {
        this.id_plato = id_plato;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSub_total() {
        return sub_total;
    }

    public void setSub_total(Double sub_total) {
        this.sub_total = sub_total;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<Long> getOpcionales() {
        return opcionales;
    }

    public void setOpcionales(List<Long> opcionales) {
        this.opcionales = opcionales;
    }
}
