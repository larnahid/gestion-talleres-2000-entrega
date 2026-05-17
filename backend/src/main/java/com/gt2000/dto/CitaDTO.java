package com.gt2000.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear o actualizar una cita.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class CitaDTO {

    /**
     * ID de la cita (null para crear, no null para actualizar).
     */
    private Integer idCita;

    /**
     * Fecha y hora de la cita.
     * Requerida.
     */
    @NotNull(message = "La fecha y hora son obligatorias")
    private String fechaHora;

    /**
     * Motivo de la cita.
     */
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    private String motivo;

    /**
     * Matrícula del vehículo asociado.
     */
    @NotBlank(message = "La matricula es obligatoria")
    private String matricula;

    /**
     * Marca del vehículo (solo para lectura).
     */
    private String marcaVehiculo;

    /**
     * Modelo del vehículo (solo para lectura).
     */
    private String modeloVehiculo;

    /**
     * Constructor por defecto.
     */
    public CitaDTO() {
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }
}