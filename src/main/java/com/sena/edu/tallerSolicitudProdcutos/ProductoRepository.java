package com.sena.edu.tallerSolicitudProdcutos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<producto, Long> {
    List<producto> findByCategoria(String categoria);
    List<producto> findByNombreContainingIgnoreCase(String nombre);
    List<producto> findByStockLessThan(Integer limite);
    List<producto> findByPrecioLessThanEqual(Double precio);
}
