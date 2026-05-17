package com.gt2000.service;

import com.gt2000.model.Rol;
import com.gt2000.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Roles.
 * Proporciona métodos CRUD y de búsqueda para roles.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class RolService {

    /**
     * Repositorio para acceso a datos de Rol.
     */
    private final RolRepository rolRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param rolRepository Repositorio de roles
     */
    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Obtiene todos los roles registrados.
     *
     * @return Lista de todos los roles
     */
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    /**
     * Busca un rol por su ID.
     *
     * @param id ID del rol
     * @return Optional con el rol si existe, vacío si no
     */
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    /**
     * Busca un rol por su nombre.
     *
     * @param nombreRol Nombre del rol
     * @return Optional con el rol si existe, vacío si no
     */
    public Optional<Rol> findByNombre(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }

    /**
     * Obtiene el rol de administrador.
     *
     * @return Optional con el rol ADMINISTRADOR
     */
    public Optional<Rol> findAdministrador() {
        return rolRepository.findByNombreRol("ADMINISTRADOR");
    }

    /**
     * Obtiene el rol de mecánico.
     *
     * @return Optional con el rol MECANICO
     */
    public Optional<Rol> findMecanico() {
        return rolRepository.findByNombreRol("MECANICO");
    }

    /**
     * Guarda un nuevo rol o actualiza uno existente.
     *
     * @param rol Rol a guardar
     * @return Rol guardado
     */
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    /**
     * Verifica si existe un rol con el nombre especificado.
     *
     * @param nombreRol Nombre del rol
     * @return true si existe, false en caso contrario
     */
    public boolean existsByNombre(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }

    /**
     * Elimina un rol por su ID.
     *
     * @param id ID del rol a eliminar
     */
    public void deleteById(Integer id) {
        rolRepository.deleteById(id);
    }

    /**
     * Cuenta el número total de roles.
     *
     * @return Número de roles
     */
    public long count() {
        return rolRepository.count();
    }
}