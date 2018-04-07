package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.backoffice.comercio.BackofficeComercioSession;
import ar.uba.fi.hoycomobackend.repository.BackofficeComercioSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackofficeComercioSessionServiceTest {

    private static String SESSION_EMAIL = "email";
    private static String SESSION_PASSWORD = "password";

    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private BackofficeComercioSessionRepository backofficeComercioSessionRepository = Mockito.mock(BackofficeComercioSessionRepository.class);
    private BackofficeComercioSessionService backofficeComercioSessionService = new BackofficeComercioSessionService(backofficeComercioSessionRepository, objectMapper);

    @Test
    public void getTokenFromSession_withValidSession_returnsToken() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        BackofficeComercioSession backofficeComercioSession = new BackofficeComercioSession();
        backofficeComercioSession.setEmail(SESSION_EMAIL);
        backofficeComercioSession.setPassword(SESSION_PASSWORD);
        Optional<BackofficeComercioSession> backofficeComercioSessionOptional = Optional.of(backofficeComercioSession);
        when(backofficeComercioSessionRepository.getBackofficeComercioSessionByEmail(SESSION_EMAIL)).thenReturn(backofficeComercioSessionOptional);

        backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        verify(objectMapper, times(1)).writeValueAsString(any(TokenDto.class));
    }

    @Test
    public void getTokenFromSession_withInvalidSession_returnsEmptyOptional() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Optional<BackofficeComercioSession> backofficeComercioSessionOptional = Optional.empty();
        when(backofficeComercioSessionRepository.getBackofficeComercioSessionByEmail(SESSION_EMAIL)).thenReturn(backofficeComercioSessionOptional);

        backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        verify(objectMapper, times(1)).writeValueAsString(any(String.class));
    }
}