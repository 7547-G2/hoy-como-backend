package ar.uba.fi.hoycomobackend.database.repository;

import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MobileUserRepository extends JpaRepository<MobileUser, Long>, JpaSpecificationExecutor<MobileUser> {

    Optional<MobileUser> getMobileUserByFacebookId(Long facebookId);
}
