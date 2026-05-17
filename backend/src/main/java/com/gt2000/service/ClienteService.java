package com.gt2000.service;

import com.gt2000.dto.ClienteDTO;
import com.gt2000.exception.ResourceAlreadyExistsException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Cliente;
import com.gt2000.model.Vehiculo;
import com.gt2000.repository.ClienteRepository;
import com.gt2000.repository.VehiculoRepository;
import com.gt2000.repository.OrdenReparacionRepository;
import com.gt2000.repository.CitaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con Clientes.
 * Proporciona métodos CRUD y de búsqueda.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final OrdenReparacionRepository ordenRepository;
    private final CitaRepository citaRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, VehiculoRepository vehiculoRepository,
                         OrdenReparacionRepository ordenRepository, CitaRepository citaRepository) {
        this.clienteRepository = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.ordenRepository = ordenRepository;
        this.citaRepository = citaRepository;
    }

    /**
     * Obtiene todos los clientes ordenados por nombre.
     *
     * @return Lista de clientes
     */
    public List<Cliente> findAll() {
        return clienteRepository.findAllByOrderByNombreAsc();
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id ID del cliente
     * @return Optional con el cliente si existe
     */
    public Cliente findById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    /**
     * Busca un cliente por su DNI.
     *
     * @param dni DNI del cliente
     * @return Optional con el cliente si existe
     */
    public Cliente findByDni(String dni) {
        return clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con DNI: " + dni));
    }

    /**
     * Busca clientes por nombre (búsqueda parcial).
     *
     * @param nombre Parte del nombre a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> findByNombreContaining(String nombre) {
        return clienteRepository.findByNombreContaining(nombre);
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param clienteDTO Datos del cliente a crear
     * @return Cliente creado
     * @throws ResourceAlreadyExistsException si ya existe un cliente con ese DNI
     */
    public Cliente create(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByDni(clienteDTO.getDni())) {
            throw new ResourceAlreadyExistsException("Ya existe un cliente con DNI: " + clienteDTO.getDni());
        }

        Cliente cliente = new Cliente(
                clienteDTO.getDni(),
                clienteDTO.getNombre(),
                clienteDTO.getTelefono()
        );

        return clienteRepository.save(cliente);
    }

    /**
     * Actualiza un cliente existente.
     *
     * @param id         ID del cliente a actualizar
     * @param clienteDTO  Nuevos datos del cliente
     * @return Cliente actualizado
     */
    public Cliente update(Integer id, ClienteDTO clienteDTO) {
        Cliente cliente = findById(id);

        if (clienteDTO.getNombre() != null) {
            cliente.setNombre(clienteDTO.getNombre());
        }
        if (clienteDTO.getTelefono() != null) {
            cliente.setTelefono(clienteDTO.getTelefono());
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Elimina un cliente por su ID.
     * Elimina en cascada: primero elimina los vehiculos asociados (y sus ordenes y citas).
     *
     * @param id ID del cliente a eliminar
     * @throws ResourceNotFoundException si el cliente no existe
     */
    public void deleteById(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        Cliente cliente = clienteRepository.findById(id).get();
        List<Vehiculo> vehiculos = cliente.getVehiculos();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos) {
                ordenRepository.deleteByVehiculoMatricula(vehiculo.getMatricula());
                citaRepository.deleteByVehiculoMatricula(vehiculo.getMatricula());
            }
        }
        vehiculoRepository.deleteByClienteIdCliente(id);
        clienteRepository.deleteById(id);
    }

    /**
     * Obtiene el numero de vehiculos de un cliente.
     *
     * @param id ID del cliente
     * @return numero de vehiculos
     */
    public int getNumeroVehiculos(Integer id) {
        return clienteRepository.findById(id)
                .map(c -> c.getVehiculos().size())
                .orElse(0);
    }

    /**
     * Cuenta el número total de clientes.
     *
     * @return Número de clientes
     */
    public long count() {
        return clienteRepository.count();
    }

    /**
     * Verifica si existe un cliente con el DNI especificado.
     *
     * @param dni DNI a verificar
     * @return true si existe
     */
    public boolean existsByDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }
}