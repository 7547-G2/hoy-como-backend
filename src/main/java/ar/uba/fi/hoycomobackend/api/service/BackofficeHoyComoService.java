package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoAddDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.database.entity.Address;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    private ComercioQuery comercioQuery;
    private ModelMapper modelMapper;

    @Autowired
    public BackofficeHoyComoService(ComercioQuery comercioQuery, ModelMapper modelMapper) {
        this.comercioQuery = comercioQuery;
        this.modelMapper = modelMapper;
    }

    public List<ComercioHoyComoDto> getComercioByNombre(String nombre) {
        List<Comercio> comercioList = comercioQuery.findByNombre(nombre);
        List<ComercioHoyComoDto> comercioHoyComoDtoList = getComercioDtos(comercioList);

        return comercioHoyComoDtoList;
    }

    public List<ComercioHoyComoDto> getComercios(String search) {
        List<Comercio> comercioList = comercioQuery.findBySearchQuery(search);
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

    public ResponseEntity addComercio(ComercioHoyComoAddDto comercioHoyComoAddDto) {
        LOGGER.info("Adding a new Comercio");
        comercioHoyComoAddDto.setId(null);

        return addComercioToDatabaseFromDto(comercioHoyComoAddDto);
    }

    public ResponseEntity updateComercio(Long comercioId, ComercioHoyComoAddDto comercioHoyComoAddDto) {
        LOGGER.info("Trying to update comercio with id: {}", comercioId);
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);

        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            comercioHoyComoAddDto.setId(comercio.getId());

            return addComercioToDatabaseFromDto(comercioHoyComoAddDto);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún comercio con id: " + comercioId);
    }

    private ResponseEntity addComercioToDatabaseFromDto(ComercioHoyComoAddDto comercioHoyComoAddDto) {
        Comercio comercio = modelMapper.map(comercioHoyComoAddDto, Comercio.class);
        AddressDto addressDto = comercioHoyComoAddDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        comercio.setAddress(address);
        try {
            comercioQuery.saveAndFlush(comercio);
            return ResponseEntity.ok(CREATION_SUCCESSFUL);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);

            String causeDetail = getCauseDetail(e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a " + causeDetail);
        }
    }

    private String getCauseDetail(DataIntegrityViolationException exception) {
        try {
            String cause =  exception.getCause().getCause().getMessage();
            String[] causeSplit = cause.split("Detail: ");

            return causeSplit[1];
        } catch (Exception e) {
            return e.getMessage();
        }
    }


}
