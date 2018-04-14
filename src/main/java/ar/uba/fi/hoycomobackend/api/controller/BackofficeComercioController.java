package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.BackofficeComercioSessionDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeComercioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String session(@RequestBody BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        return backofficeComercioService.getTokenFromSession(backofficeComercioSessionDto);
    }

    @PostMapping(value = "/backofficeComercio/{comercioId}/platos")
    public String addPlato(@PathVariable("comercioId") Long comercioId, @RequestBody PlatoDto platoDto) throws JsonProcessingException {
        return backofficeComercioService.addPlatoToComercio(comercioId, platoDto);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/platos", produces = {"application/json"})
    public String getPlatosFromComercio(@PathVariable("comercioId") Long comercioId) throws JsonProcessingException {
        return backofficeComercioService.getPlatosFromComercio(comercioId);
    }
}