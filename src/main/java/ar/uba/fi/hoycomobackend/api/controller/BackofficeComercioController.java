package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.PasswordUpdateDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoUpdateDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeComercioService;
import ar.uba.fi.hoycomobackend.api.service.ComercioService;
import ar.uba.fi.hoycomobackend.api.service.ComidasService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class BackofficeComercioController {

    private BackofficeComercioService backofficeComercioService;
    private ComercioService comercioService;
    private ComidasService comidasService;

    @Autowired
    public BackofficeComercioController(BackofficeComercioService backofficeComercioSessionService, ComercioService comercioService, ComidasService comidasService) {
        this.backofficeComercioService = backofficeComercioSessionService;
        this.comercioService = comercioService;
        this.comidasService = comidasService;
    }

    @PostMapping(value = "/backofficeComercio/session", produces = {"application/json"})
    public ResponseEntity session(@RequestBody BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        return backofficeComercioService.getTokenFromSession(backofficeComercioSessionDto);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/platos", produces = {"application/json"})
    public ResponseEntity getPlatosFromComercio(@PathVariable("comercioId") Long comercioId) throws JsonProcessingException {
        return backofficeComercioService.getPlatosHabilitadosFromComercio(comercioId);
    }

    @PostMapping(value = "/backofficeComercio/{comercioId}/platos")
    public ResponseEntity addPlato(@PathVariable("comercioId") Long comercioId, @RequestBody PlatoDto platoDto) throws JsonProcessingException {
        return backofficeComercioService.addPlatoToComercio(comercioId, platoDto);
    }

    @PutMapping(value = "/backofficeComercio/{comercioId}/platos/{platoId}")
    public ResponseEntity updatePlato(@PathVariable("comercioId") Long comercioId, @PathVariable("platoId") Long platoId, @RequestBody PlatoUpdateDto platoUpdateDto) throws JsonProcessingException {
        return backofficeComercioService.updatePlatoFromComercio(comercioId, platoId, platoUpdateDto);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}", produces = {"application/json"})
    public ResponseEntity getComercioById(@PathVariable("comercioId") Long comercioId) {
        return backofficeComercioService.getComercioById(comercioId);
    }

    @RequestMapping(value = "/backofficeComercio/passwordUpdate", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity addPlato(@RequestBody PasswordUpdateDto passwordUpdateDto) {
        return comercioService.updatePassword(passwordUpdateDto);
    }

    @GetMapping(value = "/backofficeComercio/categoriasComida", produces = {"application/json"})
    public ResponseEntity getCategoriasComida(){
        return comidasService.getTipoComidaPlatos();
    }
}
