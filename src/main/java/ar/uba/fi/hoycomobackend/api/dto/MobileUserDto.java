package ar.uba.fi.hoycomobackend.api.dto;

import ar.uba.fi.hoycomobackend.database.entity.MobileUserState;

public class MobileUserDto {
    private long facebookId;
    private String username;
    private String firstName;
    private String lastName;
    private MobileUserState mobileUserState;
    private AddressDto addressDto;

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto address) {
        this.addressDto = address;
    }

    public MobileUserState getMobileUserState() {
        return mobileUserState;
    }

    public void setMobileUserState(MobileUserState mobileUserState) {
        this.mobileUserState = mobileUserState;
    }

    public MobileUserDto removeNulls() {
        if (username == null)
            username = "";
        if (firstName == null)
            firstName = "";
        if (lastName == null)
            lastName = "";
        if (mobileUserState == null)
            mobileUserState = MobileUserState.NOT_FOUND;
        if(addressDto == null) {
            addressDto = new AddressDto();
            addressDto = addressDto.removeNulls();
        }

        return this;
    }
}
