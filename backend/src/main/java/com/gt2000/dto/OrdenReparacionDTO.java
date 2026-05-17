package com.gt2000.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear o actualizar una orden de reparación.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
public class OrdenReparacionDTO {

    /**
     * ID de la orden (null para crear, no null para actualizar).
     */
    private Integer idOrden;

/**
     * Matricula del vehiculo asociado.
     * Requerido solo para crear, opcional para actualizar.
     */
    private String matricula;

    /**
     * Descripción de la avería o reparación.
     */
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 2000, message = "La descripcion no puede exceder 2000 caracteres")
    private String descripcion;

    /**
     * Estado de la orden.
     * Valores posibles: Pendiente, En Proceso, A falta de piezas, Terminado
     */
    private String estado;

    /**
     * URL de la foto adjunta (opcional).
     */
    private String urlFoto;

    /**
     * ID del usuario (mecánico) que gestiona la orden.
     */
    private Integer idUsuario;

    /**
     * Nombre del mecánico asignado (solo para lectura).
     */
    private String nombreMecanico;

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
    public OrdenReparacionDTO() {
    }

    // ------------------------------
    // Getters y Setters
    // ------------------------------

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreMecanico() {
        return nombreMecanico;
    }

    public void setNombreMecanico(String nombreMecanico) {
        this.nombreMecanico = nombreMecanico;
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