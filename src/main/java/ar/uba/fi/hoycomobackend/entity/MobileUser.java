package ar.uba.fi.hoycomobackend.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(columnDefinition = "varchar(128) default 'enabled'")
    @NotNull
    private String state;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "mobileuser_comercio",
            joinColumns = {@JoinColumn(name = "mobileuser_facebookId")},
            inverseJoinColumns = {@JoinColumn(name = "comercio_id")})
    private List<Comercio> favoriteComercios;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
