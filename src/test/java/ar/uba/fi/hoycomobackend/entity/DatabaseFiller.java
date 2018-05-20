package ar.uba.fi.hoycomobackend.entity;

import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;

public class DatabaseFiller {

    public static Long createDefaultComercioInDatabase(ComercioRepository comercioRepository, TipoComidaRepository tipoComidaRepository) {
        TipoComida tipoComida = new TipoComida();
        tipoComida.setTipo("tipo");
        tipoComida = tipoComidaRepository.saveAndFlush(tipoComida);
        Comercio comercio = createDefaultComercio();
        comercio.setTipoComida(tipoComida);
        comercio = comercioRepository.saveAndFlush(comercio);

        return comercio.getId();
    }
}
