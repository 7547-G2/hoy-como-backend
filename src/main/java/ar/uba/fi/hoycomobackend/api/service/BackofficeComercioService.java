package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.businesslogic.ComercioPriceUpdater;
import ar.uba.fi.hoycomobackend.api.businesslogic.TokenGenerator;
import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;
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

    @Autowired
    public BackofficeComercioService(ComercioQuery comercioQuery, PlatoRepository platoRepository, CategoriaComidaRepository categoriaComidaRepository, ModelMapper modelMapper, ObjectMapper objectMapper, TokenGenerator tokenGenerator, ComercioPriceUpdater comercioPriceUpdater) {
        this.comercioQuery = comercioQuery;
        this.platoRepository = platoRepository;
        this.categoriaComidaRepository = categoriaComidaRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.tokenGenerator = tokenGenerator;
        this.comercioPriceUpdater = comercioPriceUpdater;
    }

    public ResponseEntity getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<Comercio> comercioOptional = comercioQuery.getComercioByEmail(givenEmail);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            if (comercio.getEmail().equals(givenEmail) && comercio.getPassword().equals(givenPassword)) {
                Long matchingValidComercioId = comercio.getId();
                TokenDto tokenDto = createToken(matchingValidComercioId);
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

    private TokenDto createToken(Long matchingValidComercioId) {
        TokenDto tokenDto = new TokenDto();
        String token = tokenGenerator.createToken();
        tokenDto.setToken(token);
        tokenDto.setComercioId(matchingValidComercioId);

        return tokenDto;
    }

    public ResponseEntity getPlatosHabilitadosFromComercio(Long comercioId) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            platoSet = platoSet.stream().filter(plato -> PlatoState.ACTIVO.equals(plato.getState())).collect(Collectors.toSet());
            Type setType = new TypeToken<Set<PlatoGetDto>>() {
            }.getType();
            Set<PlatoGetDto> platoDtoSet = modelMapper.map(platoSet, setType);
            platoDtoSet = fillCategoryDescriptionToPlatoDtoSet(platoDtoSet);
            String response = objectMapper.writeValueAsString(platoDtoSet);

            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }

    private Set<PlatoGetDto> fillCategoryDescriptionToPlatoDtoSet(Set<PlatoGetDto> platoDtoSet) {
        platoDtoSet.forEach(platoGetDto -> {
            if(platoGetDto.getCategoria() != null) {
                Optional<CategoriaComida> categoriaComidaOptional = categoriaComidaRepository.findById(platoGetDto.getCategoria());
                if (categoriaComidaOptional.isPresent())
                    platoGetDto.setDescCategoria(categoriaComidaOptional.get().getTipo());
                else
                    platoGetDto.setDescCategoria("");
            }
            else
                platoGetDto.setDescCategoria("");
        });

        return platoDtoSet;
    }

    public ResponseEntity addPlatoToComercio(Long comercioId, PlatoDto platoDto) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Plato plato = modelMapper.map(platoDto, Plato.class);
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
                modelMapper.map(platoUpdateDto, plato);
                plato.setComercio(comercio);
                plato.setId(platoId);
                platoUpdateDto.setId(platoId);
                String response = objectMapper.writeValueAsString(platoUpdateDto);

                platoRepository.saveAndFlush(plato);
                comercioPriceUpdater.updatePriceOfComercio(comercioId);

                return ResponseEntity.ok(response);
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún plato con id: " + platoId));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
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
