package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.Plato;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.repository.PlatoRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
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

    public String getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<Comercio> comercio = comercioRepository.getComercioByEmail(givenEmail);

        if (comercio.isPresent()) {
            if (comercio.get().getEmail().equals(givenEmail) && comercio.get().getPassword().equals(givenPassword)) {
                Comercio matchingValidComercio = comercio.get();
                TokenDto tokenDto = createToken();
                String tokenString = tokenDto.getToken();
                matchingValidComercio.setToken(tokenString);
                comercioRepository.save(matchingValidComercio);

                return objectMapper.writeValueAsString(tokenDto);
            } else
                return "Usuario o password de comercio incorrecto";
        } else
            return "No se encontró ningún comercio con email: " + givenEmail;
    }

    private TokenDto createToken() {
        TokenDto tokenDto = new TokenDto();
        String token = tokenGenerator.createToken();
        tokenDto.setToken(token);

        return tokenDto;
    }

    public String addPlatoToComercio(Long comercioId, PlatoDto platoDto) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Plato plato = modelMapper.map(platoDto, Plato.class);
            plato.setComercio(comercio);

            plato = platoRepository.saveAndFlush(plato);

            return objectMapper.writeValueAsString(plato.getId());
        } else
            return "No se encontró ningún comercio con id: " + comercioId;
    }

    public String getPlatosFromComercio(Long comercioId) throws JsonProcessingException {
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            Set<Plato> platoSet = comercio.getPlatos();
            Type setType = new TypeToken<Set<PlatoDto>>() {
            }.getType();
            Set<PlatoDto> platoDtoSet = modelMapper.map(platoSet, setType);

            return objectMapper.writeValueAsString(platoDtoSet);
        } else
            return "No se encontró ningún comercio con id: " + comercioId;
    }
}
