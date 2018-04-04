package ar.uba.fi.hoycomobackend.entity.mobileuser;


import javax.persistence.*;

@Entity
@Table(name = "mobileuser")
public class MobileUser {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    @Column(name = "authorized")
    private Boolean authorized = true;


    public Long getId() {
        return id;
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
}
