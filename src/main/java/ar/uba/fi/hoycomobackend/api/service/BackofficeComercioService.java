package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.businesslogic.ComercioPriceUpdater;
import ar.uba.fi.hoycomobackend.api.businesslogic.TokenGenerator;
import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
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

@Service
public class BackofficeComercioService {

    private ComercioQuery comercioQuery;
    private PlatoRepository platoRepository;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    private TokenGenerator tokenGenerator;
    private ComercioPriceUpdater comercioPriceUpdater;

    @Autowired
    public BackofficeComercioService(ComercioQuery comercioQuery, PlatoRepository platoRepository, ModelMapper modelMapper, ObjectMapper objectMapper, TokenGenerator tokenGenerator, ComercioPriceUpdater comercioPriceUpdater) {
        this.comercioQuery = comercioQuery;
        this.platoRepository = platoRepository;
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

    public ResponseEntity getPlatosFromComercio(Long comercioId) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            Type setType = new TypeToken<Set<PlatoDto>>() {
            }.getType();
            Set<PlatoDto> platoDtoSet = modelMapper.map(platoSet, setType);
            String response = objectMapper.writeValueAsString(platoDtoSet);

            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
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
                Plato plato = modelMapper.map(platoUpdateDto, Plato.class);
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
}
