package com.gt2000.dto;

/**
 * DTO genérico para respuestas de la API.
 * Envuelve cualquier objeto de respuesta con metadatos.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 * @param <T> Tipo del dato contenido en la respuesta
 */
public class ApiResponse<T> {

    /**
     * Indica si la operación fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo de la respuesta.
     */
    private String message;

    /**
     * Datos de la respuesta (puede ser null en respuestas simples).
     */
    private T data;

    /**
     * Timestamp de la respuesta (para debugging).
     */
    private long timestamp;

    /**
     * Constructor por defecto.
     */
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructor con solo éxito y mensaje.
     *
     * @param success Indica si fue exitosa
     * @param message Mensaje descriptivo
     */
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructor completo con datos.
     *
     * @param success Indica si fue exitosa
     * @param message Mensaje descriptivo
     * @param data    Datos de la respuesta
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Crea una respuesta de éxito con datos.
     *
     * @param data Datos de la respuesta
     * @param <T>  Tipo de datos
     * @return ApiResponse con éxito
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operacion exitosa", data);
    }

    /**
     * Crea una respuesta de éxito con mensaje personalizado.
     *
     * @param message Mensaje descriptivo
     * @param data    Datos de la respuesta
     * @param <T>     Tipo de datos
     * @return ApiResponse con éxito
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Crea una respuesta de error con mensaje.
     *
     * @param message Mensaje de error
     * @param <T>     Tipo de datos (null)
     * @return ApiResponse con error
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}