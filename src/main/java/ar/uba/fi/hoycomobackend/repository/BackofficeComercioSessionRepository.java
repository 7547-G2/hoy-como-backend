package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.backoffice.comercio.BackofficeComercioSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackofficeComercioSessionRepository extends JpaRepository<BackofficeComercioSession, String> {

    Optional<BackofficeComercioSession> getBackofficeComercioSessionByEmail(String email);
}
