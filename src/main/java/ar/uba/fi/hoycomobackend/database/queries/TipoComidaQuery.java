package ar.uba.fi.hoycomobackend.database.queries;

import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TipoComidaQuery {

    private static Logger LOGGER = LoggerFactory.getLogger(TipoComidaQuery.class);
    private TipoComidaRepository tipoComidaRepository;

    @Autowired
    public TipoComidaQuery(TipoComidaRepository tipoComidaRepository) {
        this.tipoComidaRepository = tipoComidaRepository;
    }

    public Set<TipoComida> getAll() {
        LOGGER.info("Getting all the tipoComida");
        List<TipoComida> tipoComidaList = tipoComidaRepository.findAll();
        Set<TipoComida> tipoComidaSet = new HashSet<>(tipoComidaList);

        return tipoComidaSet;
    }
}
