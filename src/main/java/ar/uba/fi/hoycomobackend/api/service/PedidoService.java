package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.PlatoMobileUserDto;
import ar.uba.fi.hoycomobackend.api.dto.PlatoPedidoComercioDto;
import ar.uba.fi.hoycomobackend.database.entity.Pedido;
import ar.uba.fi.hoycomobackend.database.entity.Plato;
import ar.uba.fi.hoycomobackend.database.queries.PedidoQuery;
import ar.uba.fi.hoycomobackend.database.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private PedidoQuery pedidoQuery;
    private PlatoRepository platoRepository;

    @Autowired
    public PedidoService(PedidoQuery pedidoQuery, PlatoRepository platoRepository) {
        this.pedidoQuery = pedidoQuery;
        this.platoRepository = platoRepository;
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
                platoPedidoComercioDto.setOpciones("Opciones no implementado aun");
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
            platoMobileUserDto.setOpcionales(new LinkedList<>());

            return ResponseEntity.ok(platoMobileUserDto);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se pudo encontrar el plato solicitado"));
    }
}
