package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un vehículo registrado en el taller.
 * La matrícula es la clave primaria ya que es un identificador único natural.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    /**
     * Matrícula del vehículo (Primary Key).
     * Formato español: 1234-ABC
     */
    @Id
    @Column(name = "matricula", length = 10)
    private String matricula;

    /**
     * Marca del vehículo (ej: Toyota, Seat, Ford).
     */
    @Column(name = "marca", length = 50)
    private String marca;

    /**
     * Modelo del vehículo (ej: Corolla, Ibiza, Transit).
     */
    @Column(name = "modelo", length = 50)
    private String modelo;

    /**
     * Cliente propietario del vehículo (clave foránea).
     * Relación muchos a uno con Cliente.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(name = "fk_vehiculo_cliente"))
    @JsonIgnoreProperties({"vehiculos"})
    private Cliente cliente;

    /**
     * Lista de órdenes de reparación asociadas a este vehículo.
     * Relación uno a muchos con OrdenReparacion.
     */
    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrdenReparacion> ordenesReparacion;

    /**
     * Lista de citas programadas para este vehículo.
     * Relación uno a muchos con Cita.
     */
    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cita> citas;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Vehiculo() {
    }

    /**
     * Constructor para crear un nuevo vehículo.
     *
     * @param matricula Matrícula única (PK)
     * @param marca     Marca del vehículo
     * @param modelo    Modelo del vehículo
     * @param cliente   Cliente propietario
     */
    public Vehiculo(String matricula, String marca, String modelo, Cliente cliente) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cliente = cliente;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<OrdenReparacion> getOrdenesReparacion() {
        return ordenesReparacion;
    }

    public void setOrdenesReparacion(List<OrdenReparacion> ordenesReparacion) {
        this.ordenesReparacion = ordenesReparacion;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    /**
     * Obtiene el nombre del propietario del vehículo.
     *
     * @return Nombre del cliente o "Sin asignar"
     */
    public String getNombreCliente() {
        return cliente != null ? cliente.getNombre() : "Sin asignar";
    }

    /**
     * Obtiene el DNI del propietario del vehículo.
     *
     * @return DNI del cliente o null
     */
    public String getDniCliente() {
        return cliente != null ? cliente.getDni() : null;
    }

    /**
     * Obtiene el número de órdenes de reparación asociadas.
     *
     * @return Cantidad de órdenes
     */
    public int getNumeroOrdenes() {
        return ordenesReparacion != null ? ordenesReparacion.size() : 0;
    }

    @Override
    public String toString() {
        return "Vehiculo{matricula='" + matricula + "', marca='" + marca + "', modelo='" + modelo + "', cliente=" + (cliente != null ? cliente.getNombre() : "null") + "}";
    }
}