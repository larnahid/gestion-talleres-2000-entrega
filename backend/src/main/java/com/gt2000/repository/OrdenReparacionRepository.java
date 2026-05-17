package com.gt2000.repository;

import com.gt2000.model.OrdenReparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad OrdenReparacion.
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface OrdenReparacionRepository extends JpaRepository<OrdenReparacion, Integer> {

    void deleteByVehiculoMatricula(String matricula);

    /**
     * Busca todas las órdenes de un vehículo específico.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de órdenes de reparación
     */
    List<OrdenReparacion> findByVehiculoMatricula(String matricula);

    /**
     * Busca todas las órdenes gestionadas por un usuario específico.
     *
     * @param idUsuario ID del usuario (mecánico)
     * @return Lista de órdenes gestionadas
     */
    List<OrdenReparacion> findByUsuarioIdUsuario(Integer idUsuario);

    /**
     * Busca órdenes por estado.
     *
     * @param estado Estado de la orden
     * @return Lista de órdenes con ese estado
     */
    List<OrdenReparacion> findByEstado(OrdenReparacion.Estado estado);

    /**
     * Busca órdenes de un vehículo con un estado específico.
     *
     * @param matricula Matrícula del vehículo
     * @param estado    Estado de la orden
     * @return Lista de órdenes que coinciden
     */
    List<OrdenReparacion> findByVehiculoMatriculaAndEstado(String matricula, OrdenReparacion.Estado estado);

    /**
     * Busca órdenes pendientes (estado = PENDIENTE).
     *
     * @return Lista de órdenes pendientes
     */
    @Query("SELECT o FROM OrdenReparacion o WHERE o.estado = 'PENDIENTE' ORDER BY o.fechaEntrada ASC")
    List<OrdenReparacion> findOrdenesPendientes();

    /**
     * Busca órdenes en proceso (estado = EN_PROCESO).
     *
     * @return Lista de órdenes en proceso
     */
    @Query("SELECT o FROM OrdenReparacion o WHERE o.estado = 'EN_PROCESO' ORDER BY o.fechaEntrada ASC")
    List<OrdenReparacion> findOrdenesEnProceso();

    /**
     * Cuenta cuántas órdenes tiene un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Número de órdenes
     */
    @Query("SELECT COUNT(o) FROM OrdenReparacion o WHERE o.vehiculo.matricula = :matricula")
    long countByVehiculoMatricula(@Param("matricula") String matricula);

    /**
     * Obtiene todas las órdenes con vehículo y usuario cargados (fetch join).
     *
     * @return Lista de órdenes con relaciones cargadas
     */
    @Query("SELECT o FROM OrdenReparacion o JOIN FETCH o.vehiculo v JOIN FETCH o.usuario u ORDER BY o.fechaEntrada DESC")
    List<OrdenReparacion> findAllWithVehiculoAndUsuario();

    /**
     * Busca órdenes por matrícula de vehículo ordenadas por fecha.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de órdenes ordenadas por fecha de entrada
     */
    @Query("SELECT o FROM OrdenReparacion o JOIN FETCH o.vehiculo WHERE o.vehiculo.matricula = :matricula ORDER BY o.fechaEntrada DESC")
    List<OrdenReparacion> findByVehiculoMatriculaOrderByFechaEntradaDesc(@Param("matricula") String matricula);
}