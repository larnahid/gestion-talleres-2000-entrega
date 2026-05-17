package com.gt2000.controller;

import com.gt2000.dto.ApiResponse;
import com.gt2000.dto.LoginRequest;
import com.gt2000.dto.LoginResponse;
import com.gt2000.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para autenticación y gestión de usuarios.
 * Proporciona endpoints para login, logout y perfil de usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Servicio de usuarios para autenticación.
     */
    private final UsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioService Servicio de usuarios
     */
    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para iniciar sesión.
     * Valida credenciales y devuelve token JWT.
     *
     * @param loginRequest Credenciales (username, password)
     * @return 200 OK con LoginResponse (token, datos usuario)
     *         401 Unauthorized si credenciales inválidas
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = usuarioService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", loginResponse));
    }

    /**
     * Endpoint para cerrar sesión.
     * En backend stateless JWT, esto es principalmente informativo.
     *
     * @return 200 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout realizado", null));
    }

    /**
     * Endpoint para obtener el perfil del usuario autenticado.
     * Requiere token JWT válido.
     *
     * @param authentication Credenciales de Spring Security
     * @return 200 OK con datos del usuario
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(Authentication authentication) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", authentication.getName());
        userData.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(ApiResponse.success(userData));
    }

    /**
     * Endpoint de salud para verificar que la API está activa.
     *
     * @return 200 OK con mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("API funcionando correctamente", "OK"));
    }
}