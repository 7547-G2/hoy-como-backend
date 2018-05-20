package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long facebook_id;
    private Long store_id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Orden> orden;
    private Double total;
    private String address;
    private String floor;
    private String dep;
    private String medioPago;
    private String nombreTC;
    private String numeroTC;
    private String fechaTC;
    private String codigoTC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Orden> getOrden() {
        return orden;
    }

    public void setOrden(List<Orden> orden) {
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