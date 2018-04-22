package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.TipoComidaQuery;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
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
    private static String ADDRESS_ADDED_SUCCESSFUL = "Se agregó dirección exitosamente";
    private static String MOBILE_USER_ADDED_SUCCESSFUL = "Se agregó usuario exitosamente";
    private static String MOBILE_USER_ADD_UNSUCCESSFUL = "Datos incorrectos al cargar usuario";

    private MobileUserRepository mobileUserRepository;
    private ComercioQuery comercioQuery;
    private TipoComidaQuery tipoComidaQuery;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ComercioQuery comercioQuery, TipoComidaQuery tipoComidaQuery, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.comercioQuery = comercioQuery;
        this.tipoComidaQuery = tipoComidaQuery;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity getMobileUserList() {
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

        return ResponseEntity.ok(mobileUserDtoList);
    }

    public ResponseEntity getMobileUserById(Long id) {
        LOGGER.info("Getting MobileUser by id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = mobileUser.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);
            mobileUserDto.setAddressDto(addressDto);

            return ResponseEntity.ok(mobileUserDto);
        } else
            return ResponseEntity.ok(new MobileUserDto());
    }

    public ResponseEntity addMobileUser(MobileUserAddDto mobileUserAddDto) {
        LOGGER.info("Adding new MobileUser: {}", mobileUserAddDto);
        AddressDto addressDto = mobileUserAddDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        MobileUser mobileUser = modelMapper.map(mobileUserAddDto, MobileUser.class);
        mobileUser.setAddress(address);
        try {
            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok(MOBILE_USER_ADDED_SUCCESSFUL);
        } catch (RuntimeException e) {
            LOGGER.info("Error while adding new mobile user: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(MOBILE_USER_ADD_UNSUCCESSFUL));
    }

    public ResponseEntity getMobileUserAuthorizedById(Long id) throws JsonProcessingException {
        LOGGER.info("Checking if MobileUser is authorized by id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);
        MobileUserStateDto mobileUserStateDto;
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserState mobileUserState = mobileUser.getState();
            mobileUserStateDto = new MobileUserStateDto(mobileUserState);
        } else
            mobileUserStateDto = new MobileUserStateDto(MobileUserState.NOT_FOUND);

        String mobileUserStateDtoJson = objectMapper.writeValueAsString(mobileUserStateDto);
        return ResponseEntity.ok(mobileUserStateDtoJson);
    }

    public ResponseEntity addFavoriteComercioToMobileUser(Long mobileUserFacebookId, Long comercioId) {
        LOGGER.info("Trying to add Comercio with id: {}, to mobile user with id: {}", comercioId, mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (mobileUserOptional.isPresent() && comercioOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Comercio comercio = comercioOptional.get();
            mobileUser.getFavoriteComercios().add(comercio);
            comercio.getMobileUserList().add(mobileUser);

            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok("Comercio con id: " + comercioId + "agregado a favoritos al usuario con id: " + mobileUserFacebookId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo dar de alta debido a que el usuario o el comercio no fueron encontrados"));
        }
    }

    public ResponseEntity getMobileUserFavorites(Long id) throws JsonProcessingException {
        LOGGER.info("Trying to get all mobile user's favorites with id: {}", id);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(id);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserFavoritesDto mobileUserFavoritesDto = getMobileUserFavoritesDtoFromMobileUser(mobileUser);
            return ResponseEntity.ok(objectMapper.writeValueAsString(mobileUserFavoritesDto));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + id));
        }
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

    public ResponseEntity addAddressToMobileUser(Long mobileUserFacebookId, AddressDto addressDto) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            Address address = modelMapper.map(addressDto, Address.class);
            mobileUser.setAddress(address);

            mobileUserRepository.saveAndFlush(mobileUser);
            return ResponseEntity.ok(ADDRESS_ADDED_SUCCESSFUL);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + mobileUserFacebookId));
    }

    public ResponseEntity getComercioMobileUserDtoSet(String search) {
        List<Comercio> comercioList = comercioQuery.findBySearchQuery(search);
        List<ComercioMobileUserDto> comercioMobileUserDtoList = getComercioDtos(comercioList);

        return ResponseEntity.ok(comercioMobileUserDtoList);
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

    public ResponseEntity changeStateToMobileUser(Long mobileUserFacebookId, Integer stateCode) {
        LOGGER.info("Getting mobile user with id: {}", mobileUserFacebookId);
        Optional<MobileUser> mobileUserOptional = mobileUserRepository.getMobileUserByFacebookId(mobileUserFacebookId);

        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            MobileUserState mobileUserState = MobileUserState.getByStateCode(stateCode);
            mobileUser.setState(mobileUserState);

            mobileUserRepository.saveAndFlush(mobileUser);

            return ResponseEntity.ok("Mobile user with id: " + mobileUserFacebookId + " changed state successfully to: " + mobileUserState.name());
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No existe el usuario con id: " + mobileUserFacebookId));
    }

    public ResponseEntity getTipoComidaDtoSet() {
        Set<TipoComida> tipoComidaSet = tipoComidaQuery.getAll();
        Set<TipoComidaDto> tipoComidaDtoSet = getTipoComidaDtoSet(tipoComidaSet);

        return ResponseEntity.ok(tipoComidaDtoSet);
    }

    private Set<TipoComidaDto> getTipoComidaDtoSet(Set<TipoComida> tipoComidaSet) {
        Set<TipoComidaDto> tipoComidaDtoSet = new HashSet<>();
        for(TipoComida tipoComida : tipoComidaSet) {
            TipoComidaDto tipoComidaDto = modelMapper.map(tipoComida, TipoComidaDto.class);
            tipoComidaDtoSet.add(tipoComidaDto);
        }

        return tipoComidaDtoSet;
    }
}
