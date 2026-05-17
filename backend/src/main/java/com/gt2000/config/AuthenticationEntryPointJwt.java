package com.gt2000.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt2000.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de errores de autenticación (401 Unauthorized).
 * Se ejecuta cuando alguien intenta acceder a un recurso protegido sin token válido.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Component
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * ObjectMapper para convertir respuestas a JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Maneja una excepción de autenticación devolviendo JSON con error 401.
     *
     * @param request           Request que produjo la excepción
     * @param response          Response HTTP
     * @param authException     Excepción de autenticación
     * @throws IOException si hay error de E/S
     */
    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.error("No autenticado: " + authException.getMessage());

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}