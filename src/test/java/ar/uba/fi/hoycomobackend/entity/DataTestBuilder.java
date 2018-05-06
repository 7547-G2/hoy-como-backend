package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.*;
import org.springframework.mail.SimpleMailMessage;

public class DataTestBuilder {

    private static MobileUserState DEFAULT_MOBILE_USER_STATE = MobileUserState.AUTHORIZED;

    public static Comercio createComercio(String email, String nombre) {
        Comercio comercio = new Comercio();
        comercio.setEmail(email);
        comercio.setNombre(nombre);

        return comercio;
    }

    public static Comercio createComercio(Long id, String email, String nombre) {
        Comercio comercio = createComercio(email, nombre);
        comercio.setId(id);

        return comercio;
    }

    public static ComercioHoyComoDto createComercioDto(Long id, String email, String nombre) {
        ComercioHoyComoDto comercioHoyComoDto = new ComercioHoyComoDto();
        comercioHoyComoDto.setEmail(email);
        comercioHoyComoDto.setNombre(nombre);

        return comercioHoyComoDto;
    }

    public static MobileUser createMobileUser(Long facebookId, String username, String firstName, String lastName) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setFacebookId(facebookId);
        mobileUser.setUsername(username);
        mobileUser.setFirstName(firstName);
        mobileUser.setLastName(lastName);
        mobileUser.setState(DEFAULT_MOBILE_USER_STATE);

        return mobileUser;
    }

    public static MobileUser createDefaultMobileUser() {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setFacebookId(1L);
        mobileUser.setUsername("username");
        mobileUser.setFirstName("firstName");
        mobileUser.setLastName("lastName");
        mobileUser.setState(DEFAULT_MOBILE_USER_STATE);
        Address address = createDefaultAddress();
        mobileUser.setAddress(address);

        return mobileUser;
    }

    public static Address createDefaultAddress() {
        Address address = new Address();
        address.setDepartment("department");
        address.setFloor("floor");
        address.setPostalCode("postalCode");
        address.setStreet("street");

        return address;
    }

    public static Comercio createDefaultComercio() {
        Comercio comercio = new Comercio();
        Address address = createDefaultAddress();
        comercio.setAddress(address);
        TipoComida tipoComida = createDefaultTipoComida();
        comercio.setTipoComida(tipoComida);
        comercio.setEmail("email");
        comercio.setNombre("nombre");
        comercio.setRazonSocial("razonSocial");
        comercio.setToken("token");
        comercio.setPassword("password");
        comercio.setEstado("estado");
        comercio.setImagenLogo("imagenLogo");
        comercio.setLeadTime(15);
        comercio.setPrecioMinimo(50f);
        comercio.setPrecioMaximo(100f);
        comercio.setTotalPedidos(20);
        comercio.setRating(4.5f);

        return comercio;
    }

    public static TipoComida createDefaultTipoComida() {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");

        return tipoComida;
    }

    public static ComercioHoyComoDto createDefaultComercioHoyComoDto() {
        AddressDto addressDto = createDefaultAddressDto();
        TipoComidaDto tipoComida = createDefaultTipoComidaDto();
        ComercioHoyComoDto comercioHoyComoDto = new ComercioHoyComoDto();
        comercioHoyComoDto.setEmail("email");
        comercioHoyComoDto.setNombre("nombre");
        comercioHoyComoDto.setRazonSocial("razonSocial");
        comercioHoyComoDto.setTipoComida(tipoComida);
        comercioHoyComoDto.setPassword("password");
        comercioHoyComoDto.setEstado("estado");
        comercioHoyComoDto.setImagenLogo("imagenLogo");
        comercioHoyComoDto.setAddressDto(addressDto);

        return comercioHoyComoDto;
    }

    public static ComercioHoyComoAddDto createDefaultComercioHoyComoAddDto() {
        AddressDto addressDto = createDefaultAddressDto();
        TipoComidaAddDto tipoComidaAddDto = createDefaultTipoComidaAddDto();
        ComercioHoyComoAddDto comercioHoyComoAddDto = new ComercioHoyComoAddDto();
        comercioHoyComoAddDto.setEmail("email");
        comercioHoyComoAddDto.setNombre("nombre");
        comercioHoyComoAddDto.setRazonSocial("razonSocial");
        comercioHoyComoAddDto.setTipoComida(tipoComidaAddDto);
        comercioHoyComoAddDto.setPassword("password");
        comercioHoyComoAddDto.setEstado("estado");
        comercioHoyComoAddDto.setImagenLogo("imagenLogo");
        comercioHoyComoAddDto.setAddressDto(addressDto);

        return comercioHoyComoAddDto;
    }

    private static TipoComidaAddDto createDefaultTipoComidaAddDto() {
        TipoComidaAddDto tipoComidaAddDto = new TipoComidaAddDto();
        tipoComidaAddDto.setTipo("tipo");

        return  tipoComidaAddDto;
    }

    public static TipoComidaDto createDefaultTipoComidaDto() {
        TipoComidaDto tipoComidaDto = new TipoComidaDto();
        tipoComidaDto.setTipo("tipo");

        return tipoComidaDto;
    }

    public static AddressDto createDefaultAddressDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setDepartment("department");
        addressDto.setFloor("floor");
        addressDto.setPostalCode("postalCode");
        addressDto.setStreet("street");

        return addressDto;
    }

    public static PlatoDto createDefaultPlatoDto() {
        PlatoDto platoDto = new PlatoDto();
        platoDto.setImagen("imagen");
        platoDto.setNombre("nombre");
        platoDto.setPrecio(1.0f);
        platoDto.setCategoria(1L);
        platoDto.setOrden(1);

        return platoDto;
    }

    public static PlatoUpdateDto createDefaultPlatoUpdateDto() {
        PlatoUpdateDto platoUpdateDto = new PlatoUpdateDto();
        platoUpdateDto.setImagen("imagen");
        platoUpdateDto.setNombre("nombre");
        platoUpdateDto.setPrecio(1.0f);
        platoUpdateDto.setState(PlatoState.ACTIVO);
        platoUpdateDto.setCategoria(1L);
        platoUpdateDto.setOrden(1);

        return platoUpdateDto;
    }

    public static Plato createDefaultPlato() {
        Plato plato = new Plato();
        plato.setImagen("imagen");
        plato.setNombre("nombre");
        plato.setPrecio(1.0f);
        plato.setState(PlatoState.ACTIVO);

        return plato;
    }

    public static MobileUser createMobileUser(Long facebookId, String username, String firstName, String lastName, Boolean authorized) {
        MobileUser mobileUser = createMobileUser(facebookId, username, firstName, lastName);
        mobileUser.setState(DEFAULT_MOBILE_USER_STATE);

        return mobileUser;
    }


    public static MobileUserDto createMobileUserDto(Long facebookId, String username, String firstName, String lastName) {
        MobileUserDto mobileUserDto = new MobileUserDto();
        mobileUserDto.setFacebookId(facebookId);
        mobileUserDto.setUsername(username);
        mobileUserDto.setFirstName(firstName);
        mobileUserDto.setLastName(lastName);

        return mobileUserDto;
    }

    public static SimpleMailMessage createDefaultSimpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("to");
        simpleMailMessage.setSubject("subject");
        simpleMailMessage.setText("text");

        return simpleMailMessage;
    }
}
