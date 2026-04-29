package com.example.msordenes.service;

import com.example.msordenes.dto.OrdenCompraRequestDTO;
import com.example.msordenes.dto.OrdenCompraResponseDTO;
import com.example.msordenes.exception.ResourceNotFoundException;
import com.example.msordenes.model.OrdenCompra;
import com.example.msordenes.repository.OrdenCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenCompraServiceTest {

    @Mock
    private OrdenCompraRepository repository;

    @InjectMocks
    private OrdenCompraService service;

    private OrdenCompraRequestDTO requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new OrdenCompraRequestDTO();
        requestValido.setClienteNombre("Maria Soto");
        requestValido.setMascotaNombre("Rocky");
        requestValido.setProducto("Alimento premium 10kg");
        requestValido.setCantidad(2);
        requestValido.setPrecioUnitario(15990.0);
    }

    @Test
    @DisplayName("crearOrden debe calcular total = cantidad * precioUnitario y dejar estado PENDIENTE")
    void crearOrden_calculaTotalYAsignaEstadoPendiente() {
        when(repository.save(any(OrdenCompra.class))).thenAnswer(inv -> {
            OrdenCompra entrante = inv.getArgument(0);
            entrante.setId(10L);
            return entrante;
        });

        OrdenCompraResponseDTO resultado = service.crearOrden(requestValido);

        ArgumentCaptor<OrdenCompra> captor = ArgumentCaptor.forClass(OrdenCompra.class);
        verify(repository).save(captor.capture());
        OrdenCompra persistida = captor.getValue();
        assertThat(persistida.getEstado()).isEqualTo("PENDIENTE");
        assertThat(persistida.getTotal()).isEqualTo(2 * 15990.0);
        assertThat(persistida.getFechaCreacion()).isNotBlank();
        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getTotal()).isEqualTo(31980.0);
    }

    @Test
    @DisplayName("obtenerEstado debe lanzar ResourceNotFoundException cuando la orden no existe")
    void obtenerEstado_cuandoNoExiste_lanzaResourceNotFound() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerEstado(404L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("404");
    }
}
