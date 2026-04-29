package com.example.msordenes.controller;

import com.example.msordenes.dto.OrdenCompraResponseDTO;
import com.example.msordenes.service.OrdenCompraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdenCompraController.class)
class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdenCompraService service;

    @Test
    @DisplayName("GET /ordenes/{id} debe responder 200 con enlaces HATEOAS self, estado, ordenes y eliminar")
    void obtenerPorId_devuelveEnlacesHateoasEsperados() throws Exception {
        OrdenCompraResponseDTO dto = new OrdenCompraResponseDTO();
        dto.setId(5L);
        dto.setClienteNombre("Maria Soto");
        dto.setMascotaNombre("Rocky");
        dto.setProducto("Alimento premium 10kg");
        dto.setCantidad(2);
        dto.setPrecioUnitario(15990.0);
        dto.setTotal(31980.0);
        dto.setEstado("PENDIENTE");
        dto.setFechaCreacion("2026-04-28");
        when(service.obtenerPorId(5L)).thenReturn(dto);

        mockMvc.perform(get("/ordenes/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.total").value(31980.0))
                .andExpect(jsonPath("$._links.self.href",
                        org.hamcrest.Matchers.containsString("/ordenes/5")))
                .andExpect(jsonPath("$._links.estado.href",
                        org.hamcrest.Matchers.containsString("/ordenes/5/estado")))
                .andExpect(jsonPath("$._links.ordenes.href",
                        org.hamcrest.Matchers.containsString("/ordenes")))
                .andExpect(jsonPath("$._links.eliminar.href",
                        org.hamcrest.Matchers.containsString("/ordenes/5")));
    }

    @Test
    @DisplayName("GET /ordenes debe retornar la coleccion HATEOAS con _embedded y enlace self")
    void listarTodas_devuelveCollectionModelConEmbedded() throws Exception {
        OrdenCompraResponseDTO dto = new OrdenCompraResponseDTO();
        dto.setId(11L);
        dto.setClienteNombre("Pedro");
        dto.setMascotaNombre("Toby");
        dto.setProducto("Juguete");
        dto.setCantidad(1);
        dto.setPrecioUnitario(4990.0);
        dto.setTotal(4990.0);
        dto.setEstado("PENDIENTE");
        when(service.obtenerTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/ordenes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ordenCompraResponseDTOList[0].id").value(11))
                .andExpect(jsonPath("$._embedded.ordenCompraResponseDTOList[0]._links.self.href",
                        org.hamcrest.Matchers.containsString("/ordenes/11")))
                .andExpect(jsonPath("$._links.self.href",
                        org.hamcrest.Matchers.containsString("/ordenes")));
    }
}
