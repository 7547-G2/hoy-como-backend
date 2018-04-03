package ar.uba.fi.hoycomobackend.controller;

import ar.uba.fi.hoycomobackend.entity.comercio.Comercio;
import ar.uba.fi.hoycomobackend.service.ComercioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
