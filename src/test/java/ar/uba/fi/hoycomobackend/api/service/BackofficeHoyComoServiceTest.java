package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.entity.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercio;
import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultComercioHoyComoDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackofficeHoyComoServiceTest {

    private static String COMERCIO_NOMBRE = "nombre";
    private ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    private BackofficeHoyComoService backofficeHoyComoService = new BackofficeHoyComoService(comercioRepository, modelMapper);

    @Before
    public void setUp() {
        List<Comercio> comercioList = Arrays.asList(createDefaultComercio());

        when(comercioRepository.findByNombre(COMERCIO_NOMBRE)).thenReturn(comercioList);
    }

    @Test
    public void whenValidName_thenComercioShouldBeFound() {
        ComercioHoyComoDto comercioHoyComoDto = createDefaultComercioHoyComoDto();
        when(modelMapper.map(any(Comercio.class), any())).thenReturn(comercioHoyComoDto);
        backofficeHoyComoService.getComercioByNombre(COMERCIO_NOMBRE);

        verify(comercioRepository).findByNombre(COMERCIO_NOMBRE);
    }
}