package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.ComercioHoyComoDto;
import ar.uba.fi.hoycomobackend.api.service.BackofficeHoyComoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BackofficeHoyComoRestController {

    private BackofficeHoyComoService backofficeHoyComoService;

    @Autowired
    public BackofficeHoyComoRestController(BackofficeHoyComoService backofficeHoyComoService) {
        this.backofficeHoyComoService = backofficeHoyComoService;
    }

    @GetMapping(value = {"/comercios"}, produces = {"application/json"})
    public List<ComercioHoyComoDto> getComercios(@RequestParam(value = "search", required = false) String search) {
        return backofficeHoyComoService.getComercios(search);
    }

    @PostMapping(value = "/comercios", consumes = {"application/json"})
    public ResponseEntity addComercio(@RequestBody ComercioHoyComoDto comercioHoyComoDto) {
        return backofficeHoyComoService.addComercio(comercioHoyComoDto);
    }

    @PutMapping(value = "/comercios/{comercioId}", consumes = {"application/json"})
    public ResponseEntity updateComercio(@PathVariable("comercioId") Long comercioId, @RequestBody ComercioHoyComoDto comercioHoyComoDto) {
        return backofficeHoyComoService.updateComercio(comercioId, comercioHoyComoDto);
    }
}
