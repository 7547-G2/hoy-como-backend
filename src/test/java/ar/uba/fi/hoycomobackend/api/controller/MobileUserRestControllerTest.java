package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.service.CategoriaComidaService;
import ar.uba.fi.hoycomobackend.api.service.MobileUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createMobileUserDto;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MobileUserRestController.class)
public class MobileUserRestControllerTest {

    private static String MOBILE_USER_API = "/api/mobileUser";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MobileUserService mobileUserService;
    @MockBean
    private CategoriaComidaService categoriaComidaService;

    @Test
    public void whenGetMobileUserList_thenReturnJsonArray() throws Exception {
        MobileUserDto mobileUserDto = createMobileUserDto(1L, "username", "firstName", "lastName");

        List<MobileUserDto> allMobileUserDto = Arrays.asList(mobileUserDto);

        given(mobileUserService.getMobileUserList()).willReturn(ResponseEntity.ok(allMobileUserDto));

        mockMvc.perform(get(MOBILE_USER_API)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(mobileUserDto.getUsername())))
                .andExpect(jsonPath("$[0].firstName", is(mobileUserDto.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(mobileUserDto.getLastName())));
    }

    @Test
    public void whenGetMobileUserById_thenReturnJsonArray() throws Exception {
        MobileUserDto mobileUserDto = createMobileUserDto(1L, "username", "firstName", "lastName");

        given(mobileUserService.getMobileUserById(1L)).willReturn(ResponseEntity.ok(mobileUserDto));

        mockMvc.perform(get(MOBILE_USER_API + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(mobileUserDto.getUsername())))
                .andExpect(jsonPath("$.firstName", is(mobileUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(mobileUserDto.getLastName())));
    }

    @Test
    public void whenAddMobileUser_thenReturnOk() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MobileUserDto mobileUserDto = createMobileUserDto(1L, "username", "firstName", "lastName");

        mockMvc.perform(post(MOBILE_USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mobileUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whengetMobileUserAuthorizedById_mobileUserAuthorized_thenReturnResponseEntity() throws Exception {
        given(mobileUserService.getMobileUserAuthorizedById(1L)).willReturn(ResponseEntity.ok("ok"));

        mockMvc.perform(get(MOBILE_USER_API + "/1/authorized")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
