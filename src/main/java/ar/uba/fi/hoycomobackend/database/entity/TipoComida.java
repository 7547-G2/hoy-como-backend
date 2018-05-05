package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tipo_comida")
public class TipoComida {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tipo;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tipoComida")
    private Set<Comercio> comercioSet;

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

    public Set<Comercio> getComercioSet() {
        return comercioSet;
    }

    public void setComercioSet(Set<Comercio> comercioSet) {
        this.comercioSet = comercioSet;
    }
}
