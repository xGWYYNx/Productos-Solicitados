package com.sena.model;


public class pedido {

    private Long id;
    private String cliente;
    private Long productoId;
    private Integer cantidad;
    private prioridad prioridad;
    private estadoPedido estado;

    public pedido(Long id, String cliente, Long productoId, Integer cantidad,
                  prioridad prioridad, estadoPedido estado) {

        this.id = id;
        this.cliente = cliente;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public Long getProductoId() {
        return productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public prioridad getPrioridad() {
        return prioridad;
    }

    public estadoPedido getEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrioridad(prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public void setEstado(estadoPedido estado) {
        this.estado = estado;
    }
}