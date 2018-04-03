package ar.uba.fi.hoycomobackend.entity.comercio;

import javax.persistence.*;

@Entity
@Table(name = "comercio_delivery_time")
@IdClass(ComercioDeliveryTimeId.class)
public class ComercioDeliveryTime {

    @Id
    @GeneratedValue
    private Long id;

    @Id
    private Long comercioId;

    private Integer deliveryTimeInMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public Integer getDeliveryTimeInMinutes() {
        return deliveryTimeInMinutes;
    }

    public void setDeliveryTimeInMinutes(Integer deliveryTimeInMinutes) {
        this.deliveryTimeInMinutes = deliveryTimeInMinutes;
    }
}
