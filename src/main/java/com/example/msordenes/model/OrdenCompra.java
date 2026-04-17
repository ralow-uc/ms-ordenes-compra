package com.example.msordenes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ORDENES_COMPRA", schema = "ADMIN")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CLIENTE_NOMBRE", nullable = false, length = 100)
    private String clienteNombre;

    @Column(name = "MASCOTA_NOMBRE", nullable = false, length = 100)
    private String mascotaNombre;

    @Column(name = "PRODUCTO", nullable = false, length = 255)
    private String producto;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private Double precioUnitario;

    @Column(name = "TOTAL")
    private Double total;

    @Column(name = "ESTADO", length = 30)
    private String estado;

    @Column(name = "FECHA_CREACION", length = 20)
    private String fechaCreacion;
}
