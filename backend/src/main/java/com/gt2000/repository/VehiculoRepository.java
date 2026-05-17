package com.gt2000.repository;

import com.gt2000.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Vehiculo.
 * La matrícula es la clave primaria (PK).
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    void deleteByClienteIdCliente(Integer idCliente);

    /**
     * Busca un vehículo por su matrícula.
     *
     * @param matricula Matrícula del vehículo
     * @return Optional con el vehículo si existe, vacío si no
     */
    Optional<Vehiculo> findByMatricula(String matricula);

    /**
     * Verifica si existe un vehículo con la matrícula especificada.
     *
     * @param matricula Matrícula a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByMatricula(String matricula);

    /**
     * Busca todos los vehículos de un cliente específico.
     *
     * @param idCliente ID del cliente propietario
     * @return Lista de vehículos del cliente
     */
    List<Vehiculo> findByClienteIdCliente(Integer idCliente);

    /**
     * Busca vehículos por marca (búsqueda parcial).
     *
     * @param marca Marca del vehículo
     * @return Lista de vehículos de esa marca
     */
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);

    /**
     * Busca vehículos por marca y modelo.
     *
     * @param marca  Marca del vehículo
     * @param modelo Modelo del vehículo
     * @return Lista de vehículos que coinciden
     */
    List<Vehiculo> findByMarcaContainingIgnoreCaseAndModeloContainingIgnoreCase(String marca, String modelo);

    /**
     * Busca vehículos ordenados por matrícula.
     *
     * @return Lista de vehículos ordenada
     */
    @Query("SELECT v FROM Vehiculo v JOIN FETCH v.cliente ORDER BY v.matricula")
    List<Vehiculo> findAllWithCliente();
}