package com.gt2000.controller;

import com.gt2000.dto.ApiResponse;
import com.gt2000.dto.VehiculoDTO;
import com.gt2000.dto.VehiculoUpdateDTO;
import com.gt2000.model.Vehiculo;
import com.gt2000.service.VehiculoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Vehículos.
 * Proporciona endpoints CRUD y búsqueda por matrícula (QR).
 * La matrícula es el identificador natural usado en QR.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    /**
     * Servicio de vehículos.
     */
    private final VehiculoService vehiculoService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param vehiculoService Servicio de vehículos
     */
    @Autowired
    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    /**
     * Obtiene todos los vehículos con información del cliente.
     * Accessible por ADMIN y MECANICO.
     *
     * @return Lista de vehículos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehiculo>>> getAllVehiculos() {
        List<Vehiculo> vehiculos = vehiculoService.findAll();
        return ResponseEntity.ok(ApiResponse.success(vehiculos));
    }

    /**
     * Busca un vehículo por su matrícula.
     * Este es el endpoint principal usado tras escanear un QR.
     *
     * @param matricula Matrícula del vehículo (ej: 1234-ABC)
     * @return Vehículo encontrado con datos del cliente
     *         404 si no existe
     */
    @GetMapping("/{matricula}")
    public ResponseEntity<ApiResponse<Vehiculo>> getVehiculoByMatricula(@PathVariable String matricula) {
        Vehiculo vehiculo = vehiculoService.findByMatricula(matricula);
        return ResponseEntity.ok(ApiResponse.success(vehiculo));
    }

    /**
     * Busca vehículos por marca.
     *
     * @param marca Marca a buscar
     * @return Lista de vehículos de esa marca
     */
    @GetMapping("/buscar/marca")
    public ResponseEntity<ApiResponse<List<Vehiculo>>> buscarPorMarca(@RequestParam String marca) {
        List<Vehiculo> vehiculos = vehiculoService.findByMarca(marca);
        return ResponseEntity.ok(ApiResponse.success(vehiculos));
    }

    /**
     * Obtiene todos los vehículos de un cliente.
     *
     * @param idCliente ID del cliente
     * @return Lista de vehículos del cliente
     */
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponse<List<Vehiculo>>> getVehiculosByCliente(@PathVariable Integer idCliente) {
        List<Vehiculo> vehiculos = vehiculoService.findByClienteId(idCliente);
        return ResponseEntity.ok(ApiResponse.success(vehiculos));
    }

    /**
     * Crea un nuevo vehículo.
     * Solo Administradores pueden crear vehículos.
     *
     * @param vehiculoDTO Datos del vehículo
     * @return Vehículo creado
     *         201 Created
     *         409 Conflict si ya existe
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Vehiculo>> createVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = vehiculoService.create(vehiculoDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vehiculo creado correctamente", vehiculo));
    }

    /**
     * Actualiza un vehículo existente.
     * Solo Administradores pueden actualizar.
     *
     * @param matricula          Matrícula del vehículo a actualizar
     * @param vehiculoUpdateDTO Nuevos datos
     * @return Vehículo actualizado
     *         404 si no existe
     */
    @PutMapping("/{matricula}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Vehiculo>> updateVehiculo(
            @PathVariable String matricula,
            @Valid @RequestBody VehiculoUpdateDTO vehiculoUpdateDTO) {
        Vehiculo vehiculo = vehiculoService.update(matricula, vehiculoUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success("Vehiculo actualizado correctamente", vehiculo));
    }

    /**
     * Elimina un vehículo por su matrícula.
     * Solo Administradores pueden eliminar.
     *
     * @param matricula Matrícula del vehículo a eliminar
     * @return 200 OK
     *         404 si no existe
     */
    @DeleteMapping("/{matricula}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> deleteVehiculo(@PathVariable String matricula) {
        try {
            vehiculoService.deleteByMatricula(matricula);
            return ResponseEntity.ok(ApiResponse.success("Vehiculo eliminado correctamente", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Obtiene el número total de vehículos.
     *
     * @return Contador de vehículos
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countVehiculos() {
        long count = vehiculoService.count();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}