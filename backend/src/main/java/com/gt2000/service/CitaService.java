package com.gt2000.service;

import com.gt2000.dto.CitaDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cita;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.CitaRepository;
import com.gt2000.repository.VehiculoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Citas.
 * Proporciona métodos CRUD, búsqueda por fecha y gestión de citas.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class CitaService {

    /**
     * Repositorio para acceso a datos de Cita.
     */
    private final CitaRepository citaRepository;

    /**
     * Repositorio de vehículos (para validar relación).
     */
    private final VehiculoRepository vehiculoRepository;

    /**
     * Formateador para fechas.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param citaRepository     Repositorio de citas
     * @param vehiculoRepository  Repositorio de vehículos
     */
    @Autowired
    public CitaService(CitaRepository citaRepository, VehiculoRepository vehiculoRepository) {
        this.citaRepository = citaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Obtiene todas las citas con vehículo cargado.
     *
     * @return Lista de citas
     */
    public List<Cita> findAll() {
        return citaRepository.findAllWithVehiculo();
    }

    /**
     * Busca una cita por su ID.
     *
     * @param id ID de la cita
     * @return Cita encontrada
     * @throws ResourceNotFoundException si no existe
     */
    public Cita findById(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }

    /**
     * Obtiene todas las citas de un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de citas del vehículo
     */
    public List<Cita> findByVehiculoMatricula(String matricula) {
        return citaRepository.findByVehiculoMatricula(matricula);
    }

    /**
     * Obtiene las citas de un día específico.
     *
     * @param fecha Fecha del día ( LocalDate)
     * @return Lista de citas de ese día
     */
    public List<Cita> findByFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return citaRepository.findByFechaBetween(inicio, fin);
    }

    /**
     * Obtiene las citas entre dos fechas.
     *
     * @param fechaInicio Fecha de inicio
     * @param fechaFin    Fecha de fin
     * @return Lista de citas en el rango
     */
    public List<Cita> findBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return citaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    /**
     * Obtiene citas futuras (a partir de ahora).
     *
     * @return Lista de citas futuras
     */
    public List<Cita> findFuturas() {
        return citaRepository.findCitasFuturas();
    }

    /**
     * Crea una nueva cita.
     *
     * @param citaDTO Datos de la cita
     * @return Cita creada
     * @throws ResourceNotFoundException si el vehículo no existe
     * @throws ResourceAlreadyExistsException si ya existe una cita a esa hora
     */
    public Cita create(CitaDTO citaDTO) {
        Vehiculo vehiculo = vehiculoRepository.findByMatricula(citaDTO.getMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con matricula: " + citaDTO.getMatricula()));

        LocalDateTime fechaHora = LocalDateTime.parse(citaDTO.getFechaHora(), DATE_FORMATTER);

        if (citaRepository.existsByFechaHoraAndVehiculoMatricula(fechaHora, citaDTO.getMatricula())) {
            throw new ResourceAlreadyExistsException("Ya existe una cita a esa hora para este vehículo");
        }

        Cita cita = new Cita(fechaHora, citaDTO.getMotivo(), vehiculo);
        return citaRepository.save(cita);
    }

    /**
     * Actualiza una cita existente.
     *
     * @param id      ID de la cita a actualizar
     * @param citaDTO Nuevos datos
     * @return Cita actualizada
     */
    public Cita update(Integer id, CitaDTO citaDTO) {
        Cita cita = findById(id);

        if (citaDTO.getFechaHora() != null) {
            LocalDateTime fechaHora = LocalDateTime.parse(citaDTO.getFechaHora(), DATE_FORMATTER);
            cita.setFechaHora(fechaHora);
        }

        if (citaDTO.getMotivo() != null) {
            cita.setMotivo(citaDTO.getMotivo());
        }

        if (citaDTO.getMatricula() != null && !citaDTO.getMatricula().equals(cita.getMatriculaVehiculo())) {
            Vehiculo vehiculo = vehiculoRepository.findByMatricula(citaDTO.getMatricula())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado"));
            cita.setVehiculo(vehiculo);
        }

        return citaRepository.save(cita);
    }

    /**
     * Elimina una cita por su ID.
     *
     * @param id ID de la cita a eliminar
     */
    public void deleteById(Integer id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    /**
     * Cuenta cuántas citas tiene un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Número de citas
     */
    public long countByVehiculo(String matricula) {
        return citaRepository.countByVehiculoMatricula(matricula);
    }

    /**
     * Convierte una Cita a DTO.
     *
     * @param cita Entidad cita
     * @return CitaDTO con los datos
     */
    public CitaDTO toDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setFechaHora(cita.getFechaHora().format(DATE_FORMATTER));
        dto.setMotivo(cita.getMotivo());
        dto.setMatricula(cita.getMatriculaVehiculo());

        if (cita.getVehiculo() != null) {
            dto.setMarcaVehiculo(cita.getVehiculo().getMarca());
            dto.setModeloVehiculo(cita.getVehiculo().getModelo());
        }

        return dto;
    }
}