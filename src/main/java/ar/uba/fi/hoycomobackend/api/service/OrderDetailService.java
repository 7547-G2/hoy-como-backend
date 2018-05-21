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
        Double total = calculateTotal(orderDetail);
        orderDetail.setTotal(total);

        orderDetailRepository.saveAndFlush(orderDetail);
    }

    private Double calculateTotal(OrderDetail orderDetail) {
        Double total = orderDetail.getOrderContent().stream().
                filter(orderContent -> orderContent.getSubtotal() != null).mapToDouble(orderContent -> orderContent.getSubtotal()).sum();
        if (total != null)
            return total;
        else
            return 0.0;
    }

    private OrderDetail addOrderContent(OrderDetail orderDetail, Pedido pedido) {
        Set<OrderContent> orderContentList = orderDetail.getOrderContent();
        pedido.getOrden().forEach(orden -> {
            OrderContent orderContent = new OrderContent();
            orderContent.setCant(orden.getCantidad());
            orderContent.setName(orderContent.getName());
            orderContent.setSubtotal(orderContent.getSubtotal());
            orderContent.setOrderDetail(orderDetail);
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
        orderStatusHistory.setOrderDetail(orderDetail);
        orderStatusHistoryList.add(orderStatusHistory);

        orderDetail.setStatusHistory(orderStatusHistoryList);

        return orderDetail;
    }

    public void update(Pedido pedido) {
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByPedidoId(pedido.getId());

        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            orderDetail = addOrderStatusHistory(orderDetail, pedido);
            orderDetail = addOrderContent(orderDetail, pedido);
            Double total = calculateTotal(orderDetail);
            orderDetail.setTotal(total);

            orderDetailRepository.saveAndFlush(orderDetail);
        }
    }
}
