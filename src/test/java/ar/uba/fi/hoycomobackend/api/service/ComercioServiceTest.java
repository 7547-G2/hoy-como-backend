package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.EntityTestBuilder.createComercio;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ComercioServiceTest {

    private static Long COMERCIO_ID = 1L;
    private static String COMERCIO_NOMBRE = "comercio";
    private static String COMERCIO_EMAIL = "email";
    private static ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private ComercioService comercioService = new ComercioService(comercioRepository);

    @Before
    public void setUp() {
        List<Comercio> comercioList = Arrays.asList(createComercio(COMERCIO_ID, COMERCIO_EMAIL, COMERCIO_NOMBRE));

        Mockito.when(comercioRepository.findByNombre(COMERCIO_NOMBRE)).thenReturn(comercioList);
    }

    @Test
    public void whenValidName_thenComercioShouldBeFound() {
        List<Comercio> comercioList = comercioService.getComercioByNombre(COMERCIO_NOMBRE);
        Comercio found = comercioList.get(0);

        assertThat(found.getNombre()).isEqualTo(COMERCIO_NOMBRE);
    }
}