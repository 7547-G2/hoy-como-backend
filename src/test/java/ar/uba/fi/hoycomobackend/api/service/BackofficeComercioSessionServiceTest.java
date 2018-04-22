package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import ar.uba.fi.hoycomobackend.utils.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackofficeComercioSessionServiceTest {

    private static String SESSION_EMAIL = "email";
    private static String SESSION_PASSWORD = "password";

    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private PlatoRepository platoRepository = Mockito.mock(PlatoRepository.class);
    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    private TokenGenerator tokenGenerator = Mockito.mock(TokenGenerator.class);
    private BackofficeComercioService backofficeComercioSessionService = new BackofficeComercioService(comercioRepository, platoRepository, modelMapper, objectMapper, tokenGenerator);

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

        verify(objectMapper).writeValueAsString(any(TokenDto.class));
    }

    @Test
    public void getTokenFromSession_withInvalidSession_returnsEmptyOptional() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Optional<Comercio> comercio = Optional.empty();
        when(comercioRepository.getComercioByEmail(SESSION_EMAIL)).thenReturn(comercio);

        ResponseEntity response = backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        ErrorMessage errorMessage = (ErrorMessage) response.getBody();
        String errorMessageContent = errorMessage.getErrorMessage();
        assertThat(errorMessageContent).isEqualTo("No se encontró ningún comercio con email: " + SESSION_EMAIL);
    }
}