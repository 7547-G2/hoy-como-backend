package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
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

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-localprod.yml")
public class ComercioRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComercioRepository comercioRepository;

    @After
    public void tearDown() {
        comercioRepository.deleteAll();
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {

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
                .andExpect(jsonPath("$[0].tipo", is("tipo")))
                .andExpect(jsonPath("$[0].token", is("token")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }
}
