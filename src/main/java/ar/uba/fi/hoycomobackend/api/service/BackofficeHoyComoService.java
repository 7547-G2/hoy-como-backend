package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ChangeStateDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoAddDto;
import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.database.entity.*;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.UsuarioQuery;
import ar.uba.fi.hoycomobackend.database.repository.TipoComidaRepository;
import org.apache.commons.lang.RandomStringUtils;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BackofficeHoyComoService {

    public static final int MININUM_AMOUNT_OF_PLATOS = 5;
    private static Logger LOGGER = LoggerFactory.getLogger(BackofficeHoyComoService.class);

    private ComercioQuery comercioQuery;
    private UsuarioQuery usuarioQuery;
    private ModelMapper modelMapper;
    private MailingService mailingService;
    private TipoComidaRepository tipoComidaRepository;

    @Autowired
    public BackofficeHoyComoService(ComercioQuery comercioQuery, UsuarioQuery usuarioQuery, ModelMapper modelMapper, MailingService mailingService, TipoComidaRepository tipoComidaRepository) {
        this.comercioQuery = comercioQuery;
        this.usuarioQuery = usuarioQuery;
        this.modelMapper = modelMapper;
        this.mailingService = mailingService;
        this.tipoComidaRepository = tipoComidaRepository;
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
            if (cannotChangeState(comercioHoyComoAddDto, comercio))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede habilitar un comercio que no haya cargado al menos 5 platos");
            return updateComercioToDatabaseFromDto(comercioHoyComoAddDto, comercio);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún comercio con id: " + comercioId);
    }

    private boolean cannotChangeState(ComercioHoyComoAddDto comercioHoyComoAddDto, Comercio comercio) {
        long amountOfPlatos = getAmountOfPlatos(comercio);
        String newEstado = comercioHoyComoAddDto.getEstado();
        if ("habilitado".equalsIgnoreCase(newEstado) && amountOfPlatos < MININUM_AMOUNT_OF_PLATOS && "pendiente menu".equalsIgnoreCase(comercio.getEstado())) {
            return true;
        }
        return false;
    }

    private long getAmountOfPlatos(Comercio comercio) {
        Set<Plato> platoSet = comercio.getPlatos();
        platoSet = platoSet.stream().filter(plato -> !PlatoState.BORRADO.equals(plato.getState())).collect(Collectors.toSet());
        return platoSet.stream().count();
    }

    private ResponseEntity updateComercioToDatabaseFromDto(ComercioHoyComoAddDto comercioHoyComoAddDto, Comercio comercio) {
        comercio = getUpdatedComercio(comercioHoyComoAddDto, comercio);
        try {
            comercioQuery.saveAndFlush(comercio);
            return ResponseEntity.ok("Comercio updateado correctamente");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Got the following error while trying to save a new Comercio: {}", e);

            String causeDetail = getCauseDetail(e);
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a " + causeDetail);
        }
    }

    private Comercio getUpdatedComercio(ComercioHoyComoAddDto comercioHoyComoAddDto, Comercio comercio) {
        TipoComida previousTipoComida = comercio.getTipoComida();
        modelMapper.map(comercioHoyComoAddDto, comercio);
        comercio.setTipoComida(previousTipoComida);
        AddressDto addressDto = comercioHoyComoAddDto.getAddressDto();
        if (addressDto != null) {
            Address address = modelMapper.map(addressDto, Address.class);
            comercio.setAddress(address);
        }
        comercio = updateComercioWithTipoComercio(comercioHoyComoAddDto, comercio);
        return comercio;
    }

    private Comercio updateComercioWithTipoComercio(ComercioHoyComoAddDto comercioHoyComoAddDto, Comercio comercio) {
        Long tipoComidaId = comercioHoyComoAddDto.getTipoComidaId();
        if (tipoComidaId != null && (comercio.getTipoComida() == null || !tipoComidaId.equals(comercio.getTipoComida().getId()))) {
            Optional<TipoComida> tipoComidaOptional = tipoComidaRepository.findById(tipoComidaId);
            tipoComidaOptional.ifPresent(tipoComida -> comercio.setTipoComida(tipoComida));
        }
        return comercio;
    }

    private ResponseEntity addComercioToDatabaseFromDto(ComercioHoyComoAddDto comercioHoyComoAddDto) {
        Long tipoComidaId = comercioHoyComoAddDto.getTipoComidaId();
        if (hasInvalidTipoComercioId(tipoComidaId)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No se pudo agregar el comercio debido a que el tipo de comida con id: " + tipoComidaId + " no existe en la base de datos.");
        }
        AddressDto addressDto = comercioHoyComoAddDto.getAddressDto();
        Address address = modelMapper.map(addressDto, Address.class);
        Comercio comercio = modelMapper.map(comercioHoyComoAddDto, Comercio.class);
        TipoComida tipoComida = tipoComidaRepository.findById(tipoComidaId).get();
        comercio.setTipoComida(tipoComida);
        comercio.setAddress(address);
        comercio = updateComercioWithTipoComercio(comercioHoyComoAddDto, comercio);
        comercio = updateComercioWithRandomPassword(comercio);
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

    private Comercio updateComercioWithRandomPassword(Comercio comercio) {
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        comercio.setPassword(generatedString);

        return comercio;
    }

    private boolean hasInvalidTipoComercioId(Long tipoComidaId) {
        return !tipoComidaRepository.findById(tipoComidaId).isPresent();
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


    public ResponseEntity getUsuarios(String search) {
        List<MobileUser> mobileUserList = usuarioQuery.findBySearchQuery(search);

        return ResponseEntity.ok(mobileUserList);
    }

    public ResponseEntity changeStateOfUserById(Long mobileUserId, ChangeStateDto changeStateDto) {
        Optional<MobileUser> mobileUserOptional = usuarioQuery.findById(mobileUserId);
        if (mobileUserOptional.isPresent()) {
            MobileUser mobileUser = mobileUserOptional.get();
            mobileUser.setState(changeStateDto.getState());
            mobileUser.setMotivoDeshabilitacion(changeStateDto.getMotivoDeshabilitacion());

            usuarioQuery.save(mobileUser);

            return ResponseEntity.ok("Estado cambiado al usuario a: " + mobileUser.getState());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("No fue encontrado el usuario con ese ID"));
    }

    public ResponseEntity createTipoComida(String tipoComida) {
        TipoComida tipoComidaEntity = new TipoComida();
        tipoComidaEntity.setTipo(tipoComida);
        tipoComidaEntity = tipoComidaRepository.saveAndFlush(tipoComidaEntity);

        return ResponseEntity.ok(tipoComidaEntity);
    }
}
