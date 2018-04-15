package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.entity.Address;
import ar.uba.fi.hoycomobackend.entity.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BackofficeHoyComoService {

    private static Logger LOGGER = LoggerFactory.getLogger(BackofficeHoyComoService.class);
    private static String CREATION_SUCCESSFUL = "Comercio creado correctamente";

    private ComercioRepository comercioRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BackofficeHoyComoService(ComercioRepository comercioRepository, ModelMapper modelMapper) {
        this.comercioRepository = comercioRepository;
        this.modelMapper = modelMapper;
    }

    public List<ComercioHoyComoDto> getComercioByNombre(String nombre) {
        LOGGER.info("Getting all Comercios with name: {}", nombre);
        List<Comercio> comercioList = comercioRepository.findByNombre(nombre);
        List<ComercioHoyComoDto> comercioHoyComoDtoList = getComercioDtos(comercioList);

        return comercioHoyComoDtoList;
    }

    public List<ComercioHoyComoDto> getAllComercios() {
        LOGGER.info("Getting all Comercios");
        List<Comercio> comercioList = comercioRepository.findAll();
        List<ComercioHoyComoDto> comercioHoyComoDtoList = getComercioDtos(comercioList);

        return comercioHoyComoDtoList;
    }

    private List<ComercioHoyComoDto> getComercioDtos(List<Comercio> comercioList) {
        List<ComercioHoyComoDto> comercioHoyComoDtoList = new ArrayList<>();
        for (Comercio comercio : comercioList) {
            Address address = comercio.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            ComercioHoyComoDto comercioHoyComoDto = modelMapper.map(comercio, ComercioHoyComoDto.class);
            comercioHoyComoDto.setAddressDto(addressDto);
            comercioHoyComoDtoList.add(comercioHoyComoDto);
        }
        return comercioHoyComoDtoList;
    }

    public ResponseEntity addComercio(ComercioHoyComoDto comercioHoyComoDto) {
        LOGGER.info("Adding a new Comercio");
        comercioHoyComoDto.setId(null);

        return addComercioToDatabaseFromDto(comercioHoyComoDto);
    }

    public ResponseEntity updateComercio(Long comercioId, ComercioHoyComoDto comercioHoyComoDto) {
        LOGGER.info("Trying to update comercio with id: {}", comercioId);
        Optional<Comercio> comercioOptional = comercioRepository.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            comercioHoyComoDto.setId(comercio.getId());

            return addComercioToDatabaseFromDto(comercioHoyComoDto);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún comercio con id: " + comercioId);
    }

    private ResponseEntity addComercioToDatabaseFromDto(ComercioHoyComoDto comercioHoyComoDto) {
        Comercio comercio = modelMapper.map(comercioHoyComoDto, Comercio.class);
        AddressDto addressDto = comercioHoyComoDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        comercio.setAddress(address);
        try {
            comercioRepository.saveAndFlush(comercio);
            return ResponseEntity.ok(CREATION_SUCCESSFUL);
        } catch (RuntimeException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a " + e.getCause().getMessage());
        }
    }

}
