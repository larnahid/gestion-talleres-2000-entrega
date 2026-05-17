package com.gt2000.repository;

import com.gt2000.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Rol.
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre exacto.
     *
     * @param nombreRol Nombre del rol (ej: "ADMINISTRADOR", "MECANICO")
     * @return Optional con el rol si existe, vacío si no
     */
    Optional<Rol> findByNombreRol(String nombreRol);

    /**
     * Verifica si existe un rol con el nombre especificado.
     *
     * @param nombreRol Nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombreRol(String nombreRol);
}