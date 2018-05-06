package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.TipoComidaDto;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CategoriaComidaService {

    private TipoComidaQuery tipoComidaQuery;
    private ModelMapper modelMapper;

    @Autowired
    public CategoriaComidaService(TipoComidaQuery tipoComidaQuery, ModelMapper modelMapper) {
        this.tipoComidaQuery = tipoComidaQuery;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity getCategoriaComida() {
        Set<TipoComida> tipoComidaSet = tipoComidaQuery.getAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(tipoComidaSet);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }

    private Set<TipoComidaDto> getTipoComidaDtoSet(Set<TipoComida> tipoComidaSet) {
        Set<TipoComidaDto> tipoComidaDtoSet = new HashSet<>();
        for (TipoComida tipoComida : tipoComidaSet) {
            TipoComidaDto tipoComidaDto = modelMapper.map(tipoComida, TipoComidaDto.class);
            tipoComidaDtoSet.add(tipoComidaDto);
        }

        return tipoComidaDtoSet;
    }
}
