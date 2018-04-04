package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;

public class EntityTestBuilder {

    public static Comercio createComercio(String nombre) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);

        return comercio;
    }

    public static MobileUser createMobileUser(String username, String firstName, String lastName) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setUsername(username);
        mobileUser.setFirstName(firstName);
        mobileUser.setLastName(lastName);

        return mobileUser;
    }

    public static MobileUserDto createMobileUserDto(String username, String firstName, String lastName) {
        MobileUserDto mobileUserDto = new MobileUserDto();
        mobileUserDto.setUsername(username);
        mobileUserDto.setFirstName(firstName);
        mobileUserDto.setLastName(lastName);

        return mobileUserDto;
    }
}
