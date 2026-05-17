package com.gt2000.repository;

import com.gt2000.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre de usuario (login).
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe, vacío si no
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre especificado.
     *
     * @param username Nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);

    /**
     * Busca todos los usuarios que tienen un rol específico.
     *
     * @param idRol ID del rol
     * @return Lista de usuarios con ese rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.idRol = :idRol")
    List<Usuario> findByRolId(@Param("idRol") Integer idRol);

    /**
     * Busca un usuario por username incluyendo su rol (fetch eager).
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario y su rol
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRol(@Param("username") String username);
}