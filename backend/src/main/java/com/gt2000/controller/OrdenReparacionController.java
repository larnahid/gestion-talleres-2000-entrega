package com.gt2000.controller;

import com.gt2000.dto.ApiResponse;
import com.gt2000.dto.OrdenReparacionDTO;
import com.gt2000.model.OrdenReparacion;
import com.gt2000.model.Usuario;
import com.gt2000.repository.UsuarioRepository;
import com.gt2000.service.OrdenReparacionService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Órdenes de Reparación.
 * Proporciona endpoints CRUD y gestión de estados.
 * Acceso: ADMIN puede todo, MECANICO puede crear/actualizar sus órdenes.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@RestController
@RequestMapping("/ordenes")
public class OrdenReparacionController {

    private final OrdenReparacionService ordenService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public OrdenReparacionController(OrdenReparacionService ordenService, UsuarioRepository usuarioRepository) {
        this.ordenService = ordenService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todas las órdenes con vehículo y usuario cargados.
     * Accessible por ADMIN y MECANICO.
     *
     * @return Lista de órdenes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrdenReparacion>>> getAllOrdenes() {
        List<OrdenReparacion> ordenes = ordenService.findAll();
        return ResponseEntity.ok(ApiResponse.success(ordenes));
    }

    /**
     * Obtiene una orden por su ID.
     *
     * @param id ID de la orden
     * @return Orden encontrada
     *         404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdenReparacion>> getOrdenById(@PathVariable Integer id) {
        OrdenReparacion orden = ordenService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(orden));
    }

    /**
     * Obtiene todas las órdenes de un vehículo (historial).
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de órdenes del vehículo
     */
    @GetMapping("/vehiculo/{matricula}")
    public ResponseEntity<ApiResponse<List<OrdenReparacion>>> getOrdenesByVehiculo(@PathVariable String matricula) {
        List<OrdenReparacion> ordenes = ordenService.findByVehiculoMatricula(matricula);
        return ResponseEntity.ok(ApiResponse.success(ordenes));
    }

    /**
     * Obtiene órdenes por estado.
     *
     * @param estado Estado a filtrar (PENDIENTE, EN_PROCESO, TERMINADO)
     * @return Lista de órdenes con ese estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<OrdenReparacion>>> getOrdenesByEstado(@PathVariable String estado) {
        List<OrdenReparacion> ordenes = ordenService.findByEstado(OrdenReparacion.Estado.valueOf(estado));
        return ResponseEntity.ok(ApiResponse.success(ordenes));
    }

    /**
     * Obtiene todas las órdenes pendientes.
     *
     * @return Lista de órdenes pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<ApiResponse<List<OrdenReparacion>>> getOrdenesPendientes() {
        List<OrdenReparacion> ordenes = ordenService.findPendientes();
        return ResponseEntity.ok(ApiResponse.success(ordenes));
    }

    /**
     * Obtiene todas las órdenes en proceso.
     *
     * @return Lista de órdenes en proceso
     */
    @GetMapping("/en-proceso")
    public ResponseEntity<ApiResponse<List<OrdenReparacion>>> getOrdenesEnProceso() {
        List<OrdenReparacion> ordenes = ordenService.findEnProceso();
        return ResponseEntity.ok(ApiResponse.success(ordenes));
    }

    /**
     * Crea una nueva orden de reparación.
     * Accessible por ADMIN y MECANICO.
     * El mecánico que crea la orden se asigna automáticamente.
     *
     * @param ordenDTO        Datos de la orden
     * @param authentication  Credenciales del usuario actual
     * @return Orden creada
     *         201 Created
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrdenReparacion>> createOrden(
            @Valid @RequestBody OrdenReparacionDTO ordenDTO,
            Authentication authentication) {
        Integer idUsuario = extractUserIdFromAuthentication(authentication);
        OrdenReparacion orden = ordenService.create(ordenDTO, idUsuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Orden de reparacion creada correctamente", orden));
    }

    /**
     * Actualiza una orden de reparación existente.
     * Accessible por ADMIN y MECANICO (solo sus órdenes).
     *
     * @param id        ID de la orden
     * @param ordenDTO  Nuevos datos
     * @return Orden actualizada
     *         404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrdenReparacion>> updateOrden(
            @PathVariable Integer id,
            @Valid @RequestBody OrdenReparacionDTO ordenDTO) {
        OrdenReparacion orden = ordenService.update(id, ordenDTO);
        return ResponseEntity.ok(ApiResponse.success("Orden actualizada correctamente", orden));
    }

    /**
     * Actualiza solo el estado de una orden.
     * Método optimizado para cambios de estado rápidos desde la app.
     *
     * @param id           ID de la orden
     * @param nuevoEstado  Nuevo estado (texto)
     * @return Orden actualizada
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<OrdenReparacion>> updateEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        OrdenReparacion orden = ordenService.updateEstado(id, nuevoEstado);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado correctamente", orden));
    }

    /**
     * Elimina una orden de reparación.
     * Solo Administradores pueden eliminar.
     *
     * @param id ID de la orden
     * @return 200 OK
     *         404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrden(@PathVariable Integer id) {
        ordenService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Orden eliminada correctamente", null));
    }

    /**
     * Obtiene contadores de órdenes para el dashboard.
     *
     * @return Objeto con pendientes y en proceso
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Object>> getStats() {
        long pendientes = ordenService.countPendientes();
        long enProceso = ordenService.countEnProceso();

        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("pendientes", pendientes);
        stats.put("enProceso", enProceso);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * Extrae el ID de usuario real desde Spring Security Authentication.
     * Busca el usuario en la BD por username.
     */
    private Integer extractUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return usuario.getIdUsuario();
    }
}