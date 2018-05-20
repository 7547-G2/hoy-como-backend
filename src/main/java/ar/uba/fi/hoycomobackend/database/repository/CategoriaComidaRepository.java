package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.CategoriaComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaComidaRepository extends JpaRepository<CategoriaComida, Long> {

    Optional<CategoriaComida> getByComercioId(Long comercioId);
}
