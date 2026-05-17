package com.gt2000.exception;

/**
 * Excepción lançada cuando se intenta crear un recurso que ya existe.
 * Corresponde a errores HTTP 409 Conflict.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message Mensaje descriptivo del error
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo
     * @param cause   Causa original del error
     */
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}