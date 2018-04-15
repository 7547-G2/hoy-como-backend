package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeHoyComoService;
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

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createComercioDto;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BackofficeHoyComoRestController.class)
public class BackofficeHoyComoRestControllerTest {

    private static String COMERCIOS_URL = "/api/comercios";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BackofficeHoyComoService backofficeHoyComoService;

    @Test
    public void givenComercios_whenGetComercios_thenReturnJsonArray() throws Exception {
        ComercioHoyComoDto comercioHoyComoDto = createComercioDto(1L, "email", "comercio");

        List<ComercioHoyComoDto> allComercios = Arrays.asList(comercioHoyComoDto);

        given(backofficeHoyComoService.getAllComercios()).willReturn(allComercios);

        mockMvc.perform(get(COMERCIOS_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(comercioHoyComoDto.getNombre())));
    }
}