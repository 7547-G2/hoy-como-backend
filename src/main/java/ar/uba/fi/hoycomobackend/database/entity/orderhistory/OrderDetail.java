package ar.uba.fi.hoycomobackend.database.entity.orderhistory;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long pedidoId;
    private Long comercioId;
    private String storeName;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderDetail", cascade = CascadeType.ALL)
    private Set<OrderStatusHistory> statusHistory;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderDetail", cascade = CascadeType.ALL)
    private Set<OrderContent> orderContent;
    private Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Set<OrderStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(Set<OrderStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public Set<OrderContent> getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(Set<OrderContent> orderContent) {
        this.orderContent = orderContent;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
