package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.businesslogic.PedidoEstado;
import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.MessageWithId;
import ar.uba.fi.hoycomobackend.api.dto.PasswordUpdateDto;
import ar.uba.fi.hoycomobackend.api.dto.PedidoOfComercioDto;
import ar.uba.fi.hoycomobackend.api.service.pushnotification.PushNotificationMessage;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.TipoComida;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {

    private static final String PENDIENTE_MENU = "pendiente menu";
    Logger LOGGER = LoggerFactory.getLogger(ComercioService.class);
    private ComercioQuery comercioQuery;
    private PedidoQuery pedidoQuery;
    private OrderDetailService orderDetailService;
    private PushNotificationMessage pushNotificationMessage;

    @Autowired
    public ComercioService(ComercioQuery comercioQuery, PedidoQuery pedidoQuery, OrderDetailService orderDetailService, PushNotificationMessage pushNotificationMessage) {
        this.comercioQuery = comercioQuery;
        this.pedidoQuery = pedidoQuery;
        this.orderDetailService = orderDetailService;
        this.pushNotificationMessage = pushNotificationMessage;
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
            pedidoOfComercioDto.setFecha(pedido.getFecha());
            pedidoOfComercioDto.setMonto(pedido.getTotal());
            pedidoOfComercioDto.setId(pedido.getId());
            pedidoOfComercioDtoList.add(pedidoOfComercioDto);
        });

        return ResponseEntity.ok(pedidoOfComercioDtoList);
    }

    public ResponseEntity changeStateOfPedido(Long pedidoId, String estado) {
        try {
            Optional<Pedido> pedidoOptional = pedidoQuery.getPedidoById(pedidoId);

            if (pedidoOptional.isPresent()) {
                Pedido pedido = pedidoOptional.get();
                Comercio comercio = comercioQuery.getComercioById(pedido.getStoreId()).get();
                pedido.setEstado(estado);
                try {
                    if (PedidoEstado.EN_PREPARACION.equals(estado))
                        pedido.setStartTime(new Date().getTime());
                    if (PedidoEstado.DESPACHADO.equals(estado)) {
                        pedido.setEndTime(new Date().getTime());
                        Long totalTimeTakenMillis = pedido.getEndTime() - pedido.getStartTime();
                        Integer totalTimeTakenMinutes = (int) Math.ceil(totalTimeTakenMillis.doubleValue() / 60000.0);
                        Integer olderTotalTimes = comercio.getTotalPedidos();
                        Integer olderTotalTimeTakenMinutes = comercio.getLeadTime() + pedido.getTimeAccordingToDistance();
                        Integer totalMinutes = (int) Math.ceil((totalTimeTakenMinutes + olderTotalTimes * olderTotalTimeTakenMinutes) / (olderTotalTimes.floatValue() + 1.0));
                        comercio.setLeadTime(totalMinutes);
                        comercio.setTotalPedidos(olderTotalTimes + 1);
                        comercioQuery.saveAndFlush(comercio);
                    }

                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = new Date();
                        pedido.setLastModified(dateFormat.format(date));
                        pedido = pedidoQuery.savePedido(pedido);
                        orderDetailService.update(pedido);
                        String pushMessage = getPushMessageAccordingToEstado(estado);
                        sendMessageToAndroidDevice(pushMessage, pedidoId, comercio.getNombre());

                        return ResponseEntity.ok(pedido);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Error al tratar de guardar el pedido. Error: " + e.getMessage()));
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Error al cambiar estado luego de primer paso. Error: " + e.getMessage()));
                }
            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró el pedido solicitado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Error al cambiar estado de pedido. Error: " + e.getMessage()));
        }
    }

    private String getPushMessageAccordingToEstado(String estado) {
        switch (estado) {
            case "EnPreparacion":
                return "Su pedido se encuentra en preparación.";
            case "Despachado":
                return "Su pedido ya fue despachado.";
            case "Entregado":
                return "Su pedido fue entregado, que lo disfrute.";
            case "Cancelado":
                return "Su pedido ha sido cancelado.";
            default:
                return "Su pedido se encuentra en el siguiente estado: " + estado;
        }

    }

    private void sendMessageToAndroidDevice(String pushMessage, Long pedidoId, String storeName) {
        LOGGER.info("Starting to send message to android device");
        Message message = Message.builder()
                .putData("title", "Hoy Como")
                .putData("detail", storeName + ": " + pushMessage)
                .putData("order-id", pedidoId.toString())
                .setTopic("/topics/allDevices")
                .build();

        String response = pushNotificationMessage.sendMessage(message);
        LOGGER.info("Message push uri: " + response);
    }
}
