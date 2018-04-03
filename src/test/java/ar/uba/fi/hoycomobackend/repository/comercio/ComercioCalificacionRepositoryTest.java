package ar.uba.fi.hoycomobackend.repository.comercio;

import ar.uba.fi.hoycomobackend.entity.comercio.ComercioCalificacion;
import ar.uba.fi.hoycomobackend.repository.ComercioCalificacionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ComercioCalificacionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComercioCalificacionRepository comercioCalificacionRepository;

    @Test
    public void whenGetComercioCalificacionesByComercioId_thenReturnComercioCalificacioneList() {
        // given
        Long comercioId = 3L;
        ComercioCalificacion comercioCalificacion = createComercioCalificacion(1L, comercioId, 5);
        entityManager.persist(comercioCalificacion);
        comercioCalificacion = createComercioCalificacion(2L, comercioId, 4);
        entityManager.persist(comercioCalificacion);
        entityManager.flush();

        // when
        List<ComercioCalificacion> comercioCalificacionList = comercioCalificacionRepository.getComercioCalificacionByComercioId(comercioId);

        // then
        assertThat(comercioCalificacionList).hasSize(2);
    }

    private ComercioCalificacion createComercioCalificacion(Long userId, Long comercioId, Integer calificacion) {
        ComercioCalificacion comercioCalificacion = new ComercioCalificacion();
        comercioCalificacion.setUserId(userId);
        comercioCalificacion.setComercioId(comercioId);
        comercioCalificacion.setCalificacion(calificacion);

        return comercioCalificacion;
    }
}
