package com.gt2000.service;

import com.gt2000.dto.VehiculoDTO;
import com.gt2000.dto.VehiculoUpdateDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cliente;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.VehiculoRepository;
import com.gt2000.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Vehículos.
 * Proporciona métodos CRUD, búsqueda por matrícula (QR) y gestión de vehículos.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class VehiculoService {

    /**
     * Repositorio para acceso a datos de Vehiculo.
     */
    private final VehiculoRepository vehiculoRepository;

    /**
     * Repositorio de clientes (para validar relación).
     */
    private final ClienteRepository clienteRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param vehiculoRepository Repositorio de vehículos
     * @param clienteRepository  Repositorio de clientes
     */
    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository, ClienteRepository clienteRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtiene todos los vehículos con información del cliente.
     *
     * @return Lista de vehículos
     */
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAllWithCliente();
    }

    /**
     * Busca un vehículo por su matrícula.
     * Este es el método principal usado cuando se escanea un QR.
     *
     * @param matricula Matrícula del vehículo
     * @return Optional con el vehículo si existe
     */
    public Vehiculo findByMatricula(String matricula) {
        String normalized = matricula.replace("-", "").toUpperCase();
        return vehiculoRepository.findByMatricula(normalized)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con matricula: " + matricula));
    }

    /**
     * Busca un vehículo por matrícula (para uso interno).
     *
     * @param matricula Matrícula a buscar
     * @return Optional con el vehículo
     */
    public Vehiculo findByMatriculaOptional(String matricula) {
        return vehiculoRepository.findByMatricula(matricula).orElse(null);
    }

    /**
     * Obtiene todos los vehículos de un cliente.
     *
     * @param idCliente ID del cliente
     * @return Lista de vehículos del cliente
     */
    public List<Vehiculo> findByClienteId(Integer idCliente) {
        return vehiculoRepository.findByClienteIdCliente(idCliente);
    }

    /**
     * Busca vehículos por marca.
     *
     * @param marca Marca a buscar
     * @return Lista de vehículos de esa marca
     */
    public List<Vehiculo> findByMarca(String marca) {
        return vehiculoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    /**
     * Crea un nuevo vehículo.
     *
     * @param vehiculoDTO Datos del vehículo
     * @return Vehículo creado
     * @throws ResourceAlreadyExistsException si ya existe un vehículo con esa matrícula
     * @throws ResourceNotFoundException si el cliente no existe
     */
    public Vehiculo create(VehiculoDTO vehiculoDTO) {
        String normalizedMatricula = vehiculoDTO.getMatricula().replace("-", "").toUpperCase();
        if (vehiculoRepository.existsByMatricula(normalizedMatricula)) {
            throw new ResourceAlreadyExistsException("Ya existe un vehiculo con matricula: " + vehiculoDTO.getMatricula());
        }

        Cliente cliente = clienteRepository.findById(vehiculoDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + vehiculoDTO.getIdCliente()));

        Vehiculo vehiculo = new Vehiculo(
                normalizedMatricula,
                vehiculoDTO.getMarca(),
                vehiculoDTO.getModelo(),
                cliente
        );

        return vehiculoRepository.save(vehiculo);
    }

    /**
     * Actualiza un vehículo existente.
     *
     * @param matricula         Matrícula del vehículo a actualizar
     * @param vehiculoUpdateDTO Nuevos datos
     * @return Vehículo actualizado
     */
    public Vehiculo update(String matricula, VehiculoUpdateDTO vehiculoUpdateDTO) {
        Vehiculo vehiculo = findByMatricula(matricula);

        if (vehiculoUpdateDTO.getMarca() != null) {
            vehiculo.setMarca(vehiculoUpdateDTO.getMarca());
        }
        if (vehiculoUpdateDTO.getModelo() != null) {
            vehiculo.setModelo(vehiculoUpdateDTO.getModelo());
        }
        if (vehiculoUpdateDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(vehiculoUpdateDTO.getIdCliente())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            vehiculo.setCliente(cliente);
        }

        return vehiculoRepository.save(vehiculo);
    }

    /**
     * Elimina un vehiculo por su matricula.
     * Elimina en cascada sus ordenes de reparacion y citas asociadas.
     *
     * @param matricula Matricula del vehiculo a eliminar
     * @throws ResourceNotFoundException si el vehiculo no existe
     * @throws IllegalStateException si el vehiculo tiene ordenes asociadas
     */
    public void deleteByMatricula(String matricula) {
        String normalized = matricula.replace("-", "").toUpperCase();
        Vehiculo vehiculo = vehiculoRepository.findByMatricula(normalized)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con matricula: " + matricula));

        int numOrdenes = vehiculo.getOrdenesReparacion() != null ? vehiculo.getOrdenesReparacion().size() : 0;
        int numCitas = vehiculo.getCitas() != null ? vehiculo.getCitas().size() : 0;

        if (numOrdenes > 0) {
            throw new IllegalStateException("No se puede eliminar el vehiculo porque tiene " + numOrdenes + " ordenes de reparacion asociadas. Elimina primero las ordenes.");
        }
        if (numCitas > 0) {
            throw new IllegalStateException("No se puede eliminar el vehiculo porque tiene " + numCitas + " citas asociadas. Elimina primero las citas.");
        }

        vehiculoRepository.deleteById(normalized);
    }

    public boolean existsByMatricula(String matricula) {
        String normalized = matricula.replace("-", "").toUpperCase();
        return vehiculoRepository.existsByMatricula(normalized);
    }

    /**
     * Cuenta el número total de vehículos.
     *
     * @return Número de vehículos
     */
    public long count() {
        return vehiculoRepository.count();
    }

    /**
     * Convierte un Vehiculo a DTO para enviar a Flutter.
     *
     * @param vehiculo Entidad vehículo
     * @return VehiculoDTO con los datos
     */
    public VehiculoDTO toDTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO(
                vehiculo.getMatricula(),
                vehiculo.getMarca(),
                vehiculo.getModelo()
        );
        dto.setIdCliente(vehiculo.getCliente() != null ? vehiculo.getCliente().getIdCliente() : null);
        dto.setNombreCliente(vehiculo.getNombreCliente());
        return dto;
    }
}