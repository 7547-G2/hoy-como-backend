package ar.uba.fi.hoycomobackend.database.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DynamicInsert
@Table(name = "plato")
public class Plato {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    @Column(columnDefinition = "text")
    private String imagen;
    private Float precio;
    @Column(length = 32, columnDefinition = "varchar(32) default 'ACTIVO'")
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private PlatoState state = PlatoState.ACTIVO;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public PlatoState getState() {
        return state;
    }

    public void setState(PlatoState state) {
        this.state = state;
    }
}
