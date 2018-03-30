package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.Comercio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ComercioRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComercioRepository employeeRepository;

    @Test
    public void whenFindByNombre_thenReturnComercio() {
        // given
        Comercio comercio = createComercio("comercio");
        entityManager.persist(comercio);
        entityManager.flush();

        // when
        Comercio found = employeeRepository.findByNombre(comercio.getNombre());

        // then
        assertThat(found.getNombre()).isEqualTo(comercio.getNombre());
    }

    private Comercio createComercio(String nombre) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        return comercio;
    }

}
