package ar.uba.fi.hoycomobackend.api.controller;

import ar.uba.fi.hoycomobackend.api.dto.AddressDto;
import ar.uba.fi.hoycomobackend.api.dto.ComentarioDto;
import ar.uba.fi.hoycomobackend.api.dto.MobileUserAddDto;
import ar.uba.fi.hoycomobackend.api.dto.PostPedidoDto;
import ar.uba.fi.hoycomobackend.api.service.ComidasService;
import ar.uba.fi.hoycomobackend.api.service.MobileUserService;
import ar.uba.fi.hoycomobackend.api.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class MobileUserRestController {

    private MobileUserService mobileUserService;
    private ComidasService comidasService;
    private PedidoService pedidoService;

    @Autowired
    public MobileUserRestController(MobileUserService mobileUserService, ComidasService comidasService, PedidoService pedidoService) {
        this.mobileUserService = mobileUserService;
        this.comidasService = comidasService;
        this.pedidoService = pedidoService;
    }

    @GetMapping(value = "/mobileUser")
    public ResponseEntity getAllMobileUsers() {
        return mobileUserService.getMobileUserList();
    }

    @GetMapping(value = "/mobileUser/{id}")
    public ResponseEntity getMobileUserById(@PathVariable("id") Long id) {
        return mobileUserService.getMobileUserById(id);
    }

    @GetMapping(value = "/mobileUser/{id}/authorized", produces = {"application/json"})
    public ResponseEntity getMobileUserAuthorized(@PathVariable("id") Long id) throws JsonProcessingException {
        return mobileUserService.getMobileUserAuthorizedById(id);
    }

    @PostMapping(value = "/mobileUser")
    public ResponseEntity addMobileUser(@RequestBody MobileUserAddDto mobileUser) {
        return mobileUserService.addMobileUser(mobileUser);
    }

    @GetMapping(value = "/mobileUser/{id}/favorites")
    public ResponseEntity getMobileUserFavorites(@PathVariable("id") Long id) throws JsonProcessingException {
        return mobileUserService.getMobileUserFavorites(id);
    }

    @PostMapping(value = "/mobileUser/{mobileUserFacebookId}/favorite/{comercioId}")
    public ResponseEntity addFavoriteComercioToMobileUser(@PathVariable("mobileUserFacebookId") Long mobileUserFacebookId, @PathVariable("comercioId") Long comercioId) {
        return mobileUserService.addFavoriteComercioToMobileUser(mobileUserFacebookId, comercioId);
    }

    @PutMapping(value = "/mobileUser/{mobileUserFacebookId}/address")
    public ResponseEntity addAddressToMobileUser(@PathVariable("mobileUserFacebookId") Long mobileUserFacebookId, @RequestBody AddressDto addressDto) {
        return mobileUserService.addAddressToMobileUser(mobileUserFacebookId, addressDto);
    }

    @GetMapping(value = "/mobileUser/comercios")
    public ResponseEntity getComercioMobileUserDtoSet(@RequestParam(value = "search", required = false) String search) {
        return mobileUserService.getComercioMobileUserDtoSet(search);
    }

    @PutMapping(value = "/mobileUser/{mobileUserFacebookId}/state")
    public ResponseEntity changeStateToMobileUser(@PathVariable("mobileUserFacebookId") Long mobileUserFacebookId, @RequestBody Integer stateCode) {
        return mobileUserService.changeStateToMobileUser(mobileUserFacebookId, stateCode);
    }

    @GetMapping(value = "/mobileUser/tipoComida")
    public ResponseEntity getTipoComidaDtoSet() {
        return comidasService.getTipoComidaComercio();
    }

    @GetMapping(value = "/mobileUser/menu/{comercioId}")
    public ResponseEntity getMenuFromComercio(@PathVariable("comercioId") Long comercioId) {
        return mobileUserService.getMenuFromComercio(comercioId);
    }

    @PostMapping(value = "/mobileUser/pedido")
    public ResponseEntity postPedido(@RequestBody PostPedidoDto postPedidoDto) {
        return mobileUserService.postPedido(postPedidoDto);
    }

    @PostMapping(value = "/mobileUser/pedido/{pedidoId}/cancel")
    public ResponseEntity postPedido(@PathVariable("pedidoId") Long pedidoId) {
        return mobileUserService.cancelPedido(pedidoId);
    }

    @PostMapping(value = "/mobileUser/{mobileUserFacebookId}/pedido/{pedidoId}/comentario")
    public ResponseEntity postComment(@PathVariable("mobileUserFacebookId") Long mobileUserFacebookId, @PathVariable("pedidoId") Long pedidoId, @RequestBody ComentarioDto comentarioDto) {
        return mobileUserService.postCommentInPedido(mobileUserFacebookId, pedidoId, comentarioDto);
    }

    @GetMapping(value = "/mobileUser/{facebookId}/pedido")
    public ResponseEntity getPedidosByUserId(@PathVariable("facebookId") Long facebookId) {
        return mobileUserService.getPedidosOfUser(facebookId);
    }

    @GetMapping(value = "/mobileUser/plato/{platoId}")
    public ResponseEntity getPlato(@PathVariable("platoId") Long platoId) {
        return pedidoService.getPlatoByIdForMobile(platoId);
    }

    @GetMapping(value = "/mobileUser/detallePedido")
    public ResponseEntity getDetallePedido() {
        return pedidoService.getDetallePedido();
    }

    @GetMapping(value = "/mobileUser/detallePedido/{detallePedidoId}")
    public ResponseEntity getDetallePedidoById(@PathVariable("detallePedidoId") Long detallePedidoId) {
        return pedidoService.getDetallePedidoById(detallePedidoId);
    }
}
