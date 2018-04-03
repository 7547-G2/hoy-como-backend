package ar.uba.fi.hoycomobackend.repository.usuario;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.entity.usuario.Usuario;
import ar.uba.fi.hoycomobackend.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.EntityTestBuilder.createUsuario;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UsuarioRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void whenFindByNombre_thenReturnComercio() {
        // given
        Usuario usuario = createUsuario("nombre", "apellido", "mail");
        Long firsUserId = (Long) entityManager.persistAndGetId(usuario);
        usuario = createUsuario("otroNombre", "otroApellido", "otroMail");
        entityManager.persist(usuario);
        entityManager.flush();

        // when
        Usuario usuarioFound = usuarioRepository.getUsuarioById(firsUserId);

        // then
        assertThat(usuarioFound.getNombre()).isEqualTo("nombre");
    }

}
