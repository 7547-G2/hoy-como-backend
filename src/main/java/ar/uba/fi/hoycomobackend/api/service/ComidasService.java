package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.TipoComidaDto;
import ar.uba.fi.hoycomobackend.database.entity.CategoriaComida;
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
    private CategoriaComidaRepository categoriaComidaRepository;

    @Autowired
    public ComidasService(TipoComidaQuery tipoComidaQuery, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper) {
        this.tipoComidaQuery = tipoComidaQuery;
        this.modelMapper = modelMapper;
        this.categoriaComidaRepository = categoriaComidaRepository;
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

    public ResponseEntity getCategoriaComidaFromMenu() {
        try {
            List<CategoriaComida> categoriaComida = categoriaComidaRepository.findAll();
            return ResponseEntity.ok(categoriaComida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    public ResponseEntity getCategoriaComidaFromMenuByComercioId(Long comercioId) {
        try {
            Optional<CategoriaComida> categoriaComidaOptional = categoriaComidaRepository.getByComercioId(comercioId);
            if (categoriaComidaOptional.isPresent())
                return ResponseEntity.ok(categoriaComidaOptional.get());
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró comercio"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    public ResponseEntity setCategoriaComidaFromMenuByComercioId(Long comercioId, String tipoComida) {
        CategoriaComida categoriaComida = new CategoriaComida();
        categoriaComida.setTipo(tipoComida);
        categoriaComida.setComercioId(comercioId);
        try {
            categoriaComida = categoriaComidaRepository.save(categoriaComida);
            return ResponseEntity.ok(categoriaComida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

}
