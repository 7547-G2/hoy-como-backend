package ar.uba.fi.hoycomobackend.repository.comercio;

import ar.uba.fi.hoycomobackend.entity.comercio.ComercioDeliveryTime;
import ar.uba.fi.hoycomobackend.repository.ComercioDeliveryTimeRepository;
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
public class ComercioLeadTimeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComercioDeliveryTimeRepository comercioDeliveryTimeRepository;

    @Test
    public void whenGetComercioDeliveryTimesByComercioId_thenReturnComercioDeliveryTimeList() {
        // given
        Long comercioId = 3L;
        ComercioDeliveryTime comercioDeliveryTime = createComercioDeliveryTime(comercioId, 5);
        entityManager.persist(comercioDeliveryTime);
        entityManager.flush();
        comercioDeliveryTime = createComercioDeliveryTime(comercioId, 15);
        entityManager.persist(comercioDeliveryTime);
        entityManager.flush();

        // when
        List<ComercioDeliveryTime> comercioCalificacionList = comercioDeliveryTimeRepository.getComercioDeliveryTimesByComercioId(comercioId);

        // then
        assertThat(comercioCalificacionList).hasSize(2);
    }

    private ComercioDeliveryTime createComercioDeliveryTime(Long comercioId, Integer timeInMinutes) {
        ComercioDeliveryTime comercioDeliveryTime = new ComercioDeliveryTime();
        comercioDeliveryTime.setComercioId(comercioId);
        comercioDeliveryTime.setDeliveryTimeInMinutes(timeInMinutes);

        return comercioDeliveryTime;
    }
}
