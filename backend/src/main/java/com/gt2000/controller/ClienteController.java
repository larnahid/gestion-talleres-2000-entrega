package com.gt2000.controller;

import com.gt2000.dto.ApiResponse;
import com.gt2000.dto.ClienteDTO;
import com.gt2000.model.Cliente;
import com.gt2000.service.ClienteService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Clientes.
 * Proporciona endpoints CRUD para clientes del taller.
 * Acceso restringido: solo Administradores pueden crear/eliminar.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    /**
     * Servicio de clientes.
     */
    private final ClienteService clienteService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param clienteService Servicio de clientes
     */
    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes.
     * Accessible por ADMIN y MECANICO.
     *
     * @return Lista de clientes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> getAllClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente
     * @return Cliente encontrado
     *         404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> getClienteById(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(cliente));
    }

    /**
     * Busca clientes por nombre (búsqueda parcial).
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de clientes que coinciden
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Cliente>>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.findByNombreContaining(nombre);
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    /**
     * Crea un nuevo cliente.
     * Solo Administradores pueden crear clientes.
     *
     * @param clienteDTO Datos del cliente
     * @return Cliente creado
     *         201 Created
     *         409 Conflict si ya existe
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Cliente>> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.create(clienteDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creado correctamente", cliente));
    }

    /**
     * Actualiza un cliente existente.
     * Solo Administradores pueden actualizar.
     *
     * @param id         ID del cliente a actualizar
     * @param clienteDTO Nuevos datos
     * @return Cliente actualizado
     *         404 si no existe
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Cliente>> updateCliente(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizado correctamente", cliente));
    }

    /**
     * Elimina un cliente por su ID.
     * Solo Administradores pueden eliminar.
     *
     * @param id ID del cliente a eliminar
     * @return 200 OK
     *         404 si no existe
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> deleteCliente(@PathVariable Integer id) {
        try {
            clienteService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Cliente eliminado correctamente", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Obtiene el número total de clientes.
     *
     * @return Contador de clientes
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countClientes() {
        long count = clienteService.count();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}