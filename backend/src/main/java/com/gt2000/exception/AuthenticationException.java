package com.gt2000.exception;

/**
 * Excepción lançada cuando las credenciales de autenticación son inválidas.
 * Corresponde a errores HTTP 401 Unauthorized.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message Mensaje descriptivo del error
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo
     * @param cause   Causa original del error
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}