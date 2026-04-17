package com.example.msordenes.service;

import com.example.msordenes.dto.OrdenCompraRequestDTO;
import com.example.msordenes.dto.OrdenCompraResponseDTO;
import com.example.msordenes.exception.ResourceNotFoundException;
import com.example.msordenes.model.OrdenCompra;
import com.example.msordenes.repository.OrdenCompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenCompraService {

    private final OrdenCompraRepository repository;

    public List<OrdenCompraResponseDTO> obtenerTodas() {
        log.info("Obteniendo todas las ordenes de compra");
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public OrdenCompraResponseDTO obtenerPorId(Long id) {
        log.info("Buscando orden de compra con ID: {}", id);
        OrdenCompra orden = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de compra no encontrada con ID: " + id));
        return toResponseDTO(orden);
    }

    public String obtenerEstado(Long id) {
        log.info("Consultando estado de orden con ID: {}", id);
        OrdenCompra orden = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de compra no encontrada con ID: " + id));
        return orden.getEstado();
    }

    public OrdenCompraResponseDTO crearOrden(OrdenCompraRequestDTO dto) {
        log.info("Creando nueva orden para cliente: {}", dto.getClienteNombre());
        OrdenCompra orden = new OrdenCompra();
        orden.setClienteNombre(dto.getClienteNombre());
        orden.setMascotaNombre(dto.getMascotaNombre());
        orden.setProducto(dto.getProducto());
        orden.setCantidad(dto.getCantidad());
        orden.setPrecioUnitario(dto.getPrecioUnitario());
        orden.setTotal(dto.getCantidad() * dto.getPrecioUnitario());
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDate.now().toString());
        OrdenCompra guardada = repository.save(orden);
        log.debug("Orden guardada con ID: {}", guardada.getId());
        return toResponseDTO(guardada);
    }

    public OrdenCompraResponseDTO actualizarOrden(Long id, OrdenCompraRequestDTO dto) {
        log.info("Actualizando orden de compra con ID: {}", id);
        OrdenCompra orden = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de compra no encontrada con ID: " + id));
        orden.setClienteNombre(dto.getClienteNombre());
        orden.setMascotaNombre(dto.getMascotaNombre());
        orden.setProducto(dto.getProducto());
        orden.setCantidad(dto.getCantidad());
        orden.setPrecioUnitario(dto.getPrecioUnitario());
        orden.setTotal(dto.getCantidad() * dto.getPrecioUnitario());
        return toResponseDTO(repository.save(orden));
    }

    public void eliminarOrden(Long id) {
        log.info("Eliminando orden de compra con ID: {}", id);
        OrdenCompra orden = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de compra no encontrada con ID: " + id));
        repository.delete(orden);
    }

    private OrdenCompraResponseDTO toResponseDTO(OrdenCompra orden) {
        OrdenCompraResponseDTO dto = new OrdenCompraResponseDTO();
        dto.setId(orden.getId());
        dto.setClienteNombre(orden.getClienteNombre());
        dto.setMascotaNombre(orden.getMascotaNombre());
        dto.setProducto(orden.getProducto());
        dto.setCantidad(orden.getCantidad());
        dto.setPrecioUnitario(orden.getPrecioUnitario());
        dto.setTotal(orden.getTotal());
        dto.setEstado(orden.getEstado());
        dto.setFechaCreacion(orden.getFechaCreacion());
        return dto;
    }
}
