package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-localprod.yml")
public class MobileUserResControllerIntegrationTest {

    private static String DEPARTMENT = "department";
    private static String FLOOR = "floor";
    private static String POSTAL_CODE = "postalCode";
    private static String STREET = "street";
    private static Long FACEBOOK_ID = 1L;
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String USER_NAME = "userName";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MobileUserRepository mobileUserRepository;

    @After
    public void tearDown() {
        mobileUserRepository.deleteAll();
    }

    @Test
    public void creatingNewMobileUserTest() throws Exception {
        ResultActions resultActions = createPostNewMobileUser();

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isEqualTo("Se agregó usuario exitosamente");
    }

    @Test
    public void gettingNewlyCreateMobileUserTest() throws Exception {
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
                .andExpect(jsonPath("$[0].addressDto.street", is(STREET)))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is(POSTAL_CODE)))
                .andExpect(jsonPath("$[0].addressDto.floor", is(FLOOR)))
                .andExpect(jsonPath("$[0].addressDto.department", is(DEPARTMENT)));
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

    private ResultActions createPostNewMobileUser() throws Exception {
        MobileUserDto mobileUser = createMobileUserDtoTest();
        String mobileUserDtoJson = objectMapper.writeValueAsString(mobileUser);

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
        mobileUserDto.setAddressDto(address);

        return mobileUserDto;
    }


}
