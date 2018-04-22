package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.MobileUserState;

public class MobileUserStateDto {
    Integer state;
    String description;

    public MobileUserStateDto(MobileUserState mobileUserState) {
        this.state = mobileUserState.getValue();
        this.description = mobileUserState.name();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
