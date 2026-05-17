package com.gt2000.exception;

/**
 * Excepción lançada cuando no se encuentra un recurso solicitado.
 * Corresponde a errores HTTP 404 Not Found.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message Mensaje descriptivo del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo
     * @param cause   Causa original del error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}