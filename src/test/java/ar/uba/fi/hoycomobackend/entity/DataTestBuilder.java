package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;

public class DataTestBuilder {

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
        Address address = createDefaultAddress();
        Comercio comercio = new Comercio();
        comercio.setEmail("email");
        comercio.setNombre("nombre");
        comercio.setRazonSocial("razonSocial");
        comercio.setTipo("tipo");
        comercio.setToken("token");
        comercio.setPassword("password");
        comercio.setEstado("estado");
        comercio.setImagenLogo("imagenLogo");
        comercio.setAddress(address);

        return comercio;
    }

    public static ComercioHoyComoDto createDefaultComercioHoyComoDto() {
        AddressDto addressDto = createDefaultAddressDto();
        ComercioHoyComoDto comercioHoyComoDto = new ComercioHoyComoDto();
        comercioHoyComoDto.setEmail("email");
        comercioHoyComoDto.setNombre("nombre");
        comercioHoyComoDto.setRazonSocial("razonSocial");
        comercioHoyComoDto.setTipo("tipo");
        comercioHoyComoDto.setPassword("password");
        comercioHoyComoDto.setEstado("estado");
        comercioHoyComoDto.setImagenLogo("imagenLogo");
        comercioHoyComoDto.setAddressDto(addressDto);

        return comercioHoyComoDto;
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

        return platoDto;
    }

    public static MobileUser createMobileUser(Long facebookId, String username, String firstName, String lastName, Boolean authorized) {
        MobileUser mobileUser = createMobileUser(facebookId, username, firstName, lastName);
        mobileUser.setAuthorized(authorized);

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
}
