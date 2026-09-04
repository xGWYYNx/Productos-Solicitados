package com.sena.edu.tallerSolicitudProdcutos;

import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private estadoPedido estado;

    public pedido() {
    }

    public pedido(Long id, String cliente, Long productoId, Integer cantidad,
                  prioridad prioridad, estadoPedido estado) {
        this.id = id;
        this.cliente = cliente;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(prioridad prioridad) { this.prioridad = prioridad; }

    public estadoPedido getEstado() { return estado; }
    public void setEstado(estadoPedido estado) { this.estado = estado; }
}
