package ar.uba.fi.hoycomobackend.database.queries;

import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import ar.uba.fi.hoycomobackend.database.specification.MobileUserSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UsuarioQuery {
    private static Logger LOGGER = LoggerFactory.getLogger(UsuarioQuery.class);
    private MobileUserRepository mobileUserRepository;

    @Autowired
    public UsuarioQuery(MobileUserRepository comercioRepository) {
        this.mobileUserRepository = comercioRepository;
    }

    public List<MobileUser> findBySearchQuery(String search) {
        LOGGER.info("Getting MobileUsers by search query: {}", search);
        MobileUserSpecificationBuilder builder = new MobileUserSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<MobileUser> spec = builder.build();
        List<MobileUser> comercioList = mobileUserRepository.findAll(spec);

        return comercioList;
    }

    public Optional<MobileUser> findById(Long mobileUserId) {
        return mobileUserRepository.findById(mobileUserId);
    }

    public void save(MobileUser mobileUser) {
        mobileUserRepository.saveAndFlush(mobileUser);
    }
}
