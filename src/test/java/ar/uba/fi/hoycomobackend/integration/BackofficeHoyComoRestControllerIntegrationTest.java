package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoAddDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.database.entity.Address;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;
import ar.uba.fi.hoycomobackend.entity.DatabaseFiller;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@ActiveProfiles("localprod")
public class BackofficeHoyComoRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ComercioRepository comercioRepository;
    @Autowired
    private TipoComidaRepository tipoComidaRepository;

    @After
    public void tearDown() {
        comercioRepository.deleteAll();
        tipoComidaRepository.deleteAll();
    }

    @Test
    public void addNewComercios() throws Exception {
        ResultActions resultActions = createComercio();

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void addComercioWithExistingEmailSendsCorrectConstraintInformation() throws Exception {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        ComercioHoyComoAddDto comercioHoyComoAddDto = createDefaultComercioHoyComoAddDto();
        comercioHoyComoAddDto.setTipoComidaId(tipoComida.getId());
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(comercioHoyComoAddDto);

        mockMvc.perform(post("/api/comercios/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson));
        MvcResult mvcResult = mockMvc.perform(post("/api/comercios/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson))
                .andExpect(status().isPreconditionFailed())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).contains("Ya existe la llave (email)=");
    }

    @Test
    public void getCreatedComerciosWithoutFilters() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void getCreatedComerciosWithMatchingFilters() throws Exception {
        DatabaseFiller.createDefaultComercioInDatabase(comercioRepository, tipoComidaRepository);
        Long tipoComidaId = comercioRepository.findAll().get(0).getTipoComida().getId();

        mockMvc.perform(get("/api/comercios?search=tipoId:" + tipoComidaId + ",tipo:tipo,estado:estado,nombre:nombre,razonSocial:rAzOnSocIAL")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void getCreatedComerciosWithNonMatchingFiltersReturnsEmptyList() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/comercios?search=nombre:noMatchingNombre")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    public void updateExistingComercio() throws Exception {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComidaRepository.saveAndFlush(tipoComida);
        TipoComida newTipoComida = new TipoComida();
        newTipoComida.setTipo("newTipo");
        newTipoComida = tipoComidaRepository.saveAndFlush(newTipoComida);
        Comercio comercio = createTestComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        Long comercioId = comercio.getId();
        ComercioHoyComoAddDto comercioHoyComoAddDto = createDefaultComercioHoyComoAddDto();
        comercioHoyComoAddDto.setTipoComidaId(newTipoComida.getId());
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(comercioHoyComoAddDto);


        mockMvc.perform(put("/api/comercios/" + comercioId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$[0].tipoComida.tipo", is("newTipo")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void updateExistingComercioWithNullValuesDoesntUpdateNullValues() throws Exception {
        Comercio comercio = createTestComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        Long comercioId = comercio.getId();
        ComercioHoyComoAddDto comercioHoyComoAddDto = createDefaultComercioHoyComoAddDto();
        comercioHoyComoAddDto.setImagenLogo(null);
        comercioHoyComoAddDto.setEstado(null);
        comercioHoyComoAddDto.setTipoComidaId(tipoComida.getId());
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(comercioHoyComoAddDto);


        mockMvc.perform(put("/api/comercios/" + comercioId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$[0].tipoComida.tipo", is("tipo")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("anotherEstado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("anotherImagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    private void expectDefaultComercioGot() throws Exception {
        mockMvc.perform(get("/api/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("email")))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].razonSocial", is("razonSocial")))
                .andExpect(jsonPath("$[0].tipoComida.tipo", is("tipo")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    private ResultActions createComercio() throws Exception {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        ComercioHoyComoAddDto defaultComercioHoyComoAddDto = createDefaultComercioHoyComoAddDto();
        defaultComercioHoyComoAddDto.setTipoComidaId(tipoComida.getId());
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(defaultComercioHoyComoAddDto);

        return mockMvc.perform(post("/api/comercios/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson));
    }

    private Comercio createTestComercio() {
        Comercio comercio = new Comercio();
        Address address = new Address();
        address.setStreet("anotherStreet");
        address.setPostalCode("anotherPostalCode");
        address.setFloor("anotherFloor");
        address.setDepartment("anotherDepartment");
        comercio.setAddress(address);
        comercio.setEmail("anotherEmail");
        comercio.setNombre("anotherNombre");
        comercio.setRazonSocial("anotherRazonSocial");
        comercio.setToken("anotherToken");
        comercio.setPassword("anotherPassword");
        comercio.setEstado("anotherEstado");
        comercio.setImagenLogo("anotherImagenLogo");

        return comercio;
    }

}
