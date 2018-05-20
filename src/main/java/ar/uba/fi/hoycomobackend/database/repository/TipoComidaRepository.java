package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoComidaRepository extends JpaRepository<TipoComida, Long> {
    Optional<TipoComida> findByComercio_Id(Long comercioId);
}
