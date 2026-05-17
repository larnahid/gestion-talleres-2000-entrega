package com.gt2000.repository;

import com.gt2000.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente.
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /**
     * Busca un cliente por su DNI.
     *
     * @param dni Documento Nacional de Identidad
     * @return Optional con el cliente si existe, vacío si no
     */
    Optional<Cliente> findByDni(String dni);

    /**
     * Verifica si existe un cliente con el DNI especificado.
     *
     * @param dni Documento Nacional de Identidad
     * @return true si existe, false en caso contrario
     */
    boolean existsByDni(String dni);

    /**
     * Busca clientes por nombre (búsqueda parcial, case-insensitive).
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de clientes que coinciden
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cliente> findByNombreContaining(@Param("nombre") String nombre);

    /**
     * Busca clientes ordenados por nombre.
     *
     * @return Lista de clientes ordenada alfabéticamente
     */
    List<Cliente> findAllByOrderByNombreAsc();
}