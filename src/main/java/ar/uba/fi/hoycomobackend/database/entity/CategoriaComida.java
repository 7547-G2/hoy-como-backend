package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
@Entity
@Table(name = "categorias_comidas")
public class CategoriaComida {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "Descripcion")
    private String tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
