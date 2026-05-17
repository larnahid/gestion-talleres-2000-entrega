package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema (Mecánico o Administrador).
 * Cada usuario tiene credenciales de acceso y un rol asociado.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /**
     * Nombre de usuario único para login.
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña cifrada (hash BCrypt).
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * Rol asociado al usuario (clave foránea).
     * Relación muchos a uno con Rol.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", foreignKey = @ForeignKey(name = "fk_usuario_rol"))
    private Rol rol;

    /**
     * Lista de órdenes de reparación gestionadas por este usuario.
     * Relación uno a muchos con OrdenReparacion.
     */
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrdenReparacion> ordenesReparacion;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor completo para crear un usuario.
     *
     * @param username Nombre de usuario único
     * @param password Contraseña cifrada
     * @param rol      Rol asignado
     */
    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<OrdenReparacion> getOrdenesReparacion() {
        return ordenesReparacion;
    }

    public void setOrdenesReparacion(List<OrdenReparacion> ordenesReparacion) {
        this.ordenesReparacion = ordenesReparacion;
    }

    /**
     * Obtiene el nombre del rol como texto.
     *
     * @return Nombre del rol o "Sin rol" si no tiene
     */
    public String getNombreRol() {
        return rol != null ? rol.getNombreRol() : "Sin rol";
    }

    /**
     * Verifica si el usuario tiene rol de administrador.
     *
     * @return true si es administrador
     */
    public boolean esAdministrador() {
        return rol != null && "ADMINISTRADOR".equalsIgnoreCase(rol.getNombreRol());
    }

    @Override
    public String toString() {
        return "Usuario{idUsuario=" + idUsuario + ", username='" + username + "', rol=" + (rol != null ? rol.getNombreRol() : "null") + "}";
    }
}