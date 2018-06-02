package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.OpcionDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoUpdateDto;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.repository.*;
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

import java.util.Arrays;
import java.util.Optional;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
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
    @Autowired
    private TipoComidaRepository tipoComidaRepository;
    @Autowired
    private CategoriaComidaRepository categoriaComidaRepository;
    @Autowired
    private OpcionRepository opcionRepository;

    @After
    public void tearDown() {
        categoriaComidaRepository.deleteAll();
        platoRepository.deleteAll();
        comercioRepository.deleteAll();
        tipoComidaRepository.deleteAll();
        opcionRepository.deleteAll();
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
        Long categoriaComidaId = createDefaultCategoriaComidaInDatabase();
        createComercioWithPlato(comercioId, categoriaComidaId);
        createComercioWithPlato(comercioId, categoriaComidaId);

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].imagen", is("imagen")))
                .andExpect(jsonPath("$[0].precio", is(1.0)))
                .andExpect(jsonPath("$[0].state", is("ACTIVO")))
                .andExpect(jsonPath("$[0].categoria", is(categoriaComidaId.intValue())))
                .andExpect(jsonPath("$[0].descCategoria", is("descCategoria")))
                .andExpect(jsonPath("$[0].orden", isOneOf(1, 2)))
                .andExpect(jsonPath("$[1].nombre", is("nombre")))
                .andExpect(jsonPath("$[1].imagen", is("imagen")))
                .andExpect(jsonPath("$[1].precio", is(1.0)))
                .andExpect(jsonPath("$[1].state", is("ACTIVO")))
                .andExpect(jsonPath("$[1].categoria", is(categoriaComidaId.intValue())))
                .andExpect(jsonPath("$[1].descCategoria", is("descCategoria")))
                .andExpect(jsonPath("$[1].orden", isOneOf(1, 2)));
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
                .andExpect(jsonPath("$[0].precio", is(50.0)))
                .andExpect(jsonPath("$[0].state", is("ACTIVO")))
                .andExpect(jsonPath("$[0].categoria", is(1)))
                .andExpect(jsonPath("$[0].orden", is(1)));
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
    public void getPlatosOnlyReturnPlatosThatArentDeleted() throws Exception {
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

    @Test
    public void getComercioById() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("email")))
                .andExpect(jsonPath("$.nombre", is("nombre")))
                .andExpect(jsonPath("$.razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$.tipoComidaId", notNullValue()))
                .andExpect(jsonPath("$.estado", is("habilitado")))
                .andExpect(jsonPath("$.imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$.addressDto.street", is("street")))
                .andExpect(jsonPath("$.addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$.addressDto.floor", is("floor")))
                .andExpect(jsonPath("$.addressDto.department", is("department")));
    }

    @Test
    public void setComercioTipoComidaAndThenFindIt() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        String commerceType = "commerceType";

        mockMvc.perform(post("/api/backofficeComercio/" + comercioId + "/tipoComidaComercio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commerceType));

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/tipoComidaComercio")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tipo", is(commerceType)));
    }

    @Test
    public void getTipoComercios() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();

        mockMvc.perform(get("/api/backofficeComercio/tipoComidaComercio")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tipo", is("tipoComidaComercio")))
                .andExpect(jsonPath("$[0].comercioId[0]", is(comercioId.intValue())));
    }

    @Test
    public void setCategoriaComidaToAComercioAndThenFindIt() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        String commerceType = "commerceType";

        mockMvc.perform(post("/api/backofficeComercio/" + comercioId + "/categoriasComida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(commerceType));

        mockMvc.perform(get("/api/backofficeComercio/" + comercioId + "/categoriasComida")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].tipo", is(commerceType)))
                .andExpect(jsonPath("$[0].orderPriority", is(1)))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    public void createOpcionAppearsAsIs() throws Exception {
        OpcionDto opcionDto = createDefaultOpcionDto();
        String opcionDtoJson = objectMapper.writeValueAsString(opcionDto);

        mockMvc.perform(post("/api/backofficeComercio/comercio/1/opcion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(opcionDtoJson));

        Long opcionId = opcionRepository.findAll().get(0).getId();
        opcionDto.setState("anotherState");
        opcionDtoJson = objectMapper.writeValueAsString(opcionDto);

        mockMvc.perform(put("/api/backofficeComercio/comercio/opcion/" + opcionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(opcionDtoJson));

        mockMvc.perform(get("/api/backofficeComercio/opcion/" + opcionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre", is("nombre")))
                .andExpect(jsonPath("$.precio", is(12.34)))
                .andExpect(jsonPath("$.state", is("anotherState")));
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

    private ResultActions createComercioWithPlato(Long comercioId, Long categoriaComidaId) throws Exception {
        PlatoDto platoDto = createDefaultPlatoDto();
        platoDto.setCategoria(categoriaComidaId);
        return createComercioWithPlato(comercioId, platoDto);
    }

    private ResultActions createComercioWithPlato(Long comercioId, PlatoDto platoDto) throws Exception {
        String platoDtoJson = objectMapper.writeValueAsString(platoDto);

        return mockMvc.perform(post("/api/backofficeComercio/" + comercioId + "/platos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(platoDtoJson));
    }

    private Long createDefaultComercioInDatabase() {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipoComidaComercio");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        Comercio comercio = createDefaultComercio();
        tipoComida.setComercio(Arrays.asList(comercio));
        comercio.setTipoComida(tipoComida);
        comercio = comercioRepository.saveAndFlush(comercio);
        return comercio.getId();
    }

    private Long createDefaultCategoriaComidaInDatabase() {
        CategoriaComida categoriaComida = new CategoriaComida();
        categoriaComida.setTipo("descCategoria");
        categoriaComida = categoriaComidaRepository.saveAndFlush(categoriaComida);

        return categoriaComida.getId();
    }
}
