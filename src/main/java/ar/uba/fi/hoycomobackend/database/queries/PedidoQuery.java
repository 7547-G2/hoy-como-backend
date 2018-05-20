package ar.uba.fi.hoycomobackend.database.queries;

import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoQuery {

    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoQuery(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }


    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.saveAndFlush(pedido);
    }

    public Pedido getPedido() {
        return pedidoRepository.findAll().get(0);
    }
}