package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.businesslogic.ComercioPriceUpdater;
import ar.uba.fi.hoycomobackend.api.businesslogic.TokenGenerator;
import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.TokenDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.CategoriaComidaRepository;
import ar.uba.fi.hoycomobackend.database.repository.OpcionRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
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
    private ComercioQuery comercioQuery = Mockito.mock(ComercioQuery.class);
    private PlatoRepository platoRepository = Mockito.mock(PlatoRepository.class);
    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    private TokenGenerator tokenGenerator = Mockito.mock(TokenGenerator.class);
    private ComercioPriceUpdater comercioPriceUpdater = Mockito.mock(ComercioPriceUpdater.class);
    private CategoriaComidaRepository categoriaComidaRepository = Mockito.mock(CategoriaComidaRepository.class);
    private OpcionRepository opcionRepository = Mockito.mock(OpcionRepository.class);
    private BackofficeComercioService backofficeComercioSessionService = new BackofficeComercioService(comercioQuery, platoRepository, categoriaComidaRepository, modelMapper, objectMapper, tokenGenerator, comercioPriceUpdater, opcionRepository);

    @Test
    public void getTokenFromSession_withValidSession_returnsToken() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Comercio comercio = new Comercio();
        comercio.setEmail(SESSION_EMAIL);
        comercio.setPassword(SESSION_PASSWORD);
        when(comercioQuery.getComercioByEmail(SESSION_EMAIL)).thenReturn(Optional.of(comercio));

        backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        verify(objectMapper).writeValueAsString(any(TokenDto.class));
    }

    @Test
    public void getTokenFromSession_withInvalidSession_returnsEmptyOptional() throws JsonProcessingException {
        BackofficeComercioSessionDto backofficeComercioSessionDto = new BackofficeComercioSessionDto();
        backofficeComercioSessionDto.setEmail(SESSION_EMAIL);
        backofficeComercioSessionDto.setPassword(SESSION_PASSWORD);
        Optional<Comercio> comercio = Optional.empty();
        when(comercioQuery.getComercioByEmail(SESSION_EMAIL)).thenReturn(comercio);

        ResponseEntity response = backofficeComercioSessionService.getTokenFromSession(backofficeComercioSessionDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        ErrorMessage errorMessage = (ErrorMessage) response.getBody();
        String errorMessageContent = errorMessage.getErrorMessage();
        assertThat(errorMessageContent).isEqualTo("No se encontró ningún comercio con email: " + SESSION_EMAIL);
    }
}