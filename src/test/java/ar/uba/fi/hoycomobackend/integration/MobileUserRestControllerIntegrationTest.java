package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.entity.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultMobileUser;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-localprod.yml")
public class MobileUserRestControllerIntegrationTest {

    private static String DEPARTMENT = "department";
    private static String FLOOR = "floor";
    private static String POSTAL_CODE = "postalCode";
    private static String STREET = "street";
    private static Long FACEBOOK_ID = 1L;
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String USER_NAME = "userName";
    private static String STATE = "state";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MobileUserRepository mobileUserRepository;
    @Autowired
    private ComercioRepository comercioRepository;

    @After
    public void tearDown() {
        mobileUserRepository.deleteAll();
        comercioRepository.deleteAll();
    }

    @Test
    public void creatingNewMobileUserTest() throws Exception {
        ResultActions resultActions = createPostNewMobileUser();

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isEqualTo("Se agregó usuario exitosamente");
    }

    @Test
    public void gettingNewlyCreatedMobileUserTest() throws Exception {
        createPostNewMobileUser();

        mockMvc.perform(get("/api/mobileUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].facebookId", is(FACEBOOK_ID.intValue())))
                .andExpect(jsonPath("$[0].username", is(USER_NAME)))
                .andExpect(jsonPath("$[0].firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$[0].lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$[0].state", is(STATE)))
                .andExpect(jsonPath("$[0].addressDto.street", is(STREET)))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is(POSTAL_CODE)))
                .andExpect(jsonPath("$[0].addressDto.floor", is(FLOOR)))
                .andExpect(jsonPath("$[0].addressDto.department", is(DEPARTMENT)));
    }

    @Test
    public void gettingNewlyCreatedMobileUserByIdTest() throws Exception {
        createPostNewMobileUser();

        mockMvc.perform(get("/api/mobileUser/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.facebookId", is(FACEBOOK_ID.intValue())))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.state", is(STATE)))
                .andExpect(jsonPath("$.addressDto.street", is(STREET)))
                .andExpect(jsonPath("$.addressDto.postalCode", is(POSTAL_CODE)))
                .andExpect(jsonPath("$.addressDto.floor", is(FLOOR)))
                .andExpect(jsonPath("$.addressDto.department", is(DEPARTMENT)));
    }

    @Test
    public void changeStateToAMobileUser() throws Exception {
        createPostNewMobileUser();

        mockMvc.perform(put("/api/mobileUser/1/state")
                .contentType(MediaType.APPLICATION_JSON)
                .content("otherState"));

        mockMvc.perform(get("/api/mobileUser/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.facebookId", is(FACEBOOK_ID.intValue())))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.state", is("otherState")))
                .andExpect(jsonPath("$.addressDto.street", is(STREET)))
                .andExpect(jsonPath("$.addressDto.postalCode", is(POSTAL_CODE)))
                .andExpect(jsonPath("$.addressDto.floor", is(FLOOR)))
                .andExpect(jsonPath("$.addressDto.department", is(DEPARTMENT)));
    }

    @Test
    public void modifyingAddressOfMobileUser() throws Exception {
        createPostNewMobileUser();
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("anotherStreet");
        addressDto.setPostalCode("anotherPostalCode");
        addressDto.setFloor("anotherFloor");
        addressDto.setDepartment("anotherDepartment");
        String addressDtoJson = objectMapper.writeValueAsString(addressDto);

        ResultActions resultActions = mockMvc.perform(put("/api/mobileUser/" + FACEBOOK_ID + "/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressDtoJson));

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isEqualTo("Se agregó dirección exitosamente");
    }

    @Test
    public void getAllComercios() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercio = comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/mobileUser/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(comercio.getId().intValue())))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].tipo", is("tipo")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].estado", is("estado")))
                //TODO change this when no longer random item is given
                //.andExpect(jsonPath("$[0].rating", is("")))
                //.andExpect(jsonPath("$[0].leadTime", is("")))
                //.andExpect(jsonPath("$[0].minPrice", is("")))
                //.andExpect(jsonPath("$[0].maxPrice", is("")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void getFavoritesFromGivenUserAfterAdding() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercio = comercioRepository.saveAndFlush(comercio);
        Long comercioId = comercio.getId();
        createPostNewMobileUser();

        mockMvc.perform(post("/api/mobileUser/" + FACEBOOK_ID + "/favorite/" + comercioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/mobileUser/" + FACEBOOK_ID + "/favorites")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorites[0]", is(comercio.getId().intValue())));
    }

    private ResultActions createPostNewMobileUser() throws Exception {
        MobileUserDto mobileUserDto = createMobileUserDtoTest();
        String mobileUserDtoJson = objectMapper.writeValueAsString(mobileUserDto);

        return mockMvc.perform(post("/api/mobileUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mobileUserDtoJson));
    }

    private MobileUserDto createMobileUserDtoTest() {
        AddressDto address = new AddressDto();
        address.setDepartment(DEPARTMENT);
        address.setFloor(FLOOR);
        address.setPostalCode(POSTAL_CODE);
        address.setStreet(STREET);
        MobileUserDto mobileUserDto = new MobileUserDto();
        mobileUserDto.setFacebookId(FACEBOOK_ID);
        mobileUserDto.setFirstName(FIRST_NAME);
        mobileUserDto.setLastName(LAST_NAME);
        mobileUserDto.setUsername(USER_NAME);
        mobileUserDto.setState(STATE);
        mobileUserDto.setAddressDto(address);

        return mobileUserDto;
    }
}
