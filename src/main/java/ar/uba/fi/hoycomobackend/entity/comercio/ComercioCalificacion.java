package ar.uba.fi.hoycomobackend.entity.comercio;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "comercio_calificacion")
@IdClass(ComercioCalificacionId.class)
public class ComercioCalificacion {

    @Id
    private Long comercioId;
    @Id
    private Long userId;

    private Integer calificacion;

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

}
