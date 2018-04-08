package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BackofficeComercioSessionServiceTest {

    private static String SESSION_EMAIL = "email";
    private static String SESSION_PASSWORD = "password";

    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private TokenGenerator tokenGenerator = Mockito.mock(TokenGenerator.class);
    private BackofficeComercioSessionService backofficeComercioSessionService = new BackofficeComercioSessionService(comercioRepository, objectMapper, tokenGenerator);

    @Test
    public void getTokenFromSession_withValidSession_returnsToken() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Comercio comercio = new Comercio();
        comercio.setEmail(SESSION_EMAIL);
        comercio.setPassword(SESSION_PASSWORD);
        when(comercioRepository.getComercioByEmail(SESSION_EMAIL)).thenReturn(Optional.of(comercio));

        backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        verify(objectMapper, times(1)).writeValueAsString(any(TokenDto.class));
    }

    @Test
    public void getTokenFromSession_withInvalidSession_returnsEmptyOptional() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Optional<Comercio> comercio = Optional.empty();
        when(comercioRepository.getComercioByEmail(SESSION_EMAIL)).thenReturn(comercio);

        backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        verify(objectMapper, times(1)).writeValueAsString(any(String.class));
    }
}