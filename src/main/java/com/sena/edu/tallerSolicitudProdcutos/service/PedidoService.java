package com.sena.edu.tallerSolicitudProdcutos.service;

import com.sena.edu.tallerSolicitudProdcutos.PedidoRepository;
import com.sena.edu.tallerSolicitudProdcutos.estadoPedido;
import com.sena.edu.tallerSolicitudProdcutos.pedido;
import com.sena.edu.tallerSolicitudProdcutos.prioridad;
import com.sena.edu.tallerSolicitudProdcutos.producto;

import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    public List<pedido> listar() {
        return pedidoRepository.findAll();
    }

    public pedido buscar(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public pedido crear(pedido nuevo) {
        nuevo.setId(null);
        nuevo.setEstado(estadoPedido.PENDIENTE);
        return pedidoRepository.save(nuevo);
    }

    public pedido confirmar(Long id) {
        pedido p = buscar(id);
        if (p == null) return null;

        if (p.getEstado() != estadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar pedidos PENDIENTES");
        }

        producto prod = productoService.buscar(p.getProductoId());
        if (prod == null) {
            throw new IllegalArgumentException("El producto no existe");
        }

        if (prod.getStock() < p.getCantidad()) {
            throw new InsufficientStockException(
                "No hay stock suficiente. El pedido permanece PENDIENTE"
            );
        }

        prod.setStock(prod.getStock() - p.getCantidad());
        productoService.guardar(prod);

        p.setEstado(estadoPedido.CONFIRMADO);
        return pedidoRepository.save(p);
    }

    public pedido cancelar(Long id) {
        pedido p = buscar(id);
        if (p == null) return null;

        if (p.getEstado() == estadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado");
        }

        if (p.getEstado() == estadoPedido.CONFIRMADO) {
            producto prod = productoService.buscar(p.getProductoId());
            if (prod != null) {
                prod.setStock(prod.getStock() + p.getCantidad());
                productoService.guardar(prod);
            }
        }

        p.setEstado(estadoPedido.CANCELADO);
        return pedidoRepository.save(p);
    }

    public pedido despachar(Long id) {
        pedido p = buscar(id);
        if (p == null) return null;

        if (p.getEstado() != estadoPedido.CONFIRMADO) {
            throw new IllegalStateException("Solo se pueden despachar pedidos CONFIRMADOS");
        }

        p.setEstado(estadoPedido.DESPACHADO);
        return pedidoRepository.save(p);
    }

    public List<pedido> porEstado(estadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<pedido> porPrioridad(prioridad prioridad) {
        return pedidoRepository.findByPrioridad(prioridad);
    }

    public List<pedido> porCliente(String cliente) {
        return pedidoRepository.findByClienteContainingIgnoreCase(cliente);
    }

    public List<pedido> pendientes() {
        return pedidoRepository.findByEstado(estadoPedido.PENDIENTE);
    }

    public List<pedido> urgentes() {
        return pedidoRepository.findByPrioridad(prioridad.URGENTE);
    }

    public pedido siguiente() {
        return pendientes().stream()
            .max(Comparator
                .comparingInt((pedido p) -> p.getPrioridad().ordinal())
                .thenComparing(pedido::getId, Comparator.reverseOrder()))
            .orElse(null);
    }

    public List<pedido> enRiesgo() {
        return pendientes().stream()
            .filter(p -> {
                producto prod = productoService.buscar(p.getProductoId());
                return prod != null && p.getCantidad() > prod.getStock();
            })
            .toList();
    }

    public long contarPorEstado(estadoPedido estado) {
        return pedidoRepository.findByEstado(estado).size();
    }

    public long contarUrgentes() {
        return pedidoRepository.findByPrioridad(prioridad.URGENTE).size();
    }


    public List<pedido> urgentesPendientes() {
    return pedidoRepository.findByEstadoAndPrioridad(
        estadoPedido.PENDIENTE,
        prioridad.URGENTE
    );
}
}
