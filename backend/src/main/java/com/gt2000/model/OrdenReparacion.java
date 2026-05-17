package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una orden de reparación en el taller.
 * Cada orden está asociada a un vehículo y gestionada por un usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "ordenes_reparacion")
public class OrdenReparacion {

    /**
     * Estados posibles de una orden de reparación.
     */
    public enum Estado {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        A_FALTA_PIEZAS("A falta de piezas"),
        TERMINADO("Terminado");

        private final String texto;

        Estado(String texto) {
            this.texto = texto;
        }

        public String getTexto() {
            return texto;
        }
    }

    /**
     * Identificador único de la orden (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;

    /**
     * Fecha y hora de entrada del vehículo al taller.
     * Valor por defecto: momento de creación.
     */
    @Column(name = "fecha_entrada", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaEntrada;

    /**
     * Descripción técnica de la avería o reparación a realizar.
     */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Estado actual de la orden de reparación.
     * Valor por defecto: PENDIENTE.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private Estado estado = Estado.PENDIENTE;

    /**
     * URL de la foto adjunta (almacenada en servidor).
     * No se almacena la imagen en la base de datos.
     */
    @Column(name = "url_foto", length = 255)
    private String urlFoto;

    /**
     * Vehículo asociado a esta orden (clave foránea).
     * Relación muchos a uno con Vehiculo.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matricula", foreignKey = @ForeignKey(name = "fk_orden_vehiculo"))
    @JsonIgnoreProperties({"ordenesReparacion", "citas"})
    private Vehiculo vehiculo;

    /**
     * Usuario (mecánico) que gestiona esta orden (clave foránea).
     * Relación muchos a uno con Usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "fk_orden_usuario"))
    @JsonIgnoreProperties({"ordenesReparacion"})
    private Usuario usuario;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public OrdenReparacion() {
        this.fechaEntrada = LocalDateTime.now();
    }

    /**
     * Constructor para crear una nueva orden de reparación.
     *
     * @param descripcion Descripción de la avería
     * @param vehiculo    Vehículo a reparar
     * @param usuario     Mecánico asignado
     */
    public OrdenReparacion(String descripcion, Vehiculo vehiculo, Usuario usuario) {
        this.fechaEntrada = LocalDateTime.now();
        this.descripcion = descripcion;
        this.estado = Estado.PENDIENTE;
        this.vehiculo = vehiculo;
        this.usuario = usuario;
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

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el texto del estado (para compatibilidad con JSON).
     *
     * @return Texto del estado
     */
    public String getEstadoTexto() {
        return estado != null ? estado.getTexto() : null;
    }

    /**
     * Establece el estado a partir de texto (para recibir desde Flutter).
     *
     * @param estadoTexto Texto del estado (Pendiente, En Proceso, etc.)
     */
    public void setEstadoDesdeTexto(String estadoTexto) {
        if (estadoTexto != null) {
            for (Estado e : Estado.values()) {
                if (e.getTexto().equalsIgnoreCase(estadoTexto)) {
                    this.estado = e;
                    return;
                }
            }
            try {
                this.estado = Estado.valueOf(estadoTexto);
            } catch (IllegalArgumentException e) {
                // Estado no encontrado, dejar como está
            }
        }
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
     * Obtiene el nombre del mecánico asignado.
     *
     * @return Nombre de usuario o null
     */
    public String getNombreMecanico() {
        return usuario != null ? usuario.getUsername() : null;
    }

    @Override
    public String toString() {
        return "OrdenReparacion{idOrden=" + idOrden + ", fechaEntrada=" + fechaEntrada +
               ", estado='" + (estado != null ? estado.getTexto() : "null") + '\'' +
               ", matricula=" + (vehiculo != null ? vehiculo.getMatricula() : "null") +
               ", mecanico=" + (usuario != null ? usuario.getUsername() : "null") + "}";
    }
}