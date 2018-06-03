package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.CategoriaComida;
import ar.uba.fi.hoycomobackend.database.entity.Opcion;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.OpcionRepository;
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
    private OpcionRepository opcionRepository;

    @Autowired
    public ComidasService(TipoComidaQuery tipoComidaQuery, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper, OpcionRepository opcionRepository) {
        this.tipoComidaQuery = tipoComidaQuery;
        this.modelMapper = modelMapper;
        this.categoriaComidaRepository = categoriaComidaRepository;
        this.opcionRepository = opcionRepository;
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
            List<CategoriaComida> categoriaComidaList = categoriaComidaRepository.getAllByComercioIdIs(comercioId);
            return ResponseEntity.ok(categoriaComidaList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    public ResponseEntity setCategoriaComidaFromMenuByComercioId(Long comercioId, String tipoComida) {
        CategoriaComida categoriaComida = new CategoriaComida();
        categoriaComida.setTipo(tipoComida);
        categoriaComida.setComercioId(comercioId);
        categoriaComida.setActive(true);
        try {
            Integer totalCategoriesOfComercio = categoriaComidaRepository.getAllByComercioIdIs(comercioId).size();
            categoriaComida.setOrderPriority(totalCategoriesOfComercio + 1);
            categoriaComida = categoriaComidaRepository.saveAndFlush(categoriaComida);
            return ResponseEntity.ok(categoriaComida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    public ResponseEntity updateCategoriaComidaById(Long categoriaComidaId, UpdateCategoriaComidaDto updateCategoriaComidaDto) {
        Optional<CategoriaComida> categoriaComidaOptional = categoriaComidaRepository.findById(categoriaComidaId);
        if (categoriaComidaOptional.isPresent()) {
            CategoriaComida categoriaComida = categoriaComidaOptional.get();
            if (updateCategoriaComidaDto.getEstaActivo() != null)
                categoriaComida.setActive(updateCategoriaComidaDto.getEstaActivo());
            if (updateCategoriaComidaDto.getNombreCategoria() != null)
                categoriaComida.setTipo(updateCategoriaComidaDto.getNombreCategoria());

            try {
                categoriaComidaRepository.saveAndFlush(categoriaComida);
                return ResponseEntity.ok("Categoría comida actualizado exitosamente");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
            }
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Id de categoría de comida no encontrado"));
    }

    public ResponseEntity changeActiveStateOfCategoriaComidaById(Long categoriaComidaId) {
        Optional<CategoriaComida> categoriaComidaOptional = categoriaComidaRepository.findById(categoriaComidaId);
        if (categoriaComidaOptional.isPresent()) {
            CategoriaComida categoriaComida = categoriaComidaOptional.get();
            boolean currentActiveStatus = categoriaComida.getActive();
            categoriaComida.setActive(!currentActiveStatus);
            try {
                categoriaComidaRepository.saveAndFlush(categoriaComida);
                return ResponseEntity.ok(new JsonMessage("Categoría comida estado esActivo: " + !currentActiveStatus));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
            }
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Id de categoría de comida no encontrado"));
    }

    public ResponseEntity swapCategoriaComidaById(SwapOrdersDto swapOrdersDto) {
        try {
            Long firstCategoriaComidaId = swapOrdersDto.getFirstCategoriaComidaId();
            Long secondCategoriaComidaId = swapOrdersDto.getSecondCategoriaComidaId();
            try {
                CategoriaComida firstCategoriaComida = categoriaComidaRepository.findById(firstCategoriaComidaId).get();
                try {
                    CategoriaComida secondCategoriaComida = categoriaComidaRepository.findById(secondCategoriaComidaId).get();
                    firstCategoriaComida.setOrderPriority(secondCategoriaComida.getOrderPriority());
                    secondCategoriaComida.setOrderPriority(firstCategoriaComida.getOrderPriority());

                    categoriaComidaRepository.saveAndFlush(firstCategoriaComida);
                    categoriaComidaRepository.saveAndFlush(secondCategoriaComida);
                    return ResponseEntity.ok("Orden de Categoría comida cambiada exitosamente");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo encontrar categoria con id: " + secondCategoriaComidaId + " Error: " + e.getMessage()));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo encontrar categoria con id: " + firstCategoriaComidaId + " Error: " + e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    public ResponseEntity createOpcion(Long comercioId, OpcionDto opcionDto) {
        try {
            Opcion opcion = modelMapper.map(opcionDto, Opcion.class);
            opcion.setComercioId(comercioId);
            opcion = opcionRepository.saveAndFlush(opcion);

            return ResponseEntity.ok(opcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Error tratando de crear/modificar opción"));
        }
    }

    public ResponseEntity getOpcionById(Long opcionId) {
        try {
            Opcion opcion = opcionRepository.findById(opcionId).get();

            return ResponseEntity.ok(opcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Opción solicitada no existe"));
        }
    }

    public ResponseEntity updateOpcion(Long opcionId, OpcionDto opcionDto) {
        try {
            Optional<Opcion> opcionOptional = opcionRepository.findById(opcionId);
            Opcion opcion = opcionOptional.get();
            modelMapper.map(opcionDto, opcion);
            opcion = opcionRepository.saveAndFlush(opcion);

            return ResponseEntity.ok(opcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Error al modificar opción. " + e.getMessage()));
        }
    }

    public ResponseEntity getAllOpciones() {
        List<Opcion> opcionList = opcionRepository.findAll();

        return ResponseEntity.ok(opcionList);
    }

    public ResponseEntity getAllOpcionesFromComercioById(Long comercioId) {
        List<Opcion> opcionList = opcionRepository.findAllByComercioIdIs(comercioId);

        return ResponseEntity.ok(opcionList);
    }
}
