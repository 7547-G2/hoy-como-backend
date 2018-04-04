package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.MobileUserDto;
import ar.uba.fi.hoycomobackend.entity.mobileuser.MobileUser;
import ar.uba.fi.hoycomobackend.repository.MobileUserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class MobileUserService {

    public static Logger LOGGER = LoggerFactory.getLogger(MobileUserService.class);

    private MobileUserRepository mobileUserRepository;
    private ModelMapper modelMapper;

    @Autowired
    public MobileUserService(MobileUserRepository mobileUserRepository, ModelMapper modelMapper) {
        this.mobileUserRepository = mobileUserRepository;
        this.modelMapper = modelMapper;
    }

    public List<MobileUserDto> getMobileUserList() {
        LOGGER.info("Getting all Usuarios from database.");
        List<MobileUser> mobileUserList = mobileUserRepository.findAll();
        Type listType = new TypeToken<List<MobileUserDto>>() {
        }.getType();
        List<MobileUserDto> mobileUserDtoList = modelMapper.map(mobileUserList, listType);

        return mobileUserDtoList;
    }

    public MobileUserDto getMobileUserById(Long id) {
        LOGGER.info("Getting MobileUser by id: {}", id);
        Optional<MobileUser> mobileUser = mobileUserRepository.getMobileUserById(id);
        MobileUserDto mobileUserDto = modelMapper.map(mobileUser, MobileUserDto.class);

        return mobileUserDto;
    }

    public void addMobileUser(MobileUserDto mobileUserDto) {
        LOGGER.info("Adding new MobileUser: {}", mobileUserDto);
        MobileUser mobileUser = modelMapper.map(mobileUserDto, MobileUser.class);

        mobileUserRepository.saveAndFlush(mobileUser);
    }

    public ResponseEntity getMobileUserAuthorizedById(Long id) {
        LOGGER.info("Checking if MobileUser is authorized by id: {}", id);
        Optional<MobileUser> mobileUser = mobileUserRepository.getMobileUserById(id);

        if(mobileUser.isPresent()) {
            if(mobileUser.get().getAuthorized()) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
