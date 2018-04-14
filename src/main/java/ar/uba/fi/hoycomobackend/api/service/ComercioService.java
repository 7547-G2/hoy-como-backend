package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioDto;
import ar.uba.fi.hoycomobackend.entity.Address;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.repository.ComercioRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComercioService {

    private static Logger LOGGER = LoggerFactory.getLogger(ComercioService.class);
    private static String CREATION_SUCCESSFUL = "Comercio creado correctamente";

    private ComercioRepository comercioRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ComercioService(ComercioRepository comercioRepository, ModelMapper modelMapper) {
        this.comercioRepository = comercioRepository;
        this.modelMapper = modelMapper;
    }

    public List<ComercioDto> getComercioByNombre(String nombre) {
        LOGGER.info("Getting all Comercios with name: {}", nombre);
        List<Comercio> comercioList = comercioRepository.findByNombre(nombre);
        List<ComercioDto> comercioDtoList = getComercioDtos(comercioList);

        return comercioDtoList;
    }

    public List<ComercioDto> getAllComercios() {
        LOGGER.info("Getting all Comercios");
        List<Comercio> comercioList = comercioRepository.findAll();
        List<ComercioDto> comercioDtoList = getComercioDtos(comercioList);

        return comercioDtoList;
    }

    private List<ComercioDto> getComercioDtos(List<Comercio> comercioList) {
        List<ComercioDto> comercioDtoList = new ArrayList<>();
        for (Comercio comercio : comercioList) {
            Address address = comercio.getAddress();
            AddressDto addressDto = modelMapper.map(address, AddressDto.class);
            ComercioDto comercioDto = modelMapper.map(comercio, ComercioDto.class);
            comercioDto.setAddressDto(addressDto);
            comercioDtoList.add(comercioDto);
        }
        return comercioDtoList;
    }

    public String addComercio(ComercioDto comercioDto) {
        LOGGER.info("Adding a new Comercio");
        Comercio comercio = modelMapper.map(comercioDto, Comercio.class);
        AddressDto addressDto = comercioDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        comercio.setAddress(address);
        String response;
        try {
            comercioRepository.saveAndFlush(comercio);
            response = CREATION_SUCCESSFUL;
        } catch (RuntimeException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);
            response = "No se pudo agregar el comercio debido a " + e.getCause().getMessage();
        }

        return response;
    }
}
