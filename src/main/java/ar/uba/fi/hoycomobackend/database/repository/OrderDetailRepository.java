package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
