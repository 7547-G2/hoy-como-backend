package ar.uba.fi.hoycomobackend.database.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DynamicInsert
@Table(name = "comercio")
public class Comercio {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", unique = true)
    @NotNull
    private String email;
    @Column(columnDefinition = "varchar(256) default ''")
    private String nombre;
    @Column(columnDefinition = "varchar(256) default ''")
    private String razonSocial;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "tipo_comida_id")
    private TipoComida tipoComida;
    private String token = "";
    private Integer leadTime = 0;
    private Float precioMinimo = 0.0f;
    private Float precioMaximo = 0.0f;
    @Column(columnDefinition = "INTEGER default 0")
    private Integer totalPedidos = 0;
    @Column(columnDefinition = "float(2) default 1.00")
    private Float rating;
    @NotNull
    private String password;
    @Column(columnDefinition = "text default ''")
    private String imagenLogo;
    @Column(columnDefinition = "varchar(128) default 'deshabilitado'")
    private String estado;
    private String motivoDeshabilitacion = "";
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "favoriteComercios")
    private Set<MobileUser> mobileUserList;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    private Double latitud;
    private Double longitud;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comercio", cascade = CascadeType.ALL)
    private Set<Plato> platos;
    private Integer descuento = 0;
    @Column(columnDefinition = "text default ''")
    private String imagenComercio;
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

    public Set<MobileUser> getMobileUserList() {
        return mobileUserList;
    }

    public void setMobileUserList(Set<MobileUser> mobileUserList) {
        this.mobileUserList = mobileUserList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(Set<Plato> platos) {
        this.platos = platos;
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

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Float getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Float precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public Float getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(Float precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public Integer getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Integer totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public TipoComida getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(TipoComida tipoComida) {
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

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public String getImagenComercio() {
        return imagenComercio;
    }

    public void setImagenComercio(String imagenComercio) {
        this.imagenComercio = imagenComercio;
    }

    public String getMotivoDeshabilitacion() {
        return motivoDeshabilitacion;
    }

    public void setMotivoDeshabilitacion(String motivoDeshabilitacion) {
        this.motivoDeshabilitacion = motivoDeshabilitacion;
    }
}
