package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.service.CategoriaComidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ComidaController {

    private CategoriaComidaService categoriaComidaService;

    @Autowired
    public ComidaController(CategoriaComidaService categoriaComidaService) {
        this.categoriaComidaService = categoriaComidaService;
    }

    @GetMapping(value = {"/categoriaComercio"}, produces = {"application/json"})
    public ResponseEntity getCategoriaComida() {
        return categoriaComidaService.getCategoriaComida();
    }
}
