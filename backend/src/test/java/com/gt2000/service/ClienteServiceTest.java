package com.gt2000.service;

import com.gt2000.dto.ClienteDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cliente;
import com.gt2000.repository.ClienteRepository;
import com.gt2000.repository.VehiculoRepository;
import com.gt2000.repository.OrdenReparacionRepository;
import com.gt2000.repository.CitaRepository;

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

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private OrdenReparacionRepository ordenRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente1;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente("12345678A", "Juan García", "600111222");
        cliente1.setIdCliente(1);

        clienteDTO = new ClienteDTO("87654321B", "María López", "650333444");
    }

    /**
     * Test 1: Obtener todos los clientes.
     */
    @Test
    void findAll_devuelveListaClientes() {
        when(clienteRepository.findAllByOrderByNombreAsc()).thenReturn(List.of(cliente1));
        List<Cliente> clientes = clienteService.findAll();
        assertEquals(1, clientes.size());
        assertEquals("Juan García", clientes.get(0).getNombre());
    }

    /**
     * Test 2: Buscar cliente por ID existente.
     */
    @Test
    void findById_conIdExistente_devuelveCliente() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Cliente result = clienteService.findById(1);
        assertNotNull(result);
        assertEquals("12345678A", result.getDni());
    }

    /**
     * Test 3: Buscar cliente por ID inexistente lanza excepción.
     */
    @Test
    void findById_conIdInexistente_lanzaExcepcion() {
        when(clienteRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.findById(999));
    }

    /**
     * Test 4: Buscar cliente por DNI.
     */
    @Test
    void findByDni_conDniExistente_devuelveCliente() {
        when(clienteRepository.findByDni("12345678A")).thenReturn(Optional.of(cliente1));
        Cliente result = clienteService.findByDni("12345678A");
        assertNotNull(result);
        assertEquals("Juan García", result.getNombre());
    }

    /**
     * Test 5: Crear cliente con DNI nuevo.
     */
    @Test
    void create_conDniNuevo_creaCliente() {
        when(clienteRepository.existsByDni("87654321B")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente c = inv.getArgument(0);
            c.setIdCliente(2);
            return c;
        });

        Cliente created = clienteService.create(clienteDTO);
        assertNotNull(created);
        assertEquals("María López", created.getNombre());
        assertEquals("87654321B", created.getDni());
    }

    /**
     * Test 6: Crear cliente con DNI duplicado lanza excepción.
     */
    @Test
    void create_conDniDuplicado_lanzaExcepcion() {
        when(clienteRepository.existsByDni("87654321B")).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class, () -> clienteService.create(clienteDTO));
    }

    /**
     * Test 7: Actualizar cliente existente.
     */
    @Test
    void update_conClienteExistente_actualizaCliente() {
        ClienteDTO updateDTO = new ClienteDTO();
        updateDTO.setNombre("Juan García Actualizado");
        updateDTO.setTelefono("600999888");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);

        Cliente updated = clienteService.update(1, updateDTO);
        assertNotNull(updated);
        verify(clienteRepository).save(any(Cliente.class));
    }

    /**
     * Test 8: Eliminar cliente existente.
     */
    @Test
    void deleteById_conIdExistente_eliminaCliente() {
        when(clienteRepository.existsById(1)).thenReturn(true);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        doNothing().when(clienteRepository).deleteById(1);

        assertDoesNotThrow(() -> clienteService.deleteById(1));
        verify(clienteRepository).deleteById(1);
    }

    /**
     * Test 9: Eliminar cliente inexistente lanza excepción.
     */
    @Test
    void deleteById_conIdInexistente_lanzaExcepcion() {
        when(clienteRepository.existsById(999)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteById(999));
    }

    /**
     * Test 10: Contar clientes.
     */
    @Test
    void count_devuelveNumeroClientes() {
        when(clienteRepository.count()).thenReturn(5L);
        long count = clienteService.count();
        assertEquals(5, count);
    }
}