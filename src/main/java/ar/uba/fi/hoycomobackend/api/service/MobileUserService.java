package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.Address;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import ar.uba.fi.hoycomobackend.entity.MobileUserState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MobileUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(MobileUserService.class);
    private static String NON_EXISTANT_USER_MESSAGE = "No existe el usuario";
    private static String ADDRESS_ADDED_SUCCESSFUL = "Se agregó dirección exitosamente";
    private static String MOBILE_USER_ADDED_SUCCESSFUL = "Se agregó usuario exitosamente";
    private static String MOBILE_USER_ADD_UNSUCCESSFUL = "Datos incorrectos al cargar usuario";

    private MobileUserRepository mobileUserRepository;
    private ComercioQuery comercioQuery;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ComercioQuery comercioQuery, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.comercioQuery = comercioQuery;
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
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = mobileUser.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);
            mobileUserDto.setAddressDto(addressDto);

            return mobileUserDto;
        } else
            return new MobileUserDto();
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

    public ResponseEntity getMobileUserAuthorizedById(Long id) {
        LOGGER.info("Checking if MobileUser is authorized by id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserState mobileUserState = mobileUser.getState();
            MobileUserStateDto mobileUserStateDto = new MobileUserStateDto(HttpStatus.OK, mobileUserState);

            return ResponseEntity.ok(mobileUserStateDto);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se encontró ningún usuario con id: " + id));
    }

    public String addFavoriteComercioToMobileUser(Long mobileUserFacebookId, Long comercioId) {
        LOGGER.info("Trying to add Comercio with id: {}, to mobile user with id: {}", comercioId, mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

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
            MobileUserFavoritesDto mobileUserFavoritesDto = getMobileUserFavoritesDtoFromMobileUser(mobileUser);
            response = objectMapper.writeValueAsString(mobileUserFavoritesDto);
        } else {
            response = "No existe el usuario";
        }

        return response;
    }

    private MobileUserFavoritesDto getMobileUserFavoritesDtoFromMobileUser(MobileUser mobileUser) {
        MobileUserFavoritesDto mobileUserFavoritesDto = new MobileUserFavoritesDto();
        Set<Long> favorites = new LinkedHashSet<>();
        List<Comercio> comercioList = mobileUser.getFavoriteComercios();

        for (Comercio comercio : comercioList) {
            favorites.add(comercio.getId());
        }
        mobileUserFavoritesDto.setFavorites(favorites);

        return mobileUserFavoritesDto;
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

    public List<ComercioMobileUserDto> getComercioMobileUserDtoSet(String search) {
        List<Comercio> comercioList = comercioQuery.findBySearchQuery(search);
        List<ComercioMobileUserDto> comercioMobileUserDtoList = getComercioDtos(comercioList);

        return comercioMobileUserDtoList;
    }

    private List<ComercioMobileUserDto> getComercioDtos(List<Comercio> comercioList) {
        List<ComercioMobileUserDto> comercioMobileUserDtoList = new ArrayList<>();
        for (Comercio comercio : comercioList) {
            Address address = comercio.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            ComercioMobileUserDto comercioMobileUserDto = modelMapper.map(comercio, ComercioMobileUserDto.class);
            comercioMobileUserDto.setAddressDto(addressDto);
            comercioMobileUserDtoList.add(comercioMobileUserDto);
        }
        return comercioMobileUserDtoList;
    }

    public String changeStateToMobileUser(Long mobileUserFacebookId, Integer stateCode) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserState mobileUserState = MobileUserState.getByStateCode(stateCode);
            mobileUser.setState(mobileUserState);

            mobileUserRepository.saveAndFlush(mobileUser);

            return "Mobile user with id: " + mobileUserFacebookId + " changed state successfully to: " + mobileUserState.name();
        } else
            return NON_EXISTANT_USER_MESSAGE;
    }
}
