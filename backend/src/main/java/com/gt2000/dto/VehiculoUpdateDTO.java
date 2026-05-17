package com.gt2000.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar un vehículo existente.
 * No incluye validación de matrícula porque se usa la de la URL.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-02
 */
public class VehiculoUpdateDTO {

    /**
     * Marca del vehículo.
     */
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marca;

    /**
     * Modelo del vehículo.
     */
    @Size(max = 50, message = "El modelo no puede exceder 50 caracteres")
    private String modelo;

    /**
     * ID del cliente propietario.
     */
    private Integer idCliente;

    /**
     * Nombre del cliente propietario (solo para lectura).
     */
    private String nombreCliente;

    /**
     * Constructor por defecto.
     */
    public VehiculoUpdateDTO() {
    }

    /**
     * Constructor con marca y modelo.
     *
     * @param marca  Marca
     * @param modelo Modelo
     */
    public VehiculoUpdateDTO(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
