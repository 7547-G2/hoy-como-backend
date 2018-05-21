package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoUpdateDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("localprod")
public class BackofficeComercioServiceTest {


    @Autowired
    private ComercioRepository comercioRepository;
    @Autowired
    private PlatoRepository platoRepository;
    @Autowired
    private BackofficeComercioService backofficeComercioService;
    private Long comercioId;

    @Before
    public void setUp() {
        Comercio comercio = createDefaultComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        comercioId = comercio.getId();
    }

    @After
    public void tearDown() {
        comercioRepository.deleteAll();
        platoRepository.deleteAll();
    }

    @Test
    public void testAddPlatoAcceptsCategoriaAndOrden() throws JsonProcessingException {
        PlatoDto platoDto = createDefaultPlatoDto();
        backofficeComercioService.addPlatoToComercio(comercioId, platoDto);
        Comercio comercio = comercioRepository.findAll().get(0);
        Plato plato = comercio.getPlatos().iterator().next();

        assertThat(plato.getCategoria()).isEqualTo(1L);
        assertThat(plato.getOrden()).isEqualTo(1);
    }

    @Test
    public void testUpdatePlatoOnlyChangingCategory() throws JsonProcessingException {
        PlatoDto platoDto = createDefaultPlatoDto();
        PlatoUpdateDto platoUpdateDto = createDefaultPlatoUpdateDto();
        platoUpdateDto.setCategoria(2L);
        platoUpdateDto.setOrden(2);
        Comercio comercio = comercioRepository.findAll().get(0);
        backofficeComercioService.addPlatoToComercio(comercio.getId(), platoDto);
        comercio = comercioRepository.findAll().get(0);
        Plato plato = comercio.getPlatos().iterator().next();
        backofficeComercioService.updatePlatoFromComercio(comercio.getId(), plato.getId(), platoUpdateDto);
        plato = comercioRepository.getComercioById(comercio.getId()).get().getPlatos().iterator().next();

        assertThat(plato.getCategoria()).isEqualTo(2L);
        assertThat(plato.getOrden()).isEqualTo(1);
    }

    @Test
    public void testUpdatePlatoThatOnlyChangesOrden() throws JsonProcessingException {
        PlatoDto platoDto = createDefaultPlatoDto();
        PlatoUpdateDto platoUpdateDto = createDefaultPlatoUpdateDto();
        platoUpdateDto.setOrden(2);
        Comercio comercio = comercioRepository.findAll().get(0);
        backofficeComercioService.addPlatoToComercio(comercio.getId(), platoDto);
        comercio = comercioRepository.findAll().get(0);
        Plato plato = comercio.getPlatos().iterator().next();
        backofficeComercioService.updatePlatoFromComercio(comercio.getId(), plato.getId(), platoUpdateDto);
        plato = comercioRepository.getComercioById(comercio.getId()).get().getPlatos().iterator().next();

        assertThat(plato.getCategoria()).isEqualTo(1L);
        assertThat(plato.getOrden()).isEqualTo(2);
    }
}
