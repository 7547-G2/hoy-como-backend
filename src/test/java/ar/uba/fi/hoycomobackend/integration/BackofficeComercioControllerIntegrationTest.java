package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoUpdateDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.PlatoState;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@ActiveProfiles("localprod")
public class BackofficeComercioControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ComercioRepository comercioRepository;
    @Autowired
    private PlatoRepository platoRepository;


    @After
    public void tearDown() {
        comercioRepository.deleteAll();
    }

    @Test
    public void addNewPlatoToExistingComercio() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        ResultActions resultActions = createComercioWithPlato(comercioId);

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void addNewPlatoToExistingComercioUpdatesComercioPrices() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        createComercioWithPlato(comercioId);
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        assertThat(comercioOptional.isPresent()).isTrue();
        Comercio comercio = comercioOptional.get();
        assertThat(comercio.getPrecioMaximo()).isEqualTo(1.0f);
        assertThat(comercio.getPrecioMinimo()).isEqualTo(1.0f);
    }

    @Test
    public void getAllPlatosFromExistingComercio() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        createComercioWithPlato(comercioId);
        createComercioWithPlato(comercioId);

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].imagen", is("imagen")))
                .andExpect(jsonPath("$[0].precio", is(1.0)))
                .andExpect(jsonPath("$[0].state", is("ACTIVO")))
                .andExpect(jsonPath("$[0].categoria", is(1)))
                .andExpect(jsonPath("$[0].orden", is(1)))
                .andExpect(jsonPath("$[1].nombre", is("nombre")))
                .andExpect(jsonPath("$[1].imagen", is("imagen")))
                .andExpect(jsonPath("$[1].precio", is(1.0)))
                .andExpect(jsonPath("$[1].state", is("ACTIVO")))
                .andExpect(jsonPath("$[1].categoria", is(1)))
                .andExpect(jsonPath("$[1].orden", is(1)));
    }


    @Test
    public void updateExistingPlato() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        String platoId = createComercioWithPlato(comercioId, createTestPlatoDto()).andReturn().getResponse().getContentAsString();
        PlatoUpdateDto platoUpdateDto = createDefaultPlatoUpdateDto();
        platoUpdateDto.setPrecio(50.0f);
        String platoDtoJson = objectMapper.writeValueAsString(platoUpdateDto);

        mockMvc.perform(put("/api/backofficeComercio/" + comercioId + "/platos/" + platoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(platoDtoJson));

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].imagen", is("imagen")))
                .andExpect(jsonPath("$[0].precio", is(50.0)));
    }

    @Test
    public void updateExistingPlatoWithNullValuesDoesntUpdateNullValues() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        String platoId = createComercioWithPlato(comercioId, createTestPlatoDto()).andReturn().getResponse().getContentAsString();
        PlatoUpdateDto platoUpdateDto = createDefaultPlatoUpdateDto();
        platoUpdateDto.setPrecio(null);
        String platoDtoJson = objectMapper.writeValueAsString(platoUpdateDto);

        mockMvc.perform(put("/api/backofficeComercio/" + comercioId + "/platos/" + platoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(platoDtoJson));

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].imagen", is("imagen")))
                .andExpect(jsonPath("$[0].precio", is(2.0)));
    }

    @Test
    public void updateExistingPlatoUpdatesComercioPrices() throws Exception {
        updateExistingPlato();
        Comercio comercio = comercioRepository.findAll().get(0);

        assertThat(comercio.getPrecioMaximo()).isEqualTo(50.0f);
        assertThat(comercio.getPrecioMinimo()).isEqualTo(50.0f);
    }

    @Test
    public void getPlatosOnlyReturnNonDeletedPlatos() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        Comercio comercio = comercioRepository.getComercioById(comercioId).get();
        Plato platoActivated = createDefaultPlato();
        platoActivated.setComercio(comercio);
        Plato platoDeactivated = createDefaultPlato();
        platoDeactivated.setState(PlatoState.INACTIVO);
        platoDeactivated.setComercio(comercio);
        Plato platoDeleted = createDefaultPlato();
        platoDeleted.setState(PlatoState.BORRADO);
        platoDeleted.setComercio(comercio);
        platoRepository.saveAndFlush(platoActivated);
        platoRepository.saveAndFlush(platoDeactivated);
        platoRepository.saveAndFlush(platoDeleted);

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private PlatoDto createTestPlatoDto() {
        PlatoDto platoDto = new PlatoDto();
        platoDto.setPrecio(2.0f);
        platoDto.setNombre("otherName");
        platoDto.setImagen("otherImage");

        return platoDto;
    }

    private ResultActions createComercioWithPlato(Long comercioId) throws Exception {
        PlatoDto platoDto = createDefaultPlatoDto();
        return createComercioWithPlato(comercioId, platoDto);
    }

    private ResultActions createComercioWithPlato(Long comercioId, PlatoDto platoDto) throws Exception {
        String platoDtoJson = objectMapper.writeValueAsString(platoDto);

        return mockMvc.perform(post("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(platoDtoJson));
    }

    private Long createDefaultComercioInDatabase() {
        Comercio comercio = createDefaultComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        return comercio.getId();
    }


}
