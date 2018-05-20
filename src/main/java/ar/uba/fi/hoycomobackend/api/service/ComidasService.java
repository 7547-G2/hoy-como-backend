package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.TipoComidaDto;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ComidasService {

    private TipoComidaQuery tipoComidaQuery;
    private ModelMapper modelMapper;

    @Autowired
    public ComidasService(TipoComidaQuery tipoComidaQuery, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper) {
        this.tipoComidaQuery = tipoComidaQuery;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity getTipoComidaPlatos() {
        List<TipoComida> tipoComidaList = tipoComidaQuery.getAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(tipoComidaList);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }

    private Set<TipoComidaDto> getTipoComidaDtoSet(List<TipoComida> categoriaComidaSet) {
        Set<TipoComidaDto> tipoComidaDtoSet = new HashSet<>();
        for (TipoComida tipoComida : categoriaComidaSet) {
            TipoComidaDto tipoComidaDto = modelMapper.map(tipoComida, TipoComidaDto.class);
            tipoComidaDto.setComercioId(new LinkedList<>());
            tipoComida.getComercio().forEach(comercio -> {
                tipoComidaDto.getComercioId().add(comercio.getId());
            });
            tipoComidaDtoSet.add(tipoComidaDto);
        }

        return tipoComidaDtoSet;
    }

    public ResponseEntity getTipoComidaComercio() {
        List<TipoComida> tipoComidaSet = tipoComidaQuery.getAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(tipoComidaSet);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }

    public ResponseEntity getTipoComidaComercioById(Long comercioId) {
        Optional<TipoComida> tipoComidaOptional = tipoComidaQuery.getTipoComidaByComercioId(comercioId);
        if (tipoComidaOptional.isPresent()) {
            TipoComida tipoComida = tipoComidaOptional.get();
            TipoComidaDto tipoComidaDto = new TipoComidaDto();
            tipoComidaDto.setTipo(tipoComida.getTipo());
            tipoComidaDto.setId(tipoComida.getId());
            return ResponseEntity.ok(tipoComidaDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Comercio con id: " + comercioId + "no encontrado"));
    }

}
