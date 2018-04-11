package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackofficeComercioSessionService {

    private ComercioRepository comercioRepository;
    private ObjectMapper objectMapper;
    private TokenGenerator tokenGenerator;

    @Autowired
    public BackofficeComercioSessionService(ComercioRepository comercioRepository, ObjectMapper objectMapper, TokenGenerator tokenGenerator) {
        this.comercioRepository = comercioRepository;
        this.objectMapper = objectMapper;
        this.tokenGenerator = tokenGenerator;
    }

    public String getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {

        Optional<TokenDto> tokenOptional = generateToken(backofficeComercioSessionDto);
        String response;
        if (tokenOptional.isPresent()) {
            TokenDto tokenDto = tokenOptional.get();
            response = objectMapper.writeValueAsString(tokenDto);
        } else {
            response = objectMapper.writeValueAsString("Datos incorrectos");
        }

        return response;
    }

    private Optional<TokenDto> generateToken(BackofficeComercioSessionDto backofficeComercioSessionDto) {
        Optional<Comercio> matchingValidComercio = getMatchingValidComercio(backofficeComercioSessionDto);
        if (matchingValidComercio.isPresent()) {
            Comercio comercio = matchingValidComercio.get();
            TokenDto tokenDto = createToken();
            String token = tokenDto.getToken();
            comercio.setToken(token);
            comercioRepository.save(comercio);

            return Optional.of(tokenDto);
        } else {
            return Optional.empty();
        }
    }

    private TokenDto createToken() {
        TokenDto tokenDto = new TokenDto();
        String token = tokenGenerator.createToken();
        tokenDto.setToken(token);

        return tokenDto;
    }

    private Optional<Comercio> getMatchingValidComercio(BackofficeComercioSessionDto backofficeComercioSessionDto) {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<Comercio> comercio = comercioRepository.getComercioByEmail(givenEmail);

        if (comercio.isPresent() &&
                comercio.get().getEmail().equals(givenEmail) &&
                comercio.get().getPassword().equals(givenPassword)) {
            return comercio;
        } else {
            return Optional.empty();
        }
    }
}
