package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long facebookId;
    private Long storeId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Orden> orden;
    private Double total;
    private String address;
    private String floor;
    private String dep;
    private Integer timeAccordingToDistance;
    private Double latitud;
    private Double longitud;
    private String medioPago;
    private String nombreTC;
    private String numeroTC;
    private String fechaTC;
    private String codigoTC;
    private String estado = "Ingresado";
    private String fecha;
    private Long startTime;
    private Long endTime;
    private String lastModified;
    private Date fechaInicioFacturacion;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Long facebookId) {
        this.facebookId = facebookId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTimeAccordingToDistance() {
        return timeAccordingToDistance;
    }

    public void setTimeAccordingToDistance(Integer timeAccordingToDistance) {
        this.timeAccordingToDistance = timeAccordingToDistance;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Date getFechaInicioFacturacion() {
        return fechaInicioFacturacion;
    }

    public void setFechaInicioFacturacion(Date fechaInicioFacturacion) {
        this.fechaInicioFacturacion = fechaInicioFacturacion;
    }
}
