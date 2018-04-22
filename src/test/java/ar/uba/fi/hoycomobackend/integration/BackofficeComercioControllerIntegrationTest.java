package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultPlatoDto;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-localprod.yml")
public class BackofficeComercioControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ComercioRepository comercioRepository;


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
                .andExpect(jsonPath("$[1].nombre", is("nombre")))
                .andExpect(jsonPath("$[1].imagen", is("imagen")))
                .andExpect(jsonPath("$[1].precio", is(1.0)));
    }


    @Test
    public void updateExistingPlato() throws Exception {
        Long comercioId = createDefaultComercioInDatabase();
        String platoId = createComercioWithPlato(comercioId, createTestPlato()).andReturn().getResponse().getContentAsString();
        PlatoDto platoDto = createDefaultPlatoDto();
        String platoDtoJson = objectMapper.writeValueAsString(platoDto);

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
                .andExpect(jsonPath("$[0].precio", is(1.0)));
    }

    private PlatoDto createTestPlato() {
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
