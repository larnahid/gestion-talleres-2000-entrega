package com.gt2000.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de propiedades de la aplicación.
 * Lee valores del archivo application.yml.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Configuration
public class AppConfig {

    /**
     * Clave secreta para firmar tokens JWT.
     * Valor definido en application.yml.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Tiempo de expiración del token JWT en milisegundos.
     * Valor definido en application.yml.
     */
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Puerto del servidor.
     */
    @Value("${server.port:8080}")
    private int serverPort;

    /**
     * Ruta base para uploads.
     */
    @Value("${upload.base-url:http://localhost:8080/api/uploads}")
    private String uploadBaseUrl;

    /**
     * Obtiene la clave secreta JWT.
     *
     * @return Clave secreta
     */
    public String getJwtSecret() {
        return jwtSecret;
    }

    /**
     * Obtiene el tiempo de expiración JWT.
     *
     * @return Tiempo en milisegundos
     */
    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    /**
     * Obtiene el puerto del servidor.
     *
     * @return Puerto
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Obtiene la URL base de uploads.
     *
     * @return URL base
     */
    public String getUploadBaseUrl() {
        return uploadBaseUrl;
    }
}