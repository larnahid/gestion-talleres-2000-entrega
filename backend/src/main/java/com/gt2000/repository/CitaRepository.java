package com.gt2000.repository;

import com.gt2000.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Cita.
 * Proporciona operaciones CRUD y métodos de búsqueda personalizados.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    void deleteByVehiculoMatricula(String matricula);

    /**
     * Busca todas las citas de un vehículo específico.
     *
     * @param matricula Matrícula del vehículo
     * @return Lista de citas del vehículo
     */
    List<Cita> findByVehiculoMatricula(String matricula);

    /**
     * Busca citas para una fecha específica (día).
     *
     * @param fecha Día para filtrar las citas
     * @return Lista de citas de ese día
     */
    @Query("SELECT c FROM Cita c WHERE DATE(c.fechaHora) = DATE(:fecha)")
    List<Cita> findByFecha(@Param("fecha") LocalDateTime fecha);

    /**
     * Busca citas entre dos fechas (rango).
     *
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin    Fecha de fin del rango
     * @return Lista de citas dentro del rango
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaHora ASC")
    List<Cita> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca citas futuras (a partir de ahora).
     *
     * @return Lista de citas ordenadas cronológicamente
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora >= CURRENT_TIMESTAMP ORDER BY c.fechaHora ASC")
    List<Cita> findCitasFuturas();

    /**
     * Cuenta cuántas citas tiene un vehículo.
     *
     * @param matricula Matrícula del vehículo
     * @return Número de citas
     */
    long countByVehiculoMatricula(String matricula);

    /**
     * Verifica si existe una cita a la misma hora para el mismo vehículo.
     *
     * @param fechaHora Fecha y hora de la cita
     * @param matricula Matrícula del vehículo
     * @return true si ya existe una cita a esa hora para ese vehículo
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c WHERE c.fechaHora = :fechaHora AND c.vehiculo.matricula = :matricula")
    boolean existsByFechaHoraAndVehiculoMatricula(@Param("fechaHora") LocalDateTime fechaHora, @Param("matricula") String matricula);

    /**
     * Obtiene todas las citas con vehículo cargado (fetch join).
     *
     * @return Lista de citas con relaciones
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.vehiculo ORDER BY c.fechaHora ASC")
    List<Cita> findAllWithVehiculo();
}