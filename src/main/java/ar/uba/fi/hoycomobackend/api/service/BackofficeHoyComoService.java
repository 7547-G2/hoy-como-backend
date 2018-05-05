package ar.uba.fi.hoycomobackend.api.service;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.PlatoState;
import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoAddDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.database.entity.Address;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Service
public class BackofficeHoyComoService {

    private static Logger LOGGER = LoggerFactory.getLogger(BackofficeHoyComoService.class);

    private ComercioQuery comercioQuery;
    private ModelMapper modelMapper;
    private MailingService mailingService;

    @Autowired
    public BackofficeHoyComoService(ComercioQuery comercioQuery, ModelMapper modelMapper, MailingService mailingService) {
        this.comercioQuery = comercioQuery;
        this.modelMapper = modelMapper;
        this.mailingService = mailingService;
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
            Set<Plato> platoSet = comercio.getPlatos();
            platoSet = platoSet.stream().filter(plato -> !PlatoState.BORRADO.equals(plato.getState())).collect(Collectors.toSet());
            long cantidad = platoSet.stream().count();
            String estadoHabilitado = "habilitado";
            String estadoPendienteMenu = "pendiente menu";
            String estadoNuevo = comercioHoyComoAddDto.getEstado();
            if(estadoNuevo == estadoHabilitado && cantidad < 5 && comercio.getEstado() == estadoPendienteMenu){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede habilitar un comercio que no haya cargado al menos 5 platos");
            }
            return updateComercioToDatabaseFromDto(comercioHoyComoAddDto, comercio);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún comercio con id: " + comercioId);
    }

    private ResponseEntity updateComercioToDatabaseFromDto(ComercioHoyComoAddDto comercioHoyComoAddDto, Comercio comercio) {
        modelMapper.map(comercioHoyComoAddDto, comercio);
        AddressDto addressDto = comercioHoyComoAddDto.getAddressDto();
        if (addressDto != null) {
            Address address = modelMapper.map(addressDto, Address.class);
            comercio.setAddress(address);
        }
        try {
            comercioQuery.saveAndFlush(comercio);
            return ResponseEntity.ok("Comercio updateado correctamente");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);

            String causeDetail = getCauseDetail(e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a " + causeDetail);
        }
    }

    private ResponseEntity addComercioToDatabaseFromDto(ComercioHoyComoAddDto comercioHoyComoAddDto) {
        AddressDto addressDto = comercioHoyComoAddDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        Comercio comercio = modelMapper.map(comercioHoyComoAddDto, Comercio.class);
        comercio.setAddress(address);
        try {
            comercio = comercioQuery.saveAndFlush(comercio);
            mailingService.sendMailToNewComercio(comercio);
            return ResponseEntity.ok("Comercio creado correctamente");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);

            String causeDetail = getCauseDetail(e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a " + causeDetail);
        }
    }

    private String getCauseDetail(DataIntegrityViolationException exception) {
        try {
            String cause = exception.getCause().getCause().getMessage();
            String[] causeSplit = cause.split("Detail: ");

            return causeSplit[1];
        } catch (Exception e) {
            return e.getMessage();
        }
    }


}
