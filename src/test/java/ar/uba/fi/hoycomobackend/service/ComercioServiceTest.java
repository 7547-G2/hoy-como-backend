package ar.uba.fi.hoycomobackend.service;

import ar.uba.fi.hoycomobackend.entity.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import static ar.uba.fi.hoycomobackend.entity.EntityTestBuilder.createComercio;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ComercioServiceTest {

    private static ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private ComercioService comercioService = new ComercioService(comercioRepository);

    @Before
    public void setUp() {
        Comercio comercio = createComercio("comercio");

        Mockito.when(comercioRepository.findByNombre(comercio.getNombre())).thenReturn(comercio);
    }

    @Test
    public void whenValidName_thenComercioShouldBeFound() {
        String nombre = "comercio";
        Comercio found = comercioService.getComercioByNombre(nombre);

        assertThat(found.getNombre()).isEqualTo(nombre);
    }
}