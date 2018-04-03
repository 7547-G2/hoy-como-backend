package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.comercio.ComercioCalificacion;
import ar.uba.fi.hoycomobackend.entity.comercio.ComercioCalificacionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComercioCalificacionRepository extends JpaRepository<ComercioCalificacion, ComercioCalificacionId> {

    List<ComercioCalificacion> getComercioCalificacionByComercioId(Long comercioId);
}
