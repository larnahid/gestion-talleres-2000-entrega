package com.gt2000;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación GESTION TALLERES 2000.
 * Punto de entrada para el backend Spring Boot.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@SpringBootApplication
public class GestionTalleresApplication {

    /**
     * Método principal que lanza la aplicación Spring Boot.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SpringApplication.run(GestionTalleresApplication.class, args);
    }
}