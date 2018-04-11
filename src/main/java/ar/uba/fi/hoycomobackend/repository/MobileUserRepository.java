package ar.uba.fi.hoycomobackend.repository;

import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MobileUserRepository extends JpaRepository<MobileUser, Long> {

    Optional<MobileUser> getMobileUserByFacebookId(Long facebookId);
}
