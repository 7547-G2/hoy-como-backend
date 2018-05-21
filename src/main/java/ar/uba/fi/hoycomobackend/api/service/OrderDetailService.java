package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderContent;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderDetail;
import ar.uba.fi.hoycomobackend.database.entity.orderhistory.OrderStatusHistory;
import ar.uba.fi.hoycomobackend.database.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class OrderDetailService {

    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }


    public void creation(Pedido pedido, String comercioName) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderContent(new HashSet<>());
        orderDetail.setStatusHistory(new HashSet<>());
        orderDetail.setComercioId(pedido.getStoreId());
        orderDetail.setPedidoId(pedido.getId());
        orderDetail.setStoreName(comercioName);
        orderDetail = addOrderStatusHistory(orderDetail, pedido);
        orderDetail = addOrderContent(orderDetail, pedido);

        orderDetailRepository.saveAndFlush(orderDetail);
    }

    private OrderDetail addOrderContent(OrderDetail orderDetail, Pedido pedido) {
        Set<OrderContent> orderContentList = orderDetail.getOrderContent();
        pedido.getOrden().forEach(orden -> {
            OrderContent orderContent = new OrderContent();
            orderContent.setCant(orden.getCantidad());
            orderContent.setName(orderContent.getName());
            orderContent.setSubtotal(orderContent.getSubtotal());
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
}
