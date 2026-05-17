package com.gt2000.dto;

/**
 * DTO para respuestas de inicio de sesión (Login).
 * Contiene el token JWT y datos básicos del usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class LoginResponse {

    /**
     * Token JWT generado tras login exitoso.
     */
    private String token;

    /**
     * Tipo de token (siempre "Bearer").
     */
    private String tokenType = "Bearer";

    /**
     * ID del usuario.
     */
    private Integer idUsuario;

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Nombre del rol (ADMINISTRADOR o MECANICO).
     */
    private String rol;

    /**
     * Nombre completo del usuario (para mostrar en la app).
     */
    private String nombreCompleto;

    /**
     * Constructor por defecto.
     */
    public LoginResponse() {
    }

    /**
     * Constructor completo para crear una respuesta de login.
     *
     * @param token         Token JWT
     * @param idUsuario     ID del usuario
     * @param username      Nombre de usuario
     * @param rol           Nombre del rol
     * @param nombreCompleto Nombre para mostrar
     */
    public LoginResponse(String token, Integer idUsuario, String username, String rol, String nombreCompleto) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.username = username;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}