package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    Comercio findByNombre(String nombre);
}
