package com.gt2000.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitudes de inicio de sesión (Login).
 * Contiene las credenciales del usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class LoginRequest {

    /**
     * Nombre de usuario para login.
     * No puede estar vacío.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    private String username;

    /**
     * Contraseña del usuario.
     * No puede estar vacía.
     */
    @NotBlank(message = "La contrasena es obligatoria")
    private String password;

    /**
     * Constructor por defecto.
     */
    public LoginRequest() {
    }

    /**
     * Constructor con username y password.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

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
}