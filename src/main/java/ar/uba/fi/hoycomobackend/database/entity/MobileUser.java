package ar.uba.fi.hoycomobackend.database.entity;


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
    @NotNull
    private String state = "habilitado";
    private String motivoDeshabilitacion = "";
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "mobileuser_comercio",
            joinColumns = {@JoinColumn(name = "mobileuser_facebookId")},
            inverseJoinColumns = {@JoinColumn(name = "comercio_id")})
    private List<Comercio> favoriteComercios;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    private String link;
    @Column(columnDefinition = "varchar(32) default 'No hay teléfono disponible'")
    private String telephone = "No hay teléfono disponible";

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

    public String getMotivoDeshabilitacion() {
        return motivoDeshabilitacion;
    }

    public void setMotivoDeshabilitacion(String motivoDeshabilitacion) {
        this.motivoDeshabilitacion = motivoDeshabilitacion;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
