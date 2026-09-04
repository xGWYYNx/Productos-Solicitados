package com.sena.edu.tallerSolicitudProdcutos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<pedido, Long> {

    List<pedido> findByEstado(estadoPedido estado);

    List<pedido> findByPrioridad(prioridad prioridad);

    List<pedido> findByClienteContainingIgnoreCase(String cliente);

    List<pedido> findByEstadoAndPrioridad(
        estadoPedido estado,
        prioridad prioridad
    );
}