package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    List<Comercio> findByNombre(String nombre);
}
