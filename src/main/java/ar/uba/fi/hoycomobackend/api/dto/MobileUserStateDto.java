package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.entity.MobileUserState;
import org.springframework.http.HttpStatus;

public class MobileUserStateDto {
    String code;
    Integer state;
    String description;

    public MobileUserStateDto(HttpStatus httpStatus, MobileUserState mobileUserState) {
        this.code = httpStatus.toString();
        this.state = mobileUserState.getValue();
        this.description = mobileUserState.name();
    }
}
