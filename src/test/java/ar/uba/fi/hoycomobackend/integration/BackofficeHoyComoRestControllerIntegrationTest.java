package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.database.entity.Address;
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
import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercioHoyComoDto;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-localprod.yml")
public class BackofficeHoyComoRestControllerIntegrationTest {

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
    public void addNewComercios() throws Exception {
        ResultActions resultActions = createComercio();

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getCreatedComercios() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercioRepository.saveAndFlush(comercio);

        expectDefaultComercioGot();
    }

    @Test
    public void updateExistingComercio() throws Exception {
        Comercio comercio = createTestComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        Long comercioId = comercio.getId();
        ComercioHoyComoDto comercioHoyComoDto = createDefaultComercioHoyComoDto();
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(comercioHoyComoDto);


        mockMvc.perform(put("/api/comercios/" + comercioId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson))
                .andExpect(status().isOk());

        expectDefaultComercioGot();
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
                .andExpect(jsonPath("$[0].tipo", is("tipo")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    private ResultActions createComercio() throws Exception {
        ComercioHoyComoDto comercioHoyComoDto = createDefaultComercioHoyComoDto();
        String comercioHoyComoDtoJson = objectMapper.writeValueAsString(comercioHoyComoDto);

        return mockMvc.perform(post("/api/comercios/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(comercioHoyComoDtoJson));
    }

    private Comercio createTestComercio() {
        Address address = new Address();
        address.setStreet("anotherStreet");
        address.setPostalCode("anotherPostalCode");
        address.setFloor("anotherFloor");
        address.setDepartment("anotherDepartment");
        Comercio comercio = new Comercio();
        comercio.setEmail("anotherEmail");
        comercio.setNombre("anotherNombre");
        comercio.setRazonSocial("anotherRazonSocial");
        comercio.setTipo("anotherTipo");
        comercio.setToken("anotherToken");
        comercio.setPassword("anotherPassword");
        comercio.setEstado("anotherEstado");
        comercio.setImagenLogo("anotherImagenLogo");
        comercio.setAddress(address);

        return comercio;
    }

}
