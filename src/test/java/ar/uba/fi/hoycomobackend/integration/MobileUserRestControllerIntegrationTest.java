package ar.uba.fi.hoycomobackend.integration;

import ar.uba.fi.hoycomobackend.App;
import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.dto.PostPedidoDto;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.repository.*;
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

import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
@ActiveProfiles("localprod")
public class MobileUserRestControllerIntegrationTest {

    private static String DEPARTMENT = "department";
    private static String FLOOR = "floor";
    private static String POSTAL_CODE = "postalCode";
    private static String STREET = "street";
    private static Long FACEBOOK_ID = 1L;
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String USER_NAME = "userName";
    private static String STATE = "habilitado";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MobileUserRepository mobileUserRepository;
    @Autowired
    private TipoComidaRepository tipoComidaRepository;
    @Autowired
    private ComercioRepository comercioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PlatoRepository platoRepository;

    @After
    public void tearDown() {
        mobileUserRepository.deleteAll();
        comercioRepository.deleteAll();
        tipoComidaRepository.deleteAll();
        pedidoRepository.deleteAll();
        platoRepository.deleteAll();
    }

    @Test
    public void creatingNewMobileUserTest() throws Exception {
        ResultActions resultActions = createPostNewMobileUser();

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isEqualTo("Se agregó usuario exitosamente");
    }

