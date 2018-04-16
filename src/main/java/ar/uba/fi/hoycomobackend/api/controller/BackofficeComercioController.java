package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeComercioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class BackofficeComercioController {

    private BackofficeComercioService backofficeComercioService;

    @Autowired
    public BackofficeComercioController(BackofficeComercioService backofficeComercioSessionService) {
        this.backofficeComercioService = backofficeComercioSessionService;
    }

    @PostMapping(value = "/backofficeComercio/session", produces = {"application/json"})
    public ResponseEntity session(@RequestBody BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        return backofficeComercioService.getTokenFromSession(backofficeComercioSessionDto);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/platos", produces = {"application/json"})
    public ResponseEntity getPlatosFromComercio(@PathVariable("comercioId") Long comercioId) throws JsonProcessingException {
        return backofficeComercioService.getPlatosFromComercio(comercioId);
    }

    @PostMapping(value = "/backofficeComercio/{comercioId}/platos")
    public ResponseEntity addPlato(@PathVariable("comercioId") Long comercioId, @RequestBody PlatoDto platoDto) throws JsonProcessingException {
        return backofficeComercioService.addPlatoToComercio(comercioId, platoDto);
    }

    @PutMapping(value = "/backofficeComercio/{comercioId}/platos/{platoId}")
    public ResponseEntity updatePlato(@PathVariable("comercioId") Long comercioId, @PathVariable("platoId") Long platoId, @RequestBody PlatoDto platoDto) throws JsonProcessingException {
        return backofficeComercioService.updatePlatoFromComercio(comercioId, platoId, platoDto);
    }
}
