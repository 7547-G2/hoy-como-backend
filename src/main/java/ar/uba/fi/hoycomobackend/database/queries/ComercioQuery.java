package ar.uba.fi.hoycomobackend.database.queries;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.specification.ComercioSpecificationBuilder;
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
public class ComercioQuery {

    private static Logger LOGGER = LoggerFactory.getLogger(ComercioQuery.class);
    private ComercioRepository comercioRepository;

    @Autowired
    public ComercioQuery(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    public List<Comercio> findByNombre(String nombre) {
        LOGGER.info("Getting all Comercios with name: {}", nombre);
        List<Comercio> comercioList = comercioRepository.findByNombre(nombre);

        return comercioList;
    }

    public List<Comercio> findAll() {
        LOGGER.info("Getting all Comercios");
        List<Comercio> comercioList = comercioRepository.findAll();

        return comercioList;
    }

    public Optional<Comercio> getComercioById(Long comercioId) {
        LOGGER.info("Trying to get comercio with id: {}", comercioId);
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        return comercioOptional;
    }

    public Comercio saveAndFlush(Comercio comercio) {
        LOGGER.info("Saving new comercio");
        return comercioRepository.saveAndFlush(comercio);
    }

    public List<Comercio> findBySearchQuery(String search) {
        LOGGER.info("Getting Comercios by search query: {}", search);
        ComercioSpecificationBuilder builder = new ComercioSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Comercio> spec = builder.build();
        List<Comercio> comercioList = comercioRepository.findAll(spec);

        return comercioList;
    }

    public Optional<Comercio> getComercioByEmail(String email) {
        LOGGER.info("Getting Comercio by email: {}", email);
        return comercioRepository.getComercioByEmail(email);
    }
}
