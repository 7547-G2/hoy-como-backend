package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orden")
public class Orden {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long id_plato;
    private Integer cantidad;
    private Double sub_total;
    private String obs;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> opcionales;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
