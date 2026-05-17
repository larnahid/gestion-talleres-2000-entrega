package com.gt2000.service;

import com.gt2000.dto.OrdenReparacionDTO;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cliente;
import com.gt2000.model.OrdenReparacion;
import com.gt2000.model.Rol;
import com.gt2000.model.Usuario;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.OrdenReparacionRepository;
import com.gt2000.repository.UsuarioRepository;
import com.gt2000.repository.VehiculoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para OrdenReparacionService.
 * Verifica creación, actualización y cambios de estado.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@ExtendWith(MockitoExtension.class)
class OrdenReparacionServiceTest {

    @Mock
    private OrdenReparacionRepository ordenRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OrdenReparacionService ordenService;

    private Vehiculo vehiculo;
    private Usuario usuario;
    private OrdenReparacion orden;
    private OrdenReparacionDTO ordenDTO;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente("12345678A", "Juan García", "600111222");
        cliente.setIdCliente(1);

        vehiculo = new Vehiculo("1234ABC", "Toyota", "Corolla", cliente);

        usuario = new Usuario("carlos_mecanico", "password", new Rol("MECANICO"));
        usuario.setIdUsuario(2);

        orden = new OrdenReparacion("Revisión de aceite", vehiculo, usuario);
        orden.setIdOrden(1);

        ordenDTO = new OrdenReparacionDTO();
        ordenDTO.setMatricula("1234ABC");
        ordenDTO.setDescripcion("Frenos ruidosos");
        ordenDTO.setEstado("Pendiente");
    }

    /**
     * Test 1: Obtener todas las órdenes.
     */
    @Test
    void findAll_devuelveTodasLasOrdenes() {
        when(ordenRepository.findAllWithVehiculoAndUsuario()).thenReturn(List.of(orden));
        List<OrdenReparacion> ordenes = ordenService.findAll();
        assertEquals(1, ordenes.size());
    }

    /**
     * Test 2: Buscar orden por ID.
     */
    @Test
    void findById_conIdExistente_devuelveOrden() {
        when(ordenRepository.findById(1)).thenReturn(Optional.of(orden));
        OrdenReparacion result = ordenService.findById(1);
        assertNotNull(result);
        assertEquals("Revisión de aceite", result.getDescripcion());
    }

    /**
     * Test 3: Buscar orden por ID inexistente.
     */
    @Test
    void findById_conIdInexistente_lanzaExcepcion() {
        when(ordenRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ordenService.findById(999));
    }

    /**
     * Test 4: Crear orden de reparación.
     */
    @Test
    void create_conDatosValidos_creaOrden() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(ordenRepository.save(any(OrdenReparacion.class))).thenAnswer(inv -> {
            OrdenReparacion o = inv.getArgument(0);
            o.setIdOrden(2);
            return o;
        });

        OrdenReparacion created = ordenService.create(ordenDTO, 2);
        assertNotNull(created);
        assertEquals("Frenos ruidosos", created.getDescripcion());
    }

    /**
     * Test 5: Crear orden con vehículo inexistente.
     */
    @Test
    void create_conVehiculoInexistente_lanzaExcepcion() {
        when(vehiculoRepository.findByMatricula("9999ZZZ")).thenReturn(Optional.empty());

        ordenDTO.setMatricula("9999ZZZ");
        assertThrows(ResourceNotFoundException.class,
            () -> ordenService.create(ordenDTO, 2));
    }

    /**
     * Test 6: Actualizar estado de orden.
     */
    @Test
    void updateEstado_conEstadoValido_actualizaEstado() {
        when(ordenRepository.findById(1)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(any(OrdenReparacion.class))).thenReturn(orden);

        OrdenReparacion updated = ordenService.updateEstado(1, "En Proceso");
        assertNotNull(updated);
        verify(ordenRepository).save(any(OrdenReparacion.class));
    }

    /**
     * Test 7: Obtener órdenes pendientes.
     */
    @Test
    void findPendientes_devuelveOrdenesPendientes() {
        when(ordenRepository.findOrdenesPendientes()).thenReturn(List.of(orden));
        List<OrdenReparacion> pendientes = ordenService.findPendientes();
        assertEquals(1, pendientes.size());
    }

    /**
     * Test 8: Obtener órdenes en proceso.
     */
    @Test
    void findEnProceso_devuelveOrdenesEnProceso() {
        when(ordenRepository.findOrdenesEnProceso()).thenReturn(List.of());
        List<OrdenReparacion> enProceso = ordenService.findEnProceso();
        assertTrue(enProceso.isEmpty());
    }

    /**
     * Test 9: Eliminar orden existente.
     */
    @Test
    void deleteById_conIdExistente_eliminaOrden() {
        when(ordenRepository.existsById(1)).thenReturn(true);
        doNothing().when(ordenRepository).deleteById(1);

        assertDoesNotThrow(() -> ordenService.deleteById(1));
        verify(ordenRepository).deleteById(1);
    }

    /**
     * Test 10: Eliminar orden inexistente.
     */
    @Test
    void deleteById_conIdInexistente_lanzaExcepcion() {
        when(ordenRepository.existsById(999)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> ordenService.deleteById(999));
    }

    /**
     * Test de integración: Crear orden completa y verificar persistencia.
     */
    @Test
    void integracion_crearOrdenCompletaYBuscar_ordenePersistida() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(ordenRepository.save(any(OrdenReparacion.class))).thenAnswer(inv -> {
            OrdenReparacion o = inv.getArgument(0);
            o.setIdOrden(99);
            return o;
        });

        OrdenReparacion created = ordenService.create(ordenDTO, 2);
        assertNotNull(created.getIdOrden());

        when(ordenRepository.findById(99)).thenReturn(Optional.of(created));
        OrdenReparacion found = ordenService.findById(99);
        assertEquals(created.getIdOrden(), found.getIdOrden());
        assertEquals(created.getDescripcion(), found.getDescripcion());
    }
}