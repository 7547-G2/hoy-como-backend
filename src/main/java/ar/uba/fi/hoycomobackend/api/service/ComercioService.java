package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.MessageWithId;
import ar.uba.fi.hoycomobackend.api.dto.PasswordUpdateDto;
import ar.uba.fi.hoycomobackend.api.dto.PedidoOfComercioDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {

    private static final String PENDIENTE_MENU = "pendiente menu";
    private ComercioQuery comercioQuery;
    private PedidoQuery pedidoQuery;

    @Autowired
    public ComercioService(ComercioQuery comercioQuery, PedidoQuery pedidoQuery) {
        this.comercioQuery = comercioQuery;
        this.pedidoQuery = pedidoQuery;
    }

    public ResponseEntity updatePassword(PasswordUpdateDto passwordUpdateDto) {
        String email = passwordUpdateDto.getEmail();
        Optional<Comercio> comercioOptional = comercioQuery.getComercioByEmail(email);
        if (passwordsMatch(comercioOptional, passwordUpdateDto)) {
            Comercio comercio = comercioOptional.get();
            String newPassword = passwordUpdateDto.getNewPassword();
            comercio.setPassword(newPassword);
            comercio.setEstado(PENDIENTE_MENU);
            comercio = comercioQuery.saveAndFlush(comercio);

            return ResponseEntity.ok(new MessageWithId("Contraseña actualizada correctamente.", comercio.getId()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Email no encontrado o Hash no matchea."));
    }

    private boolean passwordsMatch(Optional<Comercio> comercioOptional, PasswordUpdateDto passwordUpdateDto) {
        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            return comercio.getPassword().equals(passwordUpdateDto.getOldPassword());
        }
        return false;
    }

    public ResponseEntity setTipoComidaComercioById(Long comercioId, String tipoComidaString) {
        Optional<Comercio> comercioOptional = comercioQuery.getComercioById(comercioId);
        if (comercioOptional.isPresent()) {
            Comercio comercio = comercioOptional.get();
            TipoComida tipoComida = new TipoComida();
            tipoComida.setTipo(tipoComidaString);
            //TODO REVISAR ESTO tipoComida.setComercio(comercio);
            comercio.setTipoComida(tipoComida);
            comercioQuery.saveAndFlush(comercio);

            return ResponseEntity.ok("Comercio id: " + comercioId + " tipo comida: " + tipoComidaString);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Comercio no encontrado"));
    }

    public ResponseEntity getPedidosForGivenComercio(Long comercioId) {
        List<Pedido> pedidoList = pedidoQuery.getPedidosOfComercio(comercioId);
        List<PedidoOfComercioDto> pedidoOfComercioDtoList = new LinkedList<>();
        pedidoList.forEach(pedido -> {
            PedidoOfComercioDto pedidoOfComercioDto = new PedidoOfComercioDto();
            pedidoOfComercioDto.setEstado(pedido.getEstado());
            pedidoOfComercioDto.setFecha(Date.from(Instant.now()).toString());
            pedidoOfComercioDto.setMonto(pedido.getTotal());
            pedidoOfComercioDtoList.add(pedidoOfComercioDto);
        });

        return ResponseEntity.ok(pedidoOfComercioDtoList);
    }

    public ResponseEntity changeStateOfPedido(Long pedidoId, String estado) {
        Optional<Pedido> pedidoOptional = pedidoQuery.getPedidoById(pedidoId);

        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            pedido.setEstado(estado);
            pedido = pedidoQuery.savePedido(pedido);

            return ResponseEntity.ok(pedido);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró el pedido solicitado"));
    }
}
