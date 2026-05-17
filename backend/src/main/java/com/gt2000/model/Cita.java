package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cita programada en el taller.
 * Las citas están asociadas a un vehículo específico.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "citas")
public class Cita {

    /**
     * Identificador único de la cita (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    /**
     * Fecha y hora de la cita programada.
     * No puede ser nula.
     */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Motivo o descripción de la cita (ej: ITV, revisión, reparación).
     */
    @Column(name = "motivo", length = 255)
    private String motivo;

    /**
     * Vehículo asociado a esta cita (clave foránea).
     * Relación muchos a uno con Vehiculo.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matricula", foreignKey = @ForeignKey(name = "fk_cita_vehiculo"))
    @JsonIgnoreProperties({"ordenesReparacion", "citas"})
    private Vehiculo vehiculo;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Cita() {
    }

    /**
     * Constructor para crear una nueva cita.
     *
     * @param fechaHora Fecha y hora de la cita
     * @param motivo    Motivo de la cita
     * @param vehiculo  Vehículo asociado
     */
    public Cita(LocalDateTime fechaHora, String motivo, Vehiculo vehiculo) {
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.vehiculo = vehiculo;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * Obtiene la matrícula del vehículo asociado.
     *
     * @return Matrícula o null
     */
    public String getMatriculaVehiculo() {
        return vehiculo != null ? vehiculo.getMatricula() : null;
    }

    /**
     * Verifica si la cita ya ha pasado.
     *
     * @return true si la cita es anterior a ahora
     */
    public boolean esPasada() {
        return fechaHora != null && fechaHora.isBefore(LocalDateTime.now());
    }

    /**
     * Verifica si la cita es hoy.
     *
     * @return true si la cita es en el día actual
     */
    public boolean esHoy() {
        if (fechaHora == null) return false;
        LocalDateTime ahora = LocalDateTime.now();
        return fechaHora.toLocalDate().equals(ahora.toLocalDate());
    }

    @Override
    public String toString() {
        return "Cita{idCita=" + idCita + ", fechaHora=" + fechaHora +
               ", motivo='" + motivo + '\'' +
               ", matricula=" + (vehiculo != null ? vehiculo.getMatricula() : "null") + "}";
    }
}