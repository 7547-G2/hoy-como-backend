package ar.uba.fi.hoycomobackend.entity.mobileuser;


import ar.uba.fi.hoycomobackend.entity.Address;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mobileuser")
public class MobileUser {

    @Id
    @Column(name = "facebookId")
    private Long facebookId;

    private String username;
    private String firstName;
    private String lastName;
    @Column(name = "authorized")
    private Boolean authorized = true;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "mobileuser_comercio",
            joinColumns = {@JoinColumn(name = "mobileuser_facebookId")},
            inverseJoinColumns = {@JoinColumn(name = "comercio_id")})
    private List<Comercio> favoriteComercios;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public List<Comercio> getFavoriteComercios() {
        return favoriteComercios;
    }

    public void setFavoriteComercios(List<Comercio> favoriteComercios) {
        this.favoriteComercios = favoriteComercios;
    }


    public Long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Long facebookId) {
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

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean deactivated) {
        this.authorized = deactivated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
