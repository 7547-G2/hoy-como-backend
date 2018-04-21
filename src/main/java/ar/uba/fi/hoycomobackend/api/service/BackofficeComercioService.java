package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
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

    private ComercioRepository comercioRepository;
    private PlatoRepository platoRepository;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    private TokenGenerator tokenGenerator;

    @Autowired
    public BackofficeComercioService(ComercioRepository comercioRepository, PlatoRepository platoRepository, ModelMapper modelMapper, ObjectMapper objectMapper, TokenGenerator tokenGenerator) {
        this.comercioRepository = comercioRepository;
        this.platoRepository = platoRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.tokenGenerator = tokenGenerator;
    }

    public ResponseEntity getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<Comercio> comercioOptional = comercioRepository.getComercioByEmail(givenEmail);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            if (comercio.getEmail().equals(givenEmail) && comercio.getPassword().equals(givenPassword)) {
                Long matchingValidComercioId = comercio.getId();
                TokenDto tokenDto = createToken(matchingValidComercioId);
                String tokenString = tokenDto.getToken();
                comercio.setToken(tokenString);
                comercioRepository.saveAndFlush(comercio);
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
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

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
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Plato plato = modelMapper.map(platoDto, Plato.class);
            plato.setComercio(comercio);

            plato = platoRepository.saveAndFlush(plato);
            String response = objectMapper.writeValueAsString(plato.getId());

            return ResponseEntity.ok(response);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }

    public ResponseEntity updatePlatoFromComercio(Long comercioId, Long platoId, PlatoDto platoDto) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            Optional<Plato> platoOptional = platoSet.stream().filter(plato -> plato.getId().equals(platoId)).findFirst();

            if (platoOptional.isPresent()) {
                Plato plato = modelMapper.map(platoDto, Plato.class);
                plato.setComercio(comercio);
                plato.setId(platoId);
                platoDto.setId(platoId);
                String response = objectMapper.writeValueAsString(platoDto);

                platoRepository.saveAndFlush(plato);

                return ResponseEntity.ok(response);
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún plato con id: " + platoId));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún comercio con id: " + comercioId));
    }
}
