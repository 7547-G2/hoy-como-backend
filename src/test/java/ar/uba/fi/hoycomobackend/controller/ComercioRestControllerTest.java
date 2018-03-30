package ar.uba.fi.hoycomobackend.controller;

import ar.uba.fi.hoycomobackend.entity.Comercio;
import ar.uba.fi.hoycomobackend.service.ComercioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.EntityTestBuilder.createComercio;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ComercioRestController.class)
public class ComercioRestControllerTest {

    private static String COMERCIOS_URL = "/api/comercios";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ComercioService comercioService;

    @Test
    public void givenComercios_whenGetComercios_thenReturnJsonArray() throws Exception {
        Comercio comercio = createComercio("comercio");

        List<Comercio> allComercios = Arrays.asList(comercio);

        given(comercioService.getAllComercios()).willReturn(allComercios);

        mockMvc.perform(get(COMERCIOS_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(comercio.getNombre())));
    }
}