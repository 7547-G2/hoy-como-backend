package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario getUsuarioById(Long id);
}
