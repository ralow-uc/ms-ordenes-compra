package com.example.msordenes.dto;

import lombok.Data;

@Data
public class OrdenCompraResponseDTO {

    private Long id;
    private String clienteNombre;
    private String mascotaNombre;
    private String producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private String estado;
    private String fechaCreacion;
}
