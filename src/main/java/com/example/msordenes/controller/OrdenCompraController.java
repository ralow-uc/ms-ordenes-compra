package com.example.msordenes.controller;

import com.example.msordenes.dto.OrdenCompraRequestDTO;
import com.example.msordenes.dto.OrdenCompraResponseDTO;
import com.example.msordenes.service.OrdenCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService service;

    @GetMapping
    public ResponseEntity<List<OrdenCompraResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<Map<String, String>> obtenerEstado(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("estado", service.obtenerEstado(id)));
    }

    @PostMapping
    public ResponseEntity<OrdenCompraResponseDTO> crearOrden(
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearOrden(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> actualizarOrden(
            @PathVariable Long id,
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarOrden(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        service.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
