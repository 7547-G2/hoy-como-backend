package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserAddDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.service.menu.MenuDisplayer;
import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createDefaultMobileUser;
import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createMobileUser;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MobileUserServiceTest {

    private MobileUserRepository mobileUserRepository = Mockito.mock(MobileUserRepository.class);
    private ComercioQuery comercioQuery = Mockito.mock(ComercioQuery.class);
    private TipoComidaQuery tipoComidaQuery = Mockito.mock(TipoComidaQuery.class);
    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private MenuDisplayer menuDisplayer = Mockito.mock(MenuDisplayer.class);
    private MobileUserService mobileUserService = new MobileUserService(mobileUserRepository, comercioQuery, tipoComidaQuery, modelMapper, objectMapper, menuDisplayer);

    @Test
    public void getMobileUserById_returnsMobileUserDto() {
        MobileUser mobileUser = createMobileUser(1L, "username", "firstName", "lastName");
        Optional<MobileUser> mobileUserOptional = Optional.of(mobileUser);
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);
        when(modelMapper.map(any(MobileUser.class), any())).thenReturn(new MobileUserDto());

        mobileUserService.getMobileUserById(1L);

        verify(mobileUserRepository).getMobileUserByFacebookId(1L);
        verify(modelMapper).map(mobileUser, MobileUserDto.class);
    }

    @Test
    public void addMobileUser_addsUser() {
        MobileUserAddDto mobileUserAddDto = new MobileUserAddDto();
        when(modelMapper.map(mobileUserAddDto, MobileUser.class)).thenReturn(new MobileUser());

        mobileUserService.addMobileUser(mobileUserAddDto);

        verify(modelMapper).map(mobileUserAddDto, MobileUser.class);
        verify(mobileUserRepository).saveAndFlush(any(MobileUser.class));
    }

    @Test
    public void getMobileUserAuthorizedById_returnsStateMessage() throws JsonProcessingException {
        Optional<MobileUser> mobileUserOptional = Optional.of(createDefaultMobileUser());
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        ResponseEntity response = mobileUserService.getMobileUserAuthorizedById(1L);

        HttpStatus statusCode = response.getStatusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getMobileUserAuthorizedById_nonExistant_returnsUserNonExistantMessage() throws JsonProcessingException {
        Optional<MobileUser> mobileUserOptional = Optional.empty();
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        ResponseEntity response = mobileUserService.getMobileUserAuthorizedById(1L);

        HttpStatus statusCode = response.getStatusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
    }
}
