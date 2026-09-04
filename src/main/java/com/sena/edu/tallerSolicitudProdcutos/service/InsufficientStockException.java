package com.sena.edu.tallerSolicitudProdcutos.service;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
