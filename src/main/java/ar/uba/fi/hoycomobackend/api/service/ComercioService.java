package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComercioService {

    private ComercioRepository comercioRepository;

    @Autowired
    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    public List<Comercio> getComercioByNombre(String nombre) {
        return comercioRepository.findByNombre(nombre);
    }

    public List<Comercio> getAllComercios() {
        return comercioRepository.findAll();
    }

    public void addComercio(Comercio comercio) {
        comercioRepository.saveAndFlush(comercio);
    }
}
