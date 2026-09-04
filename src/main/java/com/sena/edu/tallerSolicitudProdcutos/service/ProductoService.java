package com.sena.edu.tallerSolicitudProdcutos.service;

import com.sena.edu.tallerSolicitudProdcutos.ProductoRepository;
import com.sena.edu.tallerSolicitudProdcutos.producto;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<producto> listar() {
        return productoRepository.findAll();
    }

    public producto buscar(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public producto crear(producto p) {
        p.setId(null);
        return productoRepository.save(p);
    }

    public producto actualizar(Long id, producto datos) {
        producto actual = buscar(id);
        if (actual == null) return null;

        actual.setNombre(datos.getNombre());
        actual.setPrecio(datos.getPrecio());
        actual.setStock(datos.getStock());
        actual.setCategoria(datos.getCategoria());
        return productoRepository.save(actual);
    }

    public boolean eliminar(Long id) {
        if (!productoRepository.existsById(id)) return false;
        productoRepository.deleteById(id);
        return true;
    }

    public List<producto> porCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<producto> porNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<producto> stockMenor(Integer limite) {
        return productoRepository.findByStockLessThan(limite);
    }

    public List<producto> precioMaximo(Double precio) {
        return productoRepository.findByPrecioLessThanEqual(precio);
    }

    public void guardar(producto p) {
        productoRepository.save(p);
    }
}
