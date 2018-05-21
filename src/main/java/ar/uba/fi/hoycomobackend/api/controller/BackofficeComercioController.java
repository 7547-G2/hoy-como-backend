package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.*;
import ar.uba.fi.hoycomobackend.api.service.BackofficeComercioService;
import ar.uba.fi.hoycomobackend.api.service.ComercioService;
import ar.uba.fi.hoycomobackend.api.service.ComidasService;
import ar.uba.fi.hoycomobackend.api.service.PedidoService;
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
    private PedidoService pedidoService;

    @Autowired
    public BackofficeComercioController(BackofficeComercioService backofficeComercioSessionService, ComercioService comercioService, ComidasService comidasService, PedidoService pedidoService) {
        this.backofficeComercioService = backofficeComercioSessionService;
        this.comercioService = comercioService;
        this.comidasService = comidasService;
        this.pedidoService = pedidoService;
    }

    @PostMapping(value = "/backofficeComercio/session", produces = {"application/json"})
    public ResponseEntity session(@RequestBody BackofficeComercioSessionDto backofficeComercioSessionDto) throws JsonProcessingException {
        return backofficeComercioService.getTokenFromSession(backofficeComercioSessionDto);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/platos", produces = {"application/json"})
    public ResponseEntity getPlatosFromComercio(@PathVariable("comercioId") Long comercioId) throws JsonProcessingException {
        return backofficeComercioService.getPlatosNotDeletedFromComercio(comercioId);
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

    @GetMapping(value = "/backofficeComercio/tipoComidaComercio", produces = {"application/json"})
    public ResponseEntity getTipoComidaDeComercio() {
        return comidasService.getTipoComidaPlatos();
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/tipoComidaComercio", produces = {"application/json"})
    public ResponseEntity getTipoComidaFromGivenComercio(@PathVariable("comercioId") Long comercioId) {
        return comidasService.getTipoComidaComercioById(comercioId);
    }

    @RequestMapping(value = "/backofficeComercio/{comercioId}/tipoComidaComercio", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity setTipoComidaFromGivenComercio(@PathVariable("comercioId") Long comercioId, @RequestBody String tipoComida) {
        return comercioService.setTipoComidaComercioById(comercioId, tipoComida);
    }

    @GetMapping(value = "/backofficeComercio/categoriasComida", produces = {"application/json"})
    public ResponseEntity getCategoriasComidaDeMenu() {
        return comidasService.getCategoriaComidaFromMenu();
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/categoriasComida", produces = {"application/json"})
    public ResponseEntity getCategoriaComidaFromGivenComercioMenu(@PathVariable("comercioId") Long comercioId) {
        return comidasService.getCategoriaComidaFromMenuByComercioId(comercioId);
    }

    @RequestMapping(value = "/backofficeComercio/{comercioId}/categoriasComida", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity setCategoriaComidaFromGivenComercioMenu(@PathVariable("comercioId") Long comercioId, @RequestBody String tipoComida) {
        return comidasService.setCategoriaComidaFromMenuByComercioId(comercioId, tipoComida);
    }

    @GetMapping(value = "/backofficeComercio/{comercioId}/pedidos", produces = {"application/json"})
    public ResponseEntity getPedidosForGivenComercio(@PathVariable("comercioId") Long comercioId) {
        return comercioService.getPedidosForGivenComercio(comercioId);
    }

    @RequestMapping(value = "backofficeComercio/pedidos/{pedidoId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity changeStateOfPedido(@PathVariable("pedidoId") Long pedidoId, @RequestBody String estado) {
        return comercioService.changeStateOfPedido(pedidoId, estado);
    }

    @GetMapping(value = "/backofficeComercio/pedidos/{pedidoId}/platos", produces = {"application/json"})
    public ResponseEntity getPlatosFromItsId(@PathVariable("pedidoId") Long pedidoId) {
        return pedidoService.getPlatoFromPedido(pedidoId);
    }

    @PostMapping(value = "/backofficeComercio/comercio/{comercioId}/opcion")
    public ResponseEntity createOpcion(@PathVariable("comercioId") Long comercioId, @RequestBody OpcionDto opcionDto) {
        return comidasService.createOpcion(comercioId, opcionDto);
    }

    @PutMapping(value = "/backofficeComercio/comercio/opcion/{opcionId}")
    public ResponseEntity updateOpcion(@PathVariable("opcionId") Long opcionId, @RequestBody OpcionDto opcionDto) {
        return comidasService.updateOpcion(opcionId, opcionDto);
    }

    @GetMapping(value = "/backofficeComercio/opcion/{opcionId}")
    public ResponseEntity getOpcionById(@PathVariable("opcionId") Long opcionId) {
        return comidasService.getOpcionById(opcionId);
    }

    @GetMapping(value = "/backofficeComercio/opcion")
    public ResponseEntity getAllOpciones() {
        return comidasService.getAllOpciones();
    }
}
