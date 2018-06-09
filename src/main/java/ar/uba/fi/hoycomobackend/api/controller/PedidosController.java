package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.service.ComercioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PedidosController {

    private ComercioService comercioService;

    @Autowired
    public PedidosController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }


    @GetMapping(value = "/pedidos", produces = {"application/json"})
    public ResponseEntity getAllPedidos() {
        return comercioService.allPedidos();
    }

}
