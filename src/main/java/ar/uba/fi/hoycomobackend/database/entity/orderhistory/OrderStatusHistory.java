package ar.uba.fi.hoycomobackend.database.entity.orderhistory;

import javax.persistence.*;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String status;
    private String date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_detail_id_history")
    private OrderDetail orderDetail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
