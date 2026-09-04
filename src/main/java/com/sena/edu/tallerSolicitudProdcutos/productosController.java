package com.sena.edu.tallerSolicitudProdcutos;

import com.sena.edu.tallerSolicitudProdcutos.service.InsufficientStockException;
import com.sena.edu.tallerSolicitudProdcutos.service.PedidoService;
import com.sena.edu.tallerSolicitudProdcutos.service.ProductoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class productosController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public productosController(ProductoService productoService, PedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    // ==================== PRODUCTOS ====================

    @GetMapping("/productos")
    public List<producto> listarProductos() {
        return productoService.listar();
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id) {

        producto p = productoService.buscar(id);

        if (p == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.ok(p);
    }

    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody producto p) {

        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El nombre es obligatorio");
        }

        if (p.getPrecio() == null || p.getPrecio() < 0) {
            return ResponseEntity.badRequest()
                    .body("El precio no puede ser negativo");
        }

        if (p.getStock() == null || p.getStock() < 0) {
            return ResponseEntity.badRequest()
                    .body("El stock no puede ser negativo");
        }

        if (p.getCategoria() == null || p.getCategoria().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("La categoria es obligatoria");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoService.crear(p));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody producto p) {

        producto actualizado = productoService.actualizar(id, p);

        if (actualizado == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {

        if (!productoService.eliminar(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @GetMapping("/productos/buscar")
    public List<producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.porNombre(nombre);
    }

    @GetMapping("/productos/categoria")
    public List<producto> buscarPorCategoria(@RequestParam String nombre) {
        return productoService.porCategoria(nombre);
    }

    @GetMapping("/productos/stock")
    public List<producto> stockMenor(@RequestParam Integer limite) {
        return productoService.stockMenor(limite);
    }

    @GetMapping("/productos/precio-maximo")
    public List<producto> precioMaximo(@RequestParam Double precio) {
        return productoService.precioMaximo(precio);
    }

    // ==================== PEDIDOS ====================

    @GetMapping("/pedidos")
    public List<pedido> listarPedidos() {
        return pedidoService.listar();
    }

    /*
     * IMPORTANTE:
     * Este endpoint específico debe estar antes de /pedidos/{id}
     */
    @GetMapping("/pedidos/urgentes-pendientes")
    public List<pedido> pedidosUrgentesPendientes() {
        return pedidoService.urgentesPendientes();
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id) {

        pedido p = pedidoService.buscar(id);

        if (p == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El pedido no existe");
        }

        return ResponseEntity.ok(p);
    }

    @PostMapping("/pedidos")
    public ResponseEntity<?> crearPedido(@RequestBody pedido nuevoPedido) {

        if (nuevoPedido.getCliente() == null ||
                nuevoPedido.getCliente().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("El cliente es obligatorio");
        }

        if (nuevoPedido.getCantidad() == null ||
                nuevoPedido.getCantidad() <= 0) {

            return ResponseEntity.badRequest()
                    .body("La cantidad debe ser mayor que cero");
        }

        if (nuevoPedido.getPrioridad() == null) {
            return ResponseEntity.badRequest()
                    .body("La prioridad es obligatoria");
        }

        if (nuevoPedido.getProductoId() == null) {
            return ResponseEntity.badRequest()
                    .body("El productoId es obligatorio");
        }

        if (productoService.buscar(nuevoPedido.getProductoId()) == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El producto no existe");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.crear(nuevoPedido));
    }

    @PutMapping("/pedidos/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {

        try {

            pedido p = pedidoService.confirmar(id);

            if (p == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("El pedido no existe");
            }

            return ResponseEntity.ok(p);

        } catch (InsufficientStockException |
                 IllegalStateException |
                 IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {

        try {

            pedido p = pedidoService.cancelar(id);

            if (p == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("El pedido no existe");
            }

            return ResponseEntity.ok(p);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/pedidos/{id}/despachar")
    public ResponseEntity<?> despacharPedido(@PathVariable Long id) {

        try {

            pedido p = pedidoService.despachar(id);

            if (p == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("El pedido no existe");
            }

            return ResponseEntity.ok(p);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/pedidos/pendientes")
    public List<pedido> pedidosPendientes() {
        return pedidoService.pendientes();
    }

    @GetMapping("/pedidos/urgentes")
    public List<pedido> pedidosUrgentes() {
        return pedidoService.urgentes();
    }

    @GetMapping("/pedidos/estado")
    public List<pedido> pedidosPorEstado(
            @RequestParam estadoPedido estado) {

        return pedidoService.porEstado(estado);
    }

    @GetMapping("/pedidos/prioridad")
    public List<pedido> pedidosPorPrioridad(
            @RequestParam prioridad prioridad) {

        return pedidoService.porPrioridad(prioridad);
    }

    @GetMapping("/pedidos/cliente")
    public List<pedido> pedidosPorCliente(
            @RequestParam String nombre) {

        return pedidoService.porCliente(nombre);
    }

    @GetMapping("/pedidos/resumen")
    public Map<String, Object> resumenPedidos() {

        Map<String, Object> resumen = new LinkedHashMap<>();

        resumen.put("total", pedidoService.listar().size());
        resumen.put(
                "pendientes",
                pedidoService.contarPorEstado(estadoPedido.PENDIENTE)
        );
        resumen.put(
                "confirmados",
                pedidoService.contarPorEstado(estadoPedido.CONFIRMADO)
        );
        resumen.put(
                "despachados",
                pedidoService.contarPorEstado(estadoPedido.DESPACHADO)
        );
        resumen.put(
                "cancelados",
                pedidoService.contarPorEstado(estadoPedido.CANCELADO)
        );
        resumen.put(
                "urgentes",
                pedidoService.contarUrgentes()
        );

        return resumen;
    }

    @GetMapping("/pedidos/siguiente")
    public ResponseEntity<?> siguientePedido() {

        pedido siguiente = pedidoService.siguiente();

        if (siguiente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No hay pedidos pendientes");
        }

        return ResponseEntity.ok(siguiente);
    }

    @GetMapping("/pedidos/en-riesgo")
    public List<pedido> pedidosEnRiesgo() {
        return pedidoService.enRiesgo();
    }
}