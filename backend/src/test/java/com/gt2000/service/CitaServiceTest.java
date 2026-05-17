package com.gt2000.service;

import com.gt2000.dto.CitaDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cita;
import com.gt2000.model.Cliente;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.CitaRepository;
import com.gt2000.repository.VehiculoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CitaService.
 * Verifica CRUD y búsqueda por fecha.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private CitaService citaService;

    private Vehiculo vehiculo;
    private Cita cita;
    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente("12345678A", "Juan García", "600111222");
        cliente.setIdCliente(1);

        vehiculo = new Vehiculo("1234ABC", "Toyota", "Corolla", cliente);

        LocalDateTime fechaHora = LocalDateTime.of(2026, 5, 15, 10, 0);
        cita = new Cita(fechaHora, "ITV Anual", vehiculo);
        cita.setIdCita(1);

        citaDTO = new CitaDTO();
        citaDTO.setFechaHora("2026-05-15T10:00");
        citaDTO.setMotivo("Revisión semestral");
        citaDTO.setMatricula("1234ABC");
    }

    /**
     * Test 1: Obtener todas las citas.
     */
    @Test
    void findAll_devuelveTodasLasCitas() {
        when(citaRepository.findAllWithVehiculo()).thenReturn(List.of(cita));
        List<Cita> citas = citaService.findAll();
        assertEquals(1, citas.size());
    }

    /**
     * Test 2: Buscar cita por ID.
     */
    @Test
    void findById_conIdExistente_devuelveCita() {
        when(citaRepository.findById(1)).thenReturn(Optional.of(cita));
        Cita result = citaService.findById(1);
        assertNotNull(result);
        assertEquals("ITV Anual", result.getMotivo());
    }

    /**
     * Test 3: Buscar cita por ID inexistente.
     */
    @Test
    void findById_conIdInexistente_lanzaExcepcion() {
        when(citaRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> citaService.findById(999));
    }

    /**
     * Test 4: Obtener citas de un día.
     */
    @Test
    void findByFecha_conFechaValida_devuelveCitas() {
        LocalDate fecha = LocalDate.of(2026, 5, 15);
        when(citaRepository.findByFechaBetween(any(), any())).thenReturn(List.of(cita));

        List<Cita> citas = citaService.findByFecha(fecha);
        assertEquals(1, citas.size());
    }

    /**
     * Test 5: Crear cita con datos válidos.
     */
    @Test
    void create_conDatosValidos_creaCita() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        when(citaRepository.existsByFechaHoraAndVehiculoMatricula(any(), eq("1234ABC"))).thenReturn(false);
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            c.setIdCita(2);
            return c;
        });

        Cita created = citaService.create(citaDTO);
        assertNotNull(created);
        assertEquals("Revisión semestral", created.getMotivo());
    }

    /**
     * Test 6: Crear cita con hora duplicada.
     */
    @Test
    void create_conHoraDuplicada_lanzaExcepcion() {
        when(vehiculoRepository.findByMatricula("1234ABC")).thenReturn(Optional.of(vehiculo));
        when(citaRepository.existsByFechaHoraAndVehiculoMatricula(any(), eq("1234ABC"))).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> citaService.create(citaDTO));
    }

    /**
     * Test 7: Crear cita con vehículo inexistente.
     */
    @Test
    void create_conVehiculoInexistente_lanzaExcepcion() {
        when(vehiculoRepository.findByMatricula("9999ZZZ")).thenReturn(Optional.empty());

        citaDTO.setMatricula("9999ZZZ");
        assertThrows(ResourceNotFoundException.class, () -> citaService.create(citaDTO));
    }

    /**
     * Test 8: Obtener citas futuras.
     */
    @Test
    void findFuturas_devuelveSoloCitasFuturas() {
        when(citaRepository.findCitasFuturas()).thenReturn(List.of(cita));
        List<Cita> futuras = citaService.findFuturas();
        assertEquals(1, futuras.size());
    }

    /**
     * Test 9: Eliminar cita existente.
     */
    @Test
    void deleteById_conIdExistente_elimina() {
        when(citaRepository.existsById(1)).thenReturn(true);
        doNothing().when(citaRepository).deleteById(1);

        assertDoesNotThrow(() -> citaService.deleteById(1));
        verify(citaRepository).deleteById(1);
    }

    /**
     * Test 10: Eliminar cita inexistente.
     */
    @Test
    void deleteById_conIdInexistente_lanzaExcepcion() {
        when(citaRepository.existsById(999)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> citaService.deleteById(999));
    }
}