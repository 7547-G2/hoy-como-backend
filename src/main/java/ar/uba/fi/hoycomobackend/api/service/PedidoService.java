package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.PlatoMobileUserDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoPedidoComercioDto;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderDetail;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
import ar.uba.fi.hoycomobackend.database.repository.OpcionRepository;
import ar.uba.fi.hoycomobackend.database.repository.OrderDetailRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private PedidoQuery pedidoQuery;
    private PlatoRepository platoRepository;
    private OrderDetailRepository orderDetailRepository;
    private OpcionRepository opcionRepository;
    private ComercioQuery comercioQuery;

    @Autowired
    public PedidoService(PedidoQuery pedidoQuery, PlatoRepository platoRepository, OrderDetailRepository orderDetailRepository, OpcionRepository opcionRepository, ComercioQuery comercioQuery) {
        this.pedidoQuery = pedidoQuery;
        this.platoRepository = platoRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.opcionRepository = opcionRepository;
        this.comercioQuery = comercioQuery;
    }

    public ResponseEntity getPlatoFromPedido(Long pedidoId) {
        Optional<Pedido> pedidoOptional = pedidoQuery.getPedidoById(pedidoId);

        if (pedidoOptional.isPresent()) {
            List<PlatoPedidoComercioDto> platoPedidoComercioDtoList = new ArrayList<>();
            Pedido pedido = pedidoOptional.get();
            pedido.getOrden().forEach(orden -> {
                PlatoPedidoComercioDto platoPedidoComercioDto = new PlatoPedidoComercioDto();
                platoPedidoComercioDto.setCantidad(orden.getCantidad());
                platoPedidoComercioDto.setNombre(getNombreFromPlatoId(orden.getId_plato()));
                platoPedidoComercioDto.setObservacion(orden.getObs());
                platoPedidoComercioDto.setOpciones("");
                orden.getOpcionales().forEach(opcionalId -> {
                    try {
                        String opciones = platoPedidoComercioDto.getOpciones() + " " + opcionRepository.findById(opcionalId).get().getNombre();
                        platoPedidoComercioDto.setOpciones(opciones);
                    } catch (Exception e) {
                        platoPedidoComercioDto.setOpciones("Heroku System inestability");
                    }
                });
                platoPedidoComercioDtoList.add(platoPedidoComercioDto);
            });

            return ResponseEntity.ok(platoPedidoComercioDtoList);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró el pedido"));
    }

    private String getNombreFromPlatoId(Long id_plato) {
        try {
            return platoRepository.findById(id_plato).get().getNombre();
        } catch (Exception e) {
            return "nombre placeholder";
        }
    }

    public ResponseEntity getPlatoByIdForMobile(Long platoId) {
        Optional<Plato> platoOptional = platoRepository.findById(platoId);
        if (platoOptional.isPresent()) {
            PlatoMobileUserDto platoMobileUserDto = new PlatoMobileUserDto();
            Plato plato = platoOptional.get();
            platoMobileUserDto.setId_plato(plato.getId());
            platoMobileUserDto.setImagen(plato.getImagen());
            platoMobileUserDto.setNombre(plato.getNombre());
            platoMobileUserDto.setPrecio(plato.getPrecio());
            platoMobileUserDto.setOpcionales(plato.getOpciones());

            return ResponseEntity.ok(platoMobileUserDto);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo encontrar el plato solicitado"));
    }

    public ResponseEntity getDetallePedido() {
        List<OrderDetail> orderDetail = orderDetailRepository.findAll();
        return ResponseEntity.ok(orderDetail);
    }

    public ResponseEntity getDetallePedidoById(Long detallePedidoId) {
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByPedidoId(detallePedidoId);
        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            return ResponseEntity.ok(orderDetail);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se encontró"));
    }

    public ResponseEntity getInfoDashboard(Long comercioId) {
        try {
            List<Pedido> pedidoList = pedidoQuery.getPedidosOfComercio(comercioId);
            Comercio comercio = comercioQuery.getComercioById(comercioId).get();
            InfoDashboard infoDashboard = new InfoDashboard();

            infoDashboard.leadTime = comercio.getLeadTime().toString() + "minutos";
            infoDashboard.rating = comercio.getRating();
            List<Pedido> pedidoListToday = pedidoList.stream().filter(pedido ->  (pedido.getFechaFacturacion().after(DateTime.now().withTimeAtStartOfDay().toDate()))).collect(Collectors.toList());
            infoDashboard.facturadoDia = pedidoListToday.stream().
                    filter(pedido -> ("Entregado".equalsIgnoreCase(pedido.getEstado()) || "Calificado".equalsIgnoreCase(pedido.getEstado()))).
                    mapToDouble(filteredPedidos -> filteredPedidos.getTotal()).sum();
            infoDashboard.ingresados = pedidoListToday.stream().filter(pedido -> "Ingresado".equalsIgnoreCase(pedido.getEstado())).count();
            infoDashboard.enPreparacion = pedidoListToday.stream().filter(pedido -> "EnPreparacion".equalsIgnoreCase(pedido.getEstado())).count();
            infoDashboard.despachados = pedidoListToday.stream().filter(pedido -> "Despachado".equalsIgnoreCase(pedido.getEstado())).count();
            infoDashboard.cancelados = pedidoListToday.stream().filter(pedido -> "Cancelado".equalsIgnoreCase(pedido.getEstado())).count();
            infoDashboard.entregados = pedidoListToday.stream().filter(pedido -> ("Entregado".equalsIgnoreCase(pedido.getEstado()) || "Calificado".equalsIgnoreCase(pedido.getEstado()))).count();
            infoDashboard.facturadoMes = pedidoList.stream().
                    filter(pedido -> pedido.getFechaFacturacion().after(DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay().toDate())&&
                            ("Entregado".equalsIgnoreCase(pedido.getEstado()) || "Calificado".equalsIgnoreCase(pedido.getEstado()))).
                    mapToDouble(filteredPedidos -> filteredPedidos.getTotal()).sum();

            return ResponseEntity.ok(infoDashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage("Problema al buscar info para el dashboard, motivo: " + e.getMessage()));
        }
    }

    private class InfoDashboard {
        public Double facturadoDia;
        public Double facturadoMes;
        public Long ingresados;
        public Long enPreparacion;
        public Long despachados;
        public Long cancelados;
        public Long entregados;
        public String leadTime;
        public Float rating;
    }
}
