package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderContent;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderDetail;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderStatusHistory;
import ar.uba.fi.hoycomobackend.database.repository.OrderDetailRepository;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderDetailService {

    private OrderDetailRepository orderDetailRepository;
    private PlatoRepository platoRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository, PlatoRepository platoRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.platoRepository = platoRepository;
    }


    public OrderDetail creation(Pedido pedido, String comercioName) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderContent(new HashSet<>());
        orderDetail.setStatusHistory(new HashSet<>());
        orderDetail.setComercioId(pedido.getStoreId());
        orderDetail.setPedidoId(pedido.getId());
        orderDetail.setStoreName(comercioName);
        orderDetail = addOrderStatusHistory(orderDetail, pedido);
        orderDetail = addOrderContent(orderDetail, pedido);
        ;
        orderDetail.setTotal(pedido.getTotal());

        return orderDetailRepository.saveAndFlush(orderDetail);
    }

    private OrderDetail addOrderContent(OrderDetail orderDetail, Pedido pedido) {
        Set<OrderContent> orderContentList = orderDetail.getOrderContent();
        pedido.getOrden().forEach(orden -> {
            OrderContent orderContent = new OrderContent();
            orderContent.setCant(orden.getCantidad());
            Long platoId = orden.getId_plato();
            try {
                String name = platoRepository.findById(platoId).get().getNombre();
                orderContent.setName(name);
            } catch (Exception e) {
                orderContent.setName("nameNotFound");
            }
            orderContent.setSubtotal(orden.getSub_total());
            orderContentList.add(orderContent);
        });
        orderDetail.setOrderContent(orderContentList);

        return orderDetail;
    }

    private OrderDetail addOrderStatusHistory(OrderDetail orderDetail, Pedido pedido) {
        Set<OrderStatusHistory> orderStatusHistoryList = orderDetail.getStatusHistory();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setDate(Date.from(Instant.now()).toString());
        orderStatusHistory.setStatus(pedido.getEstado());
        orderStatusHistoryList.add(orderStatusHistory);

        orderDetail.setStatusHistory(orderStatusHistoryList);

        return orderDetail;
    }

    public void update(Pedido pedido) {
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByPedidoId(pedido.getId());

        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            orderDetail = addOrderStatusHistory(orderDetail, pedido);

            orderDetailRepository.saveAndFlush(orderDetail);
        }
    }
}
