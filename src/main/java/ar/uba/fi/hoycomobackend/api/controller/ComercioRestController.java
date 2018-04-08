package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.service.ComercioService;
import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ComercioRestController {

    private ComercioService comercioService;

    @Autowired
    public ComercioRestController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    @GetMapping(value = "/comercios", produces = {"application/json"})
    public List<Comercio> getAllComercios() {
        return comercioService.getAllComercios();
    }

    @PostMapping(value = "/comercios", consumes = {"application/json"})
    public void addComercio(@RequestBody Comercio comercio) {
        comercioService.addComercio(comercio);
    }
}
