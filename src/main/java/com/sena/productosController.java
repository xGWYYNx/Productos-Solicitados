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
import org.springframework.web.bind.annotation.RequestParam;

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


    @PutMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {

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

        if (pedidoEncontrado.getEstado() == estadoPedido.CANCELADO) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El pedido ya está cancelado");
        }

        // Si el pedido estaba confirmado,
        // devolvemos la cantidad al inventario
        if (pedidoEncontrado.getEstado() == estadoPedido.CONFIRMADO) {

            for (producto p : inventario) {

                if (p.getId().equals(pedidoEncontrado.getProductoId())) {

                    p.setStock(
                        p.getStock() + pedidoEncontrado.getCantidad()
                    );

                    break;
                }
            }
        }

        pedidoEncontrado.setEstado(estadoPedido.CANCELADO);

        return ResponseEntity.ok(pedidoEncontrado);
    }




    // Endpoint 4 Despachar pedido confirmado
    @PutMapping("/pedidos/{id}/despachar")
    public ResponseEntity<?> despacharPedido(@PathVariable Long id) {

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

        if (pedidoEncontrado.getEstado() != estadoPedido.CONFIRMADO) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Solo se pueden despachar pedidos CONFIRMADOS");
        }

        pedidoEncontrado.setEstado(estadoPedido.DESPACHADO);

        return ResponseEntity.ok(pedidoEncontrado);
    }



    // Endpoint 5 Get de Pedidos

    // pedidos pendientes

    @GetMapping("/pedidos/pendientes")
    public List<pedido> pedidosPendientes() {

        List<pedido> resultado = new ArrayList<>();

        for (pedido p : pedidos) {

            if (p.getEstado() == estadoPedido.PENDIENTE) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    // Pedidos urgentes
    @GetMapping("/pedidos/urgentes")
    public List<pedido> pedidosUrgentes() {

        List<pedido> resultado = new ArrayList<>();

        for (pedido p : pedidos) {

            if (p.getPrioridad() == prioridad.URGENTE) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    // Pedidos por estado
    @GetMapping("/pedidos/estado")
    public ResponseEntity<?> pedidosPorEstado(@RequestParam estadoPedido estado) {

        List<pedido> resultado = new ArrayList<>();

        for (pedido p : pedidos) {

            if (p.getEstado() == estado) {
                resultado.add(p);
            }
        }

        return ResponseEntity.ok(resultado);
    }

    // Resumen de pedidos

    @GetMapping("/pedidos/resumen")
    public ResponseEntity<?> resumenPedidos() {

        int total = pedidos.size();
        int pendientes = 0;
        int confirmados = 0;
        int despachados = 0;
        int cancelados = 0;
        int urgentes = 0;

        for (pedido p : pedidos) {

            if (p.getEstado() == estadoPedido.PENDIENTE) {
                pendientes++;
            }

            if (p.getEstado() == estadoPedido.CONFIRMADO) {
                confirmados++;
            }

            if (p.getEstado() == estadoPedido.DESPACHADO) {
                despachados++;
            }

            if (p.getEstado() == estadoPedido.CANCELADO) {
                cancelados++;
            }

            if (p.getPrioridad() == prioridad.URGENTE) {
                urgentes++;
            }
        }

        return ResponseEntity.ok(
            "Total de pedidos: " + total +
            "\nPendientes: " + pendientes +
            "\nConfirmados: " + confirmados +
            "\nDespachados: " + despachados +
            "\nCancelados: " + cancelados +
            "\nUrgentes: " + urgentes
        );
    }


}