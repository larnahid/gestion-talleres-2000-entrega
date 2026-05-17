package com.gt2000.service;

import com.gt2000.dto.OrdenReparacionDTO;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.OrdenReparacion;
import com.gt2000.model.Usuario;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.OrdenReparacionRepository;
import com.gt2000.repository.UsuarioRepository;
import com.gt2000.repository.VehiculoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Órdenes de Reparación.
 * Proporciona métodos CRUD, cambio de estados y gestión de órdenes.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class OrdenReparacionService {

    /**
     * Repositorio para acceso a datos de OrdenReparacion.
     */
    private final OrdenReparacionRepository ordenRepository;

    /**
     * Repositorio de vehículos (para validar relación).
     */
    private final VehiculoRepository vehiculoRepository;

    /**
     * Repositorio de usuarios (para validar relación).
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param ordenRepository  Repositorio de órdenes
     * @param vehiculoRepository Repositorio de vehículos
     * @param usuarioRepository  Repositorio de usuarios
     */
    @Autowired
    public OrdenReparacionService(OrdenReparacionRepository ordenRepository,
                                   VehiculoRepository vehiculoRepository,
                                   UsuarioRepository usuarioRepository) {
        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todas las órdenes con vehículo y usuario cargados.
     *
     * @return Lista de órdenes
     */
    public List<OrdenReparacion> findAll() {
        return ordenRepository.findAllWithVehiculoAndUsuario();
    }

    /**
     * Busca una orden por su ID.
     *
     * @param id ID de la orden
     * @return Orden de reparación
     * @throws ResourceNotFoundException si no existe
     */
    public OrdenReparacion findById(Integer id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de reparación no encontrada con ID: " + id));
    }

    /**
     * Obtiene todas las órdenes de un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de órdenes del vehículo
     */
    public List<OrdenReparacion> findByVehiculoMatricula(String matricula) {
        return ordenRepository.findByVehiculoMatriculaOrderByFechaEntradaDesc(matricula);
    }

    /**
     * Obtiene todas las órdenes pendientes.
     *
     * @return Lista de órdenes pendientes
     */
    public List<OrdenReparacion> findPendientes() {
        return ordenRepository.findOrdenesPendientes();
    }

    /**
     * Obtiene todas las órdenes en proceso.
     *
     * @return Lista de órdenes en proceso
     */
    public List<OrdenReparacion> findEnProceso() {
        return ordenRepository.findOrdenesEnProceso();
    }

    /**
     * Obtiene órdenes por estado.
     *
     * @param estado Estado de las órdenes
     * @return Lista de órdenes con ese estado
     */
    public List<OrdenReparacion> findByEstado(OrdenReparacion.Estado estado) {
        return ordenRepository.findByEstado(estado);
    }

    /**
     * Crea una nueva orden de reparación.
     *
     * @param ordenDTO Datos de la orden
     * @param idUsuario ID del usuario que crea la orden
     * @return Orden creada
     */
    public OrdenReparacion create(OrdenReparacionDTO ordenDTO, Integer idUsuario) {
        Vehiculo vehiculo = vehiculoRepository.findByMatricula(ordenDTO.getMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con matricula: " + ordenDTO.getMatricula()));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        OrdenReparacion orden = new OrdenReparacion(
                ordenDTO.getDescripcion(),
                vehiculo,
                usuario
        );

        if (ordenDTO.getEstado() != null) {
            orden.setEstadoDesdeTexto(ordenDTO.getEstado());
        }

        if (ordenDTO.getUrlFoto() != null) {
            orden.setUrlFoto(ordenDTO.getUrlFoto());
        }

        return ordenRepository.save(orden);
    }

    /**
     * Actualiza una orden de reparación existente.
     *
     * @param id       ID de la orden a actualizar
     * @param ordenDTO Nuevos datos
     * @return Orden actualizada
     */
    public OrdenReparacion update(Integer id, OrdenReparacionDTO ordenDTO) {
        OrdenReparacion orden = findById(id);

        if (ordenDTO.getDescripcion() != null) {
            orden.setDescripcion(ordenDTO.getDescripcion());
        }

        if (ordenDTO.getEstado() != null) {
            orden.setEstadoDesdeTexto(ordenDTO.getEstado());
        }

        if (ordenDTO.getUrlFoto() != null) {
            orden.setUrlFoto(ordenDTO.getUrlFoto());
        }

        return ordenRepository.save(orden);
    }

    /**
     * Actualiza solo el estado de una orden.
     * Método optimizado para cambios de estado rápidos.
     *
     * @param id           ID de la orden
     * @param nuevoEstado  Nuevo estado (texto)
     * @return Orden actualizada
     */
    public OrdenReparacion updateEstado(Integer id, String nuevoEstado) {
        OrdenReparacion orden = findById(id);
        orden.setEstadoDesdeTexto(nuevoEstado);
        return ordenRepository.save(orden);
    }

    /**
     * Elimina una orden por su ID.
     *
     * @param id ID de la orden a eliminar
     */
    public void deleteById(Integer id) {
        if (!ordenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Orden de reparacion no encontrada con ID: " + id);
        }
        ordenRepository.deleteById(id);
    }

    /**
     * Cuenta órdenes pendientes.
     *
     * @return Número de órdenes pendientes
     */
    public long countPendientes() {
        return ordenRepository.findOrdenesPendientes().size();
    }

    /**
     * Cuenta órdenes en proceso.
     *
     * @return Número de órdenes en proceso
     */
    public long countEnProceso() {
        return ordenRepository.findOrdenesEnProceso().size();
    }

    /**
     * Convierte una OrdenReparacion a DTO.
     *
     * @param orden Entidad orden
     * @return OrdenReparacionDTO con los datos
     */
    public OrdenReparacionDTO toDTO(OrdenReparacion orden) {
        OrdenReparacionDTO dto = new OrdenReparacionDTO();
        dto.setIdOrden(orden.getIdOrden());
        dto.setMatricula(orden.getMatriculaVehiculo());
        dto.setDescripcion(orden.getDescripcion());
        dto.setEstado(orden.getEstadoTexto());
        dto.setUrlFoto(orden.getUrlFoto());
        dto.setNombreMecanico(orden.getNombreMecanico());

        if (orden.getVehiculo() != null) {
            dto.setMarcaVehiculo(orden.getVehiculo().getMarca());
            dto.setModeloVehiculo(orden.getVehiculo().getModelo());
        }

        return dto;
    }
}