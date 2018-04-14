package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private String postalCode;
    private String floor;
    private String department;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "address")
    private Comercio comercio;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "address")
    private MobileUser mobileUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public MobileUser getMobileUser() {
        return mobileUser;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }
}
