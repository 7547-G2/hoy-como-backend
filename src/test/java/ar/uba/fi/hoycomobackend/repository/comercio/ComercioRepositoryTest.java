package ar.uba.fi.hoycomobackend.repository.comercio;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
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
        Comercio firstComercio = createDefaultComercio();
        entityManager.persist(firstComercio);
        Comercio secondComercio = createDefaultComercio();
        secondComercio.setEmail("anotherEmail");
        secondComercio.setNombre("anotherNombre");
        entityManager.persist(secondComercio);
        entityManager.flush();

        List<Comercio> comercioList = comercioRepository.findByNombre("nombre");

        assertThat(comercioList).hasSize(1);
        Comercio found = comercioList.get(0);
        assertThat(found.getNombre()).isEqualTo("nombre");
    }

}
