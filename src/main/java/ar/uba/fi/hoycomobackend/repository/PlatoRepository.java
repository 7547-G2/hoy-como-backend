package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
}
