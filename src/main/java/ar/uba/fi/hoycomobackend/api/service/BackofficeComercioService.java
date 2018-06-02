package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.businesslogic.ComercioPriceUpdater;
import ar.uba.fi.hoycomobackend.api.businesslogic.TokenGenerator;
import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.OpcionRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BackofficeComercioService {

    private ComercioQuery comercioQuery;
    private PlatoRepository platoRepository;
    private CategoriaComidaRepository categoriaComidaRepository;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    private TokenGenerator tokenGenerator;
    private ComercioPriceUpdater comercioPriceUpdater;
    private OpcionRepository opcionRepository;

    @Autowired
    public BackofficeComercioService(ComercioQuery comercioQuery, PlatoRepository platoRepository, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper, ObjectMapper objectMapper, TokenGenerator tokenGenerator, ComercioPriceUpdater comercioPriceUpdater, OpcionRepository opcionRepository) {
        this.comercioQuery = comercioQuery;
        this.platoRepository = platoRepository;
        this.categoriaComidaRepository = categoriaComidaRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.tokenGenerator = tokenGenerator;
        this.comercioPriceUpdater = comercioPriceUpdater;
        this.opcionRepository = opcionRepository;
    }

    public ResponseEntity getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<Comercio> comercioOptional = comercioQuery.getComercioByEmail(givenEmail);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            if (comercio.getEmail().equals(givenEmail) && comercio.getPassword().equals(givenPassword)) {
                Long matchingValidComercioId = comercio.getId();
                String name = comercio.getNombre();
                TokenDto tokenDto = createToken(matchingValidComercioId, name);
                String tokenString = tokenDto.getToken();
                comercio.setToken(tokenString);
                comercioQuery.saveAndFlush(comercio);

                String tokenDtoJson = objectMapper.writeValueAsString(tokenDto);

                return ResponseEntity.ok(tokenDtoJson);
            } else
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorMessage("Usuario o password de comercio incorrecto"));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con email: " + givenEmail));
    }

    private TokenDto createToken(Long matchingValidComercioId, String name) {
        TokenDto tokenDto = new TokenDto();
        String token = tokenGenerator.createToken();
        tokenDto.setToken(token);
        tokenDto.setComercioId(matchingValidComercioId);
        tokenDto.setName(name);

        return tokenDto;
    }

    public ResponseEntity getPlatosNotDeletedFromComercio(Long comercioId) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            platoSet = platoSet.stream().filter(plato -> !PlatoState.BORRADO.equals(plato.getState())).collect(Collectors.toSet());
            Set<PlatoGetDto> platoDtoSet = mapPlatoSetToPlatoDtoSet(platoSet);
            platoDtoSet = fillCategoryDescriptionToPlatoDtoSet(platoDtoSet);
            String response = objectMapper.writeValueAsString(platoDtoSet);

            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }

    private Set<PlatoGetDto> mapPlatoSetToPlatoDtoSet(Set<Plato> platoSet) {
        Set<PlatoGetDto> platoDtoSet = new HashSet<>();
        for (Plato plato :
                platoSet) {
            PlatoGetDto platoGetDto = new PlatoGetDto();
            platoGetDto.setCategoria(plato.getCategoria());
            platoGetDto.setId(plato.getId());
            platoGetDto.setImagen(plato.getImagen());
            platoGetDto.setNombre(plato.getNombre());
            platoGetDto.setOrden(plato.getOrden());
            platoGetDto.setPrecio(plato.getPrecio());
            platoGetDto.setState(plato.getState());
            List<Long> opcionalIds  = plato.getOpciones().stream().map(opcion -> opcion.getId()).collect(Collectors.toList());
            platoGetDto.setOpcionalIds(opcionalIds);
            platoDtoSet.add(platoGetDto);
        }

        return platoDtoSet;
    }

    private Set<PlatoGetDto> fillCategoryDescriptionToPlatoDtoSet(Set<PlatoGetDto> platoDtoSet) {
        platoDtoSet.forEach(platoGetDto -> {
            if (platoGetDto.getCategoria() != null) {
                Optional<CategoriaComida> categoriaComidaOptional = categoriaComidaRepository.findById(platoGetDto.getCategoria());
                if (categoriaComidaOptional.isPresent())
                    platoGetDto.setDescCategoria(categoriaComidaOptional.get().getTipo());
                else
                    platoGetDto.setDescCategoria("");
            } else
                platoGetDto.setDescCategoria("");
        });

        return platoDtoSet;
    }

    public ResponseEntity addPlatoToComercio(Long comercioId, PlatoDto platoDto) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Plato plato = modelMapper.map(platoDto, Plato.class);
            Long currentCategory = plato.getCategoria();
            Integer maxOrderPlato = comercio.getPlatos().stream().
                    filter(plate -> currentCategory.equals(plate.getCategoria())).
                    mapToInt(plate -> plate.getOrden()).max().orElse(0);
            plato.setOrden(maxOrderPlato + 1);
            plato.setComercio(comercio);

            plato = platoRepository.saveAndFlush(plato);
            String response = objectMapper.writeValueAsString(plato.getId());
            comercioPriceUpdater.updatePriceOfComercio(comercioId);

            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }

    public ResponseEntity updatePlatoFromComercio(Long comercioId, Long platoId, PlatoUpdateDto platoUpdateDto) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            Optional<Plato> platoOptional = platoSet.stream().filter(plato -> plato.getId().equals(platoId)).findFirst();

            if (platoOptional.isPresent()) {
                Plato plato = platoOptional.get();
                Long olderCategory = plato.getCategoria();
                modelMapper.map(platoUpdateDto, plato);
                plato.setComercio(comercio);
                plato.setId(platoId);
                platoUpdateDto.setId(platoId);
                if (shouldChangeOrden(olderCategory, plato.getCategoria())) {
                    Long currentCategory = plato.getCategoria();
                    Integer maxOrderPlato = comercio.getPlatos().stream().
                            filter(plate -> currentCategory.equals(plate.getCategoria()) && !platoId.equals(plate.getId())).
                            mapToInt(plate -> plate.getOrden()).max().orElse(0);
                    plato.setOrden(maxOrderPlato + 1);
                }
                plato = modifyAssociatedOpcionales(plato, platoUpdateDto);
                String response = objectMapper.writeValueAsString(platoUpdateDto);

                platoRepository.saveAndFlush(plato);
                comercioPriceUpdater.updatePriceOfComercio(comercioId);

                return ResponseEntity.ok(response);
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún plato con id: " + platoId));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }

    private Plato modifyAssociatedOpcionales(Plato plato, PlatoUpdateDto platoUpdateDto) {
        if (platoUpdateDto.getOpcionalIds() != null) {
            List<Long> opcionalIds = platoUpdateDto.getOpcionalIds();
            Set<Opcion> opcionList = new HashSet<>();
            for (Long opcionalId :
                    opcionalIds) {
                Opcion opcion = opcionRepository.getOne(opcionalId);
                opcionList.add(opcion);
            }
            plato.setOpciones(opcionList);
        }
            return plato;
    }

    private boolean shouldChangeOrden(Long olderCategory, Long categoria) {
        return !categoria.equals(olderCategory);
    }

    public ResponseEntity getComercioById(Long comercioId) {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            AddressDto addressDto = modelMapper.map(comercio.getAddress(), AddressDto.class);
            ComercioBackofficeDto comercioBackofficeDto = modelMapper.map(comercio, ComercioBackofficeDto.class);
            comercioBackofficeDto.setAddressDto(addressDto);
            comercioBackofficeDto.setTipoComidaId(comercio.getTipoComida().getId());

            return ResponseEntity.ok(comercioBackofficeDto);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }
}
