package com.sena.edu.tallerSolicitudProdcutos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sena.edu.tallerSolicitudProdcutos.model.pedido;
import com.sena.edu.tallerSolicitudProdcutos.model.prioridad;
import com.sena.edu.tallerSolicitudProdcutos.model.estadoPedido;
import com.sena.edu.tallerSolicitudProdcutos.model.producto;

@RestController
public class productosController {

    private final List<producto> inventario = new ArrayList<>(
        List.of(
            new producto(1L, "Mazorca", 7500.0, 100),
            new producto(2L, "Yuca", 5500.0, 40),
            new producto(3L, "Papa Criolla", 12000.0, 500)
        )
    );

    private final List<pedido> pedidos = new ArrayList<>();
    private Long siguienteIdPedido = 1L;

    @GetMapping("/productos")
    public List<producto> listarProductos() {
        return inventario;
    }


    @PostMapping("/pedidos")
    public ResponseEntity<?> crearPedido(@RequestBody pedido nuevoPedido) {

        if (nuevoPedido.getCliente() == null ||
                nuevoPedido.getCliente().trim().isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El cliente es obligatorio");
        }

        if (nuevoPedido.getCantidad() == null ||
                nuevoPedido.getCantidad() <= 0) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La cantidad debe ser mayor que cero");
        }

        if (nuevoPedido.getPrioridad() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La prioridad es obligatoria");
        }

        boolean productoExiste = false;

        for (producto p : inventario) {

            if (p.getId().equals(nuevoPedido.getProductoId())) {

                productoExiste = true;
                break;
            }
        }

        if (!productoExiste) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        nuevoPedido.setId(siguienteIdPedido++);
        nuevoPedido.setEstado(estadoPedido.PENDIENTE);

        pedidos.add(nuevoPedido);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoPedido);
    }

    @PutMapping("/pedidos/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {

        pedido pedidoEncontrado = null;

        for (pedido p : pedidos) {

            if (p.getId().equals(id)) {
                pedidoEncontrado = p;
                break;
            }
        }

        if (pedidoEncontrado == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El pedido no existe");
        }

        producto productoEncontrado = null;

        for (producto p : inventario) {

            if (p.getId().equals(pedidoEncontrado.getProductoId())) {
                productoEncontrado = p;
                break;
            }
        }

        if (productoEncontrado == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        if (productoEncontrado.getStock() < pedidoEncontrado.getCantidad()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No hay stock suficiente");
        }

        productoEncontrado.setStock(
                productoEncontrado.getStock() - pedidoEncontrado.getCantidad()
        );

        pedidoEncontrado.setEstado(estadoPedido.CONFIRMADO);

        return ResponseEntity.ok(pedidoEncontrado);
    }

}