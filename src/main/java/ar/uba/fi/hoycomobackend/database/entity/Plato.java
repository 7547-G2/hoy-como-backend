package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;

@Entity
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
}