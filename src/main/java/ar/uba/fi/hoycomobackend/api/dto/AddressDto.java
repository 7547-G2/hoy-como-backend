package ar.uba.fi.hoycomobackend.api.dto;

public class AddressDto {
    private String street;
    private String postalCode;
    private String floor;
    private String department;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public AddressDto removeNulls() {
        if(street == null)
            street = "";
        if(postalCode == null)
            postalCode = "";
        if(floor == null)
             floor = "";
        if(department == null)
             department = "";

        return this;
    }
}