    @Test
    public void newlyCreatedMobileUserIsAuthorized() throws Exception {
        MobileUser mobileUser = createDefaultMobileUser();
        mobileUser = mobileUserRepository.saveAndFlush(mobileUser);

        MvcResult mvcResult = mockMvc.perform(get("/api/mobileUser/" + mobileUser.getFacebookId() + "/authorized")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("{\"state\":0,\"description\":\"AUTHORIZED\"}");
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
                .andExpect(jsonPath("$[0].mobileUserState", is(STATE)))
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
                .andExpect(jsonPath("$.mobileUserState", is(STATE)))
                .andExpect(jsonPath("$.addressDto.street", is(STREET)))
                .andExpect(jsonPath("$.addressDto.postalCode", is(POSTAL_CODE)))
                .andExpect(jsonPath("$.addressDto.floor", is(FLOOR)))
                .andExpect(jsonPath("$.addressDto.department", is(DEPARTMENT)));
    }

    @Test
    public void changeStateToAMobileUser() throws Exception {
        createPostNewMobileUser();
        MobileUserState mobileUserStateToChangeTo = MobileUserState.UNAUTHORIZED;

        mockMvc.perform(put("/api/mobileUser/1/state")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mobileUserStateToChangeTo.getValue().toString()));

        mockMvc.perform(get("/api/mobileUser/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.facebookId", is(FACEBOOK_ID.intValue())))
                .andExpect(jsonPath("$.username", is(USER_NAME)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.mobileUserState", is("deshabilitado")))
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
    public void getComerciosWithoutFilters() throws Exception {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        Comercio comercio = createDefaultComercio();
        comercio.setTipoComida(tipoComida);
        comercio = comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/mobileUser/comercios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(comercio.getId().intValue())))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].tipoComida.tipo", is("tipo")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].estado", is("habilitado")))
                .andExpect(jsonPath("$[0].rating", is("4.5")))
                .andExpect(jsonPath("$[0].leadTime", is("15")))
                .andExpect(jsonPath("$[0].precioMinimo", is("50.0")))
                .andExpect(jsonPath("$[0].precioMaximo", is("100.0")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void getComerciosWithMatchingFilters() throws Exception {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        Comercio comercio = createDefaultComercio();
        comercio.setTipoComida(tipoComida);
        comercio = comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/mobileUser/comercios/?search=tipo:tipo,leadTime<60,precioMinimo>50,precioMaximo<120,totalPedidos>10,rating>1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(comercio.getId().intValue())))
                .andExpect(jsonPath("$[0].nombre", is("nombre")))
                .andExpect(jsonPath("$[0].tipoComida.tipo", is("tipo")))
                .andExpect(jsonPath("$[0].imagenLogo", is("imagenLogo")))
                .andExpect(jsonPath("$[0].estado", is("habilitado")))
                .andExpect(jsonPath("$[0].rating", is("4.5")))
                .andExpect(jsonPath("$[0].leadTime", is("15")))
                .andExpect(jsonPath("$[0].precioMinimo", is("50.0")))
                .andExpect(jsonPath("$[0].precioMaximo", is("100.0")))
                .andExpect(jsonPath("$[0].addressDto.street", is("street")))
                .andExpect(jsonPath("$[0].addressDto.postalCode", is("postalCode")))
                .andExpect(jsonPath("$[0].addressDto.floor", is("floor")))
                .andExpect(jsonPath("$[0].addressDto.department", is("department")));
    }

    @Test
    public void getComerciosWithNonMatchingFilters() throws Exception {
        Comercio comercio = createDefaultComercio();
        comercioRepository.saveAndFlush(comercio);

        mockMvc.perform(get("/api/mobileUser/comercios/?search=tipo:tipo,leadTime<60,precioMinimo>50,precioMaximo<120,totalPedidos>200,rating>4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
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

    @Test
    public void getTipoComida() throws Exception {
        TipoComida tipoComida = createDefaultTipoComida();
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);

        mockMvc.perform(get("/api/mobileUser/tipoComida")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipo", is(tipoComida.getTipo())));
    }

    @Test
    public void savePedido() throws Exception {
        Long comercioId = DatabaseFiller.createDefaultComercioInDatabase(comercioRepository, tipoComidaRepository);
        MobileUser mobileUser = new MobileUser();
        mobileUser.setFacebookId(1L);
        mobileUserRepository.saveAndFlush(mobileUser);
        PostPedidoDto postPedidoDto = createDefaultPostPedidoDto();
        postPedidoDto.setStore_id(comercioId);
        String postPedidoDtoJson = objectMapper.writeValueAsString(postPedidoDto);

        mockMvc.perform(post("/api/mobileUser/pedido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postPedidoDtoJson));

        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(1);
        Pedido pedido = pedidoList.get(0);
        assertThat(pedido.getAddress()).isEqualTo("address");
        assertThat(pedido.getCodigoTC()).isEqualTo("codigoTC");
        assertThat(pedido.getDep()).isEqualTo("dep");
        assertThat(pedido.getFacebookId()).isEqualTo(1);
        assertThat(pedido.getFechaTC()).isEqualTo("fechaTC");
        assertThat(pedido.getFloor()).isEqualTo("floor");
        assertThat(pedido.getMedioPago()).isEqualTo("medioPago");
        assertThat(pedido.getNombreTC()).isEqualTo("nombreTC");
        assertThat(pedido.getNumeroTC()).isEqualTo("numeroTC");
        assertThat(pedido.getStoreId()).isEqualTo(comercioId);
        assertThat(pedido.getTotal()).isEqualTo(1.0);
        List<Orden> ordenList = pedido.getOrden();
        assertThat(ordenList).hasSize(1);
        Orden orden = ordenList.get(0);
        assertThat(orden.getCantidad()).isEqualTo(1);
        assertThat(orden.getId_plato()).isEqualTo(1);
        assertThat(orden.getObs()).isEqualTo("obs");
        assertThat(orden.getSub_total()).isEqualTo(1.0);

        mockMvc.perform(get("/api/mobileUser/detallePedido")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getPedidosByUser() throws Exception {
        Long comercioId = DatabaseFiller.createDefaultComercioInDatabase(comercioRepository, tipoComidaRepository);
        MobileUser mobileUser = new MobileUser();
        mobileUser.setFacebookId(1L);
        mobileUser = mobileUserRepository.saveAndFlush(mobileUser);
        PostPedidoDto postPedidoDto = createDefaultPostPedidoDto();
        postPedidoDto.setStore_id(comercioId);
        String postPedidoDtoJson = objectMapper.writeValueAsString(postPedidoDto);

        mockMvc.perform(post("/api/mobileUser/pedido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postPedidoDtoJson));

        mockMvc.perform(get("/api/mobileUser/" + mobileUser.getFacebookId() + "/pedido")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].store_name", is("nombre")));
    }

    @Test
    public void getPlatosForMobileUser() throws Exception {
        Long platoId = DatabaseFiller.createDefaultPlatoInDatabase(platoRepository);

        mockMvc.perform(get("/api/mobileUser/plato/" + platoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("nombre")))
                .andExpect(jsonPath("$.imagen", is("imagen")))
                .andExpect(jsonPath("$.precio", is(12.34)));
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
        mobileUserDto.setMobileUserState(STATE);
        mobileUserDto.setAddressDto(address);

        return mobileUserDto;
    }
}
