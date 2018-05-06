package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
@Entity
@Table(name = "categorias_comida")
public class CategoriaComida {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
