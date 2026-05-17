package com.gt2000.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un cliente del taller.
 * Un cliente puede tener varios vehículos registrados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    /**
     * Identificador único del cliente (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    /**
     * Documento Nacional de Identidad único del cliente.
     */
    @Column(name = "dni", nullable = false, unique = true, length = 12)
    private String dni;

    /**
     * Nombre completo del cliente.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Número de teléfono de contacto.
     */
    @Column(name = "telefono", length = 15)
    private String telefono;

    /**
     * Lista de vehículos propiedad del cliente.
     * Relación uno a muchos con Vehiculo.
     */
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehiculo> vehiculos;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Cliente() {
    }

    /**
     * Constructor para crear un nuevo cliente.
     *
     * @param dni     DNI del cliente (único)
     * @param nombre  Nombre completo
     * @param telefono Teléfono de contacto
     */
    public Cliente(String dni, String nombre, String telefono) {
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

    @JsonIgnore
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /**
     * Obtiene el número de vehículos que tiene el cliente.
     *
     * @return Cantidad de vehículos
     */
    public int getNumeroVehiculos() {
        return vehiculos != null ? vehiculos.size() : 0;
    }

    @Override
    public String toString() {
        return "Cliente{idCliente=" + idCliente + ", dni='" + dni + "', nombre='" + nombre + "', telefono='" + telefono + "'}";
    }
}