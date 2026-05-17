package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un rol de usuario en el sistema.
 * Los roles definen los permisos y accesos de cada usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "roles")
public class Rol {

    /**
     * Identificador único del rol (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    /**
     * Nombre descriptivo del rol (ej: ADMINISTRADOR, MECANICO).
     */
    @Column(name = "nombre_rol", nullable = false, length = 30)
    private String nombreRol;

    /**
     * Lista de usuarios que tienen este rol asignado.
     * Relación uno a muchos con Usuario.
     */
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Usuario> usuarios;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Rol() {
    }

    /**
     * Constructor con nombre del rol.
     *
     * @param nombreRol Nombre del rol (ej: "ADMINISTRADOR")
     */
    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Método toString para depuración.
     *
     * @return String con los datos del rol
     */
    @Override
    public String toString() {
        return "Rol{idRol=" + idRol + ", nombreRol='" + nombreRol + "'}";
    }
}