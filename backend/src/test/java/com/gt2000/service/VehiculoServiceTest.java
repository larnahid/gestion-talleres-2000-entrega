package com.gt2000.service;

import com.gt2000.dto.VehiculoDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cliente;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.ClienteRepository;
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
 * Tests unitarios para VehiculoService.
 * Verifica CRUD y búsqueda por matrícula (QR).
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@ExtendWith(MockitoExtension.class)
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    private Cliente cliente;
    private Vehiculo vehiculo;
    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("12345678A", "Juan García", "600111222");
        cliente.setIdCliente(1);

        vehiculo = new Vehiculo("1234ABC", "Toyota", "Corolla", cliente);
        vehiculoDTO = new VehiculoDTO("5678DEF", "Seat", "Ibiza");
        vehiculoDTO.setIdCliente(1);
    }

    /**
     * Test 1: Buscar vehículo por matrícula (escaneo QR).
     */
    @Test
    void findByMatricula_conMatriculaExistente_devuelveVehiculo() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        Vehiculo result = vehiculoService.findByMatricula("1234ABC");
        assertNotNull(result);
        assertEquals("Toyota", result.getMarca());
        assertEquals("Corolla", result.getModelo());
    }

    /**
     * Test 2: Buscar vehículo por matrícula inexistente.
     */
    @Test
    void findByMatricula_conMatriculaInexistente_lanzaExcepcion() {
        when(vehiculoRepository.findByMatricula("9999ZZZ")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> vehiculoService.findByMatricula("9999ZZZ"));
    }

    /**
     * Test 3: Crear vehículo con matrícula nueva.
     */
    @Test
    void create_conMatriculaNueva_creaVehiculo() {
        when(vehiculoRepository.existsByMatricula("5678DEF")).thenReturn(false);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehiculo created = vehiculoService.create(vehiculoDTO);
        assertNotNull(created);
        assertEquals("Seat", created.getMarca());
        assertEquals("Ibiza", created.getModelo());
    }

    /**
     * Test 4: Crear vehículo con matrícula duplicada.
     */
    @Test
    void create_conMatriculaDuplicada_lanzaExcepcion() {
        when(vehiculoRepository.existsByMatricula("5678DEF")).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class,
            () -> vehiculoService.create(vehiculoDTO));
    }

    /**
     * Test 5: Obtener todos los vehículos.
     */
    @Test
    void findAll_devuelveTodosLosVehiculos() {
        when(vehiculoRepository.findAllWithCliente()).thenReturn(List.of(vehiculo));
        List<Vehiculo> vehiculos = vehiculoService.findAll();
        assertEquals(1, vehiculos.size());
    }

    /**
     * Test 6: Obtener vehículos de un cliente.
     */
    @Test
    void findByClienteId_devuelveVehiculosDelCliente() {
        when(vehiculoRepository.findByClienteIdCliente(1)).thenReturn(List.of(vehiculo));
        List<Vehiculo> vehiculos = vehiculoService.findByClienteId(1);
        assertEquals(1, vehiculos.size());
        assertEquals("1234ABC", vehiculos.get(0).getMatricula());
    }

    /**
     * Test 7: Buscar vehículos por marca.
     */
    @Test
    void findByMarca_devuelveVehiculosDeMarca() {
        when(vehiculoRepository.findByMarcaContainingIgnoreCase("Toyota")).thenReturn(List.of(vehiculo));
        List<Vehiculo> vehiculos = vehiculoService.findByMarca("Toyota");
        assertEquals(1, vehiculos.size());
    }

    /**
     * Test 8: Eliminar vehículo por matrícula.
     */
    @Test
    void deleteByMatricula_conMatriculaExistente_elimina() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        doNothing().when(vehiculoRepository).deleteById("1234ABC");

        assertDoesNotThrow(() -> vehiculoService.deleteByMatricula("1234ABC"));
        verify(vehiculoRepository).deleteById("1234ABC");
    }

    /**
     * Test 9: Eliminar vehículo inexistente.
     */
    @Test
    void deleteByMatricula_conMatriculaInexistente_lanzaExcepcion() {
        when(vehiculoRepository.findByMatricula("9999ZZZ")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> vehiculoService.deleteByMatricula("9999ZZZ"));
    }

    /**
     * Test 10: Verificar existencia de matrícula.
     */
    @Test
    void existsByMatricula_conMatriculaExistente_devuelveTrue() {
        when(vehiculoRepository.existsByMatricula("1234ABC")).thenReturn(true);
        assertTrue(vehiculoService.existsByMatricula("1234ABC"));
    }
}