package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.backoffice.comercio.BackofficeComercioSession;
import ar.uba.fi.hoycomobackend.repository.BackofficeComercioSessionRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackofficeComercioSessionService {

    private BackofficeComercioSessionRepository backofficeComercioSessionRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public BackofficeComercioSessionService(BackofficeComercioSessionRepository backofficeComercioSessionRepository, ObjectMapper objectMapper) {
        this.backofficeComercioSessionRepository = backofficeComercioSessionRepository;
        this.objectMapper = objectMapper;
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
        Optional<BackofficeComercioSession> matchingValidUser = getMatchingValidUser(backofficeComercioSessionDto);
        if(matchingValidUser.isPresent()) {
            BackofficeComercioSession backofficeComercioSession = matchingValidUser.get();
            TokenDto tokenDto = createToken();
            String token = tokenDto.getToken();
            backofficeComercioSession.setToken(token);
            backofficeComercioSessionRepository.save(backofficeComercioSession);

            return Optional.of(tokenDto);
        } else {
            return Optional.empty();
        }
    }

    private TokenDto createToken() {
        TokenDto tokenDto = new TokenDto();
        String token = TokenGenerator.createToken();
        tokenDto.setToken(token);

        return tokenDto;
    }

    private Optional<BackofficeComercioSession> getMatchingValidUser(BackofficeComercioSessionDto backofficeComercioSessionDto) {
        String givenEmail = backofficeComercioSessionDto.getEmail();
        String givenPassword = backofficeComercioSessionDto.getPassword();
        Optional<BackofficeComercioSession> backofficeComercioSession = backofficeComercioSessionRepository.getBackofficeComercioSessionByEmail(givenEmail);

        if (backofficeComercioSession.isPresent() &&
                backofficeComercioSession.get().getEmail().equals(givenEmail) &&
                backofficeComercioSession.get().getPassword().equals(givenPassword)) {
            return backofficeComercioSession;
        } else {
            return Optional.empty();
        }
    }
}
