package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.uba.fi.hoycomobackend.entity.DataTestBuilder.createMobileUser;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MobileUserServiceTest {

    private MobileUserRepository mobileUserRepository = Mockito.mock(MobileUserRepository.class);
    private ComercioRepository comercioRepository = Mockito.mock(ComercioRepository.class);
    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private MobileUserService mobileUserService = new MobileUserService(mobileUserRepository, comercioRepository, modelMapper, objectMapper);

    @Test
    public void getMobileUserById_returnsMobileUserDto() {
        MobileUser mobileUser = createMobileUser(1L, "username", "firstName", "lastName");
        Optional<MobileUser> mobileUserOptional = Optional.of(mobileUser);
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        mobileUserService.getMobileUserById(1L);

        verify(mobileUserRepository).getMobileUserByFacebookId(1L);
        verify(modelMapper).map(mobileUser, MobileUserDto.class);
    }

    @Test
    public void addMobileUser_addsUser() {
        MobileUserDto mobileUserDto = new MobileUserDto();
        when(modelMapper.map(mobileUserDto, MobileUser.class)).thenReturn(new MobileUser());

        mobileUserService.addMobileUser(mobileUserDto);

        verify(modelMapper).map(mobileUserDto, MobileUser.class);
        verify(mobileUserRepository).saveAndFlush(any(MobileUser.class));
    }

    @Test
    public void getMobileUserAuthorizedById_authorized_returnsAuthorizedMessage() {
        Optional<MobileUser> mobileUserOptional = Optional.of(createMobileUser(1L, "username", "firstName", "lastName"));
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        String response = mobileUserService.getMobileUserAuthorizedById(1L);

        assertThat(response).isEqualTo("El usuario está habilitado");
    }

    @Test
    public void getMobileUserAuthorizedById_nonExistant_returnsUserNonExistantMessage() {
        Optional<MobileUser> mobileUserOptional = Optional.empty();
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        String response = mobileUserService.getMobileUserAuthorizedById(1L);

        assertThat(response).isEqualTo("No existe el usuario");
    }

    @Test
    public void getMobileUserAuthorizedById_notAuthorized_returnsUserNotAuthorizedMessage() {
        Optional<MobileUser> mobileUserOptional = Optional.of(createMobileUser(1L, "username", "firstName", "lastName", false));
        when(mobileUserRepository.getMobileUserByFacebookId(1L)).thenReturn(mobileUserOptional);

        String response = mobileUserService.getMobileUserAuthorizedById(1L);

        assertThat(response).isEqualTo("El usuario está deshabilitado");
    }
}
