package com.example.msordenes.controller;

import com.example.msordenes.dto.OrdenCompraRequestDTO;
import com.example.msordenes.dto.OrdenCompraResponseDTO;
import com.example.msordenes.service.OrdenCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<OrdenCompraResponseDTO>>> listarTodas() {
        List<EntityModel<OrdenCompraResponseDTO>> ordenes = service.obtenerTodas().stream()
                .map(this::toModel)
                .toList();
        CollectionModel<EntityModel<OrdenCompraResponseDTO>> coleccion = CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenCompraController.class).listarTodas()).withSelfRel());
        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrdenCompraResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(service.obtenerPorId(id)));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Map<String, String>>> obtenerEstado(@PathVariable Long id) {
        Map<String, String> estado = Map.of("estado", service.obtenerEstado(id));
        EntityModel<Map<String, String>> modelo = EntityModel.of(estado,
                linkTo(methodOn(OrdenCompraController.class).obtenerEstado(id)).withSelfRel(),
                linkTo(methodOn(OrdenCompraController.class).obtenerPorId(id)).withRel("orden"),
                linkTo(methodOn(OrdenCompraController.class).listarTodas()).withRel("ordenes"));
        return ResponseEntity.ok(modelo);
    }

    @PostMapping
    public ResponseEntity<EntityModel<OrdenCompraResponseDTO>> crearOrden(
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        OrdenCompraResponseDTO creada = service.crearOrden(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrdenCompraResponseDTO>> actualizarOrden(
            @PathVariable Long id,
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        return ResponseEntity.ok(toModel(service.actualizarOrden(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        service.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<OrdenCompraResponseDTO> toModel(OrdenCompraResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(OrdenCompraController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(OrdenCompraController.class).obtenerEstado(dto.getId())).withRel("estado"),
                linkTo(methodOn(OrdenCompraController.class).listarTodas()).withRel("ordenes"),
                linkTo(methodOn(OrdenCompraController.class).eliminarOrden(dto.getId())).withRel("eliminar"));
    }
}
