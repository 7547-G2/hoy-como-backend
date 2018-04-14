package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserFavoritesDto;
import ar.uba.fi.hoycomobackend.entity.Address;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MobileUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(MobileUserService.class);
    private static String AUTHORIZED_USER_MESSAGE = "El usuario está habilitado";
    private static String UNAUTHORIZED_USER_MESSAGE = "El usuario está deshabilitado";
    private static String NON_EXISTANT_USER_MESSAGE = "No existe el usuario";
    private static String ADDRESS_ADDED_SUCCESSFUL = "Se agregó dirección exitosamente";
    private static String MOBILE_USER_ADDED_SUCCESSFUL = "Se agregó usuario exitosamente";
    private static String MOBILE_USER_ADD_UNSUCCESSFUL = "Datos incorrectos al cargar usuario";

    private MobileUserRepository mobileUserRepository;
    private ComercioRepository comercioRepository;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ComercioRepository comercioRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.comercioRepository = comercioRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public List<MobileUserDto> getMobileUserList() {
        LOGGER.info("Getting all Usuarios from database.");
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        List<MobileUserDto> mobileUserDtoList = new ArrayList<>();
        for (MobileUser mobileUser : mobileUserList) {
            Address address = mobileUser.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);
            mobileUserDto.setAddressDto(addressDto);
            mobileUserDtoList.add(mobileUserDto);
        }

        return mobileUserDtoList;
    }

    public MobileUserDto getMobileUserById(Long id) {
        LOGGER.info("Getting MobileUser by id: {}", id);
        Optional<MobileUser> mobileUser = mobileUserRepository.getMobileUserByFacebookId(id);
        MobileUserDto mobileUserDto = modelMapper.map(mobileUser.get(), MobileUserDto.class);

        return mobileUserDto;
    }

    public String addMobileUser(MobileUserDto mobileUserDto) {
        LOGGER.info("Adding new MobileUser: {}", mobileUserDto);
        AddressDto addressDto = mobileUserDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        MobileUser mobileUser = modelMapper.map(mobileUserDto, MobileUser.class);
        mobileUser.setAddress(address);
        try {
            mobileUserRepository.saveAndFlush(mobileUser);
            return MOBILE_USER_ADDED_SUCCESSFUL;
        } catch (RuntimeException e) {
            LOGGER.info("Error while adding new mobile user: {}", e.getMessage());
        }
        return MOBILE_USER_ADD_UNSUCCESSFUL;
    }

    public String getMobileUserAuthorizedById(Long id) {
        LOGGER.info("Checking if MobileUser is authorized by id: {}", id);
        Optional<MobileUser> mobileUser = mobileUserRepository.getMobileUserByFacebookId(id);

        String response;
        if (mobileUser.isPresent()) {
            if (mobileUser.get().getAuthorized()) {
                response = AUTHORIZED_USER_MESSAGE;
            } else {
                response = UNAUTHORIZED_USER_MESSAGE;
            }
        } else
            response = NON_EXISTANT_USER_MESSAGE;

        return response;
    }

    public String addFavoriteComercioToMobileUser(Long mobileUserFacebookId, Long comercioId) {
        LOGGER.info("Trying to add Comercio with id: {}, to mobile user with id: {}", comercioId, mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        String response;
        if (mobileUserOptional.isPresent() && comercioOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Comercio comercio = comercioOptional.get();
            mobileUser.getFavoriteComercios().add(comercio);
            comercio.getMobileUserList().add(mobileUser);

            mobileUserRepository.saveAndFlush(mobileUser);
            response = "Alta correcta";
        } else {
            response = "No se pudo dar de alta";
        }
        return response;
    }

    public String getMobileUserFavorites(Long id) throws JsonProcessingException {
        LOGGER.info("Trying to get all mobile user's favorites with id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);

        String response;
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            List<Comercio> comercioList = mobileUser.getFavoriteComercios();
            MobileUserFavoritesDto mobileUserFavoritesDto = modelMapper.map(comercioList, MobileUserFavoritesDto.class);
            response = objectMapper.writeValueAsString(mobileUserFavoritesDto);
        } else {
            response = "No existe el usuario";
        }

        return response;
    }

    public String addAddressToMobileUser(Long mobileUserFacebookId, AddressDto addressDto) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        String response;
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = modelMapper.map(addressDto, Address.class);
            mobileUser.setAddress(address);

            mobileUserRepository.saveAndFlush(mobileUser);
            response = ADDRESS_ADDED_SUCCESSFUL;
        } else
            response = NON_EXISTANT_USER_MESSAGE;

        return response;
    }
}
