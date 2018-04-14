package ar.uba.fi.hoycomobackend.repository.usuario;

import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createMobileUser;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MobileUserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MobileUserRepository mobileUserRepository;

    @Test
    public void whenGetMobileUserById_thenReturnMobileUser() {
        // given
        MobileUser mobileUser = createMobileUser(1L, "username", "firstName", "lastName");
        Long firstMobileUserId = (Long) entityManager.persistAndGetId(mobileUser);
        mobileUser = createMobileUser(2L, "otherUsername", "otherFirstName", "otherLastName");
        entityManager.persist(mobileUser);
        entityManager.flush();

        // when
        Optional<MobileUser> mobileUserFoundOptional = mobileUserRepository.getMobileUserByFacebookId(firstMobileUserId);

        // then
        assertThat(mobileUserFoundOptional.isPresent()).isTrue();
        MobileUser mobileUserFound = mobileUserFoundOptional.get();
        assertThat(mobileUserFound.getFacebookId()).isEqualTo(1L);
        assertThat(mobileUserFound.getUsername()).isEqualTo("username");
        assertThat(mobileUserFound.getFirstName()).isEqualTo("firstName");
        assertThat(mobileUserFound.getLastName()).isEqualTo("lastName");
    }

}
