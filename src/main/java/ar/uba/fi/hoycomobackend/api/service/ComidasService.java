package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.TipoComidaDto;
import ar.uba.fi.hoycomobackend.database.entity.CategoriaComida;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ComidasService {

    private TipoComidaQuery tipoComidaQuery;
    private CategoriaComidaRepository categoriaComidaRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ComidasService(TipoComidaQuery tipoComidaQuery, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper) {
        this.tipoComidaQuery = tipoComidaQuery;
        this.categoriaComidaRepository = categoriaComidaRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity getTipoComidaPlatos() {
        List<CategoriaComida> categoriaComidaSet = categoriaComidaRepository.findAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(categoriaComidaSet);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }

    private <T> Set<TipoComidaDto> getTipoComidaDtoSet(List<T> categoriaComidaSet) {
        Set<TipoComidaDto> tipoComidaDtoSet = new HashSet<>();
        for (T tipoComida : categoriaComidaSet) {
            TipoComidaDto tipoComidaDto = modelMapper.map(tipoComida, TipoComidaDto.class);
            tipoComidaDtoSet.add(tipoComidaDto);
        }

        return tipoComidaDtoSet;
    }

    public ResponseEntity getTipoComidaComercio() {
        List<TipoComida> tipoComidaSet = tipoComidaQuery.getAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(tipoComidaSet);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }
}
