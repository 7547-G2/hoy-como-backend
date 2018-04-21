package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "comercio")
public class Comercio {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", unique = true)
    @NotNull
    private String email;
    @Size(min = 3, max = 128)
    private String nombre;
    private String razonSocial;
    private String tipo;
    private String token;
    private Integer leadTime;
    private Float precioMinimo;
    private Float precioMaximo;
    private Integer totalPedidos;
    private Float rating;
    @NotNull
    private String password;
    @Column(columnDefinition = "text")
    private String imagenLogo;
    @Column(columnDefinition = "varchar(128) default 'deshabilitado'")
    private String estado;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "favoriteComercios")
    private Set<MobileUser> mobileUserList;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "comercio", cascade = CascadeType.ALL)
    private Set<Plato> platos;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
