package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.backoffice.comercio.BackofficeComercioSession;
import ar.uba.fi.hoycomobackend.repository.BackofficeComercioSessionRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackofficeComercioSessionService {

    private BackofficeComercioSessionRepository backofficeComercioSessionRepository;

    @Autowired
    public BackofficeComercioSessionService(BackofficeComercioSessionRepository backofficeComercioSessionRepository) {
        this.backofficeComercioSessionRepository = backofficeComercioSessionRepository;
    }

    public Optional<TokenDto> getTokenFromSession(BackofficeComercioSessionDto backofficeComercioSessionDto) {
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
        TokenDto tokenDto = createToken(); new TokenDto();
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
