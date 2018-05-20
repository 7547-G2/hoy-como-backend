package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tipo_comida")
// Para los comercios
public class TipoComida {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tipo;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tipoComida", cascade = CascadeType.ALL)
    private List<Comercio> comercio;

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


    public List<Comercio> getComercio() {
        return comercio;
    }

    public void setComercio(List<Comercio> comercio) {
        this.comercio = comercio;
    }
}
