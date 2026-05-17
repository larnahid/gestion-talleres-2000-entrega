package com.gt2000.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear o actualizar un cliente.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class ClienteDTO {

    /**
     * ID del cliente (null para crear nuevo, no null para actualizar).
     */
    private Integer idCliente;

    /**
     * DNI del cliente (único).
     * Requerido para crear.
     */
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 9, max = 12, message = "El DNI debe tener entre 9 y 12 caracteres")
    private String dni;

    /**
     * Nombre completo del cliente.
     * Requerido.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    /**
     * Teléfono de contacto (opcional).
     */
    @Size(max = 15, message = "El telefono no puede exceder 15 caracteres")
    private String telefono;

    /**
     * Constructor por defecto.
     */
    public ClienteDTO() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param dni     DNI del cliente
     * @param nombre  Nombre completo
     * @param telefono Teléfono
     */
    public ClienteDTO(String dni, String nombre, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}