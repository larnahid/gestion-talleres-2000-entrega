package com.gt2000.controller;

import com.gt2000.dto.ApiResponse;
import com.gt2000.dto.CitaDTO;
import com.gt2000.model.Cita;
import com.gt2000.service.CitaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de Citas.
 * Proporciona endpoints CRUD y búsqueda por fecha.
 * Acceso: solo Administradores pueden gestionar citas.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@RestController
@RequestMapping("/citas")
public class CitaController {

    /**
     * Servicio de citas.
     */
    private final CitaService citaService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param citaService Servicio de citas
     */
    @Autowired
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    /**
     * Obtiene todas las citas con vehículo cargado.
     * Solo Administradores.
     *
     * @return Lista de citas
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<Cita>>> getAllCitas() {
        List<Cita> citas = citaService.findAll();
        return ResponseEntity.ok(ApiResponse.success(citas));
    }

    /**
     * Obtiene una cita por su ID.
     *
     * @param id ID de la cita
     * @return Cita encontrada
     *         404 si no existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Cita>> getCitaById(@PathVariable Integer id) {
        Cita cita = citaService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(cita));
    }

    /**
     * Obtiene todas las citas de un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de citas del vehículo
     */
    @GetMapping("/vehiculo/{matricula}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<Cita>>> getCitasByVehiculo(@PathVariable String matricula) {
        List<Cita> citas = citaService.findByVehiculoMatricula(matricula);
        return ResponseEntity.ok(ApiResponse.success(citas));
    }

    /**
     * Obtiene las citas de un día específico.
     * Endpoint principal para el calendario.
     *
     * @param fecha Fecha del día (formato: yyyy-MM-dd)
     * @return Lista de citas de ese día
     */
    @GetMapping("/dia/{fecha}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<Cita>>> getCitasByDia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.findByFecha(fecha);
        return ResponseEntity.ok(ApiResponse.success(citas));
    }

    /**
     * Obtiene las citas futuras (a partir de hoy).
     *
     * @return Lista de citas futuras
     */
    @GetMapping("/futuras")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<Cita>>> getCitasFuturas() {
        List<Cita> citas = citaService.findFuturas();
        return ResponseEntity.ok(ApiResponse.success(citas));
    }

    /**
     * Crea una nueva cita.
     * Solo Administradores.
     *
     * @param citaDTO Datos de la cita
     * @return Cita creada
     *         201 Created
     *         409 Conflict si ya existe una cita a esa hora
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Cita>> createCita(@Valid @RequestBody CitaDTO citaDTO) {
        Cita cita = citaService.create(citaDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cita creada correctamente", cita));
    }

    /**
     * Actualiza una cita existente.
     * Solo Administradores.
     *
     * @param id      ID de la cita
     * @param citaDTO Nuevos datos
     * @return Cita actualizada
     *         404 si no existe
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Cita>> updateCita(
            @PathVariable Integer id,
            @Valid @RequestBody CitaDTO citaDTO) {
        Cita cita = citaService.update(id, citaDTO);
        return ResponseEntity.ok(ApiResponse.success("Cita actualizada correctamente", cita));
    }

    /**
     * Elimina una cita por su ID.
     * Solo Administradores.
     *
     * @param id ID de la cita
     * @return 200 OK
     *         404 si no existe
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> deleteCita(@PathVariable Integer id) {
        citaService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Cita eliminada correctamente", null));
    }
}