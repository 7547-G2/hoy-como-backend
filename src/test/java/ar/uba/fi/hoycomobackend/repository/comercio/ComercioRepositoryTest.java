package ar.uba.fi.hoycomobackend.repository.comercio;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
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
public class ComercioRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComercioRepository comercioRepository;

    @Test
    public void whenFindByNombre_thenReturnComercio() {
        // given
        String nombreComercio = "comercio";
        Comercio comercio = createComercio(nombreComercio);
        entityManager.persist(comercio);
        comercio = createComercio("segundoComercio");
        entityManager.persist(comercio);
        entityManager.flush();

        // when
        List<Comercio> comercioList = comercioRepository.findByNombre(nombreComercio);

        // then
        assertThat(comercioList).hasSize(1);
        Comercio found = comercioList.get(0);
        assertThat(found.getNombre()).isEqualTo(nombreComercio);
    }

    private Comercio createComercio(String nombre) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        return comercio;
    }

}
