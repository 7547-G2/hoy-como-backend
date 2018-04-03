package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.comercio.ComercioDeliveryTime;
import ar.uba.fi.hoycomobackend.entity.comercio.ComercioDeliveryTimeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComercioDeliveryTimeRepository extends JpaRepository<ComercioDeliveryTime, ComercioDeliveryTimeId> {

    List<ComercioDeliveryTime> getComercioDeliveryTimesByComercioId(Long comercioId);
}
