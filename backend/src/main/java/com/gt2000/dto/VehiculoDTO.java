package com.gt2000.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear o actualizar un vehículo.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class VehiculoDTO {

/**
     * Matricula del vehiculo (PK).
     * Formatos validos Espana (con o sin guion):
     * - 1234-ABC / 1234ABC (formato antiguo)
     * - 1234-ABC-M / 1234ABC-M (con sufijo provincial)
     * - ABC-1234 / ABC1234 (formato nuevo)
     * - 1234-AB / 1234AB (ciclomotores)
     */
    @NotBlank(message = "La matricula es obligatoria")
    @Pattern(regexp = "^[A-Z0-9]{4}-?[A-Z]{2,3}(?:-?M)?$|^[A-Z]{3}-?[A-Z0-9]{3,4}(?:-?[A-Z])?$", message = "Formato de matricula invalido")
    private String matricula;

    /**
     * Marca del vehículo.
     */
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marca;

    /**
     * Modelo del vehículo.
     */
    @NotBlank(message = "El modelo es obligatorio")
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
    public VehiculoDTO() {
    }

    /**
     * Constructor con matrícula, marca y modelo.
     *
     * @param matricula Matrícula
     * @param marca     Marca
     * @param modelo    Modelo
     */
    public VehiculoDTO(String matricula, String marca, String modelo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

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