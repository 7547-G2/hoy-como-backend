package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long>, JpaSpecificationExecutor<Comercio> {

    List<Comercio> findByNombre(String nombre);

    Optional<Comercio> getComercioByEmail(String email);

    Optional<Comercio> getComercioById(Long id);
}
