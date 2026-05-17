package com.gt2000.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Proveedor de tokens JWT para autenticación.
 * Genera y valida tokens JSON Web Token.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Component
public class JwtTokenProvider {

    /**
     * Clave secreta para firmar tokens (mín 256 bits para HS256).
     */
    private final SecretKey key;

    /**
     * Tiempo de expiración del token en milisegundos.
     */
    private final long jwtExpiration;

    /**
     * Constructor que inicializa la clave desde la configuración.
     *
     * @param jwtSecret Clave secreta del archivo de propiedades
     * @param jwtExpirationMs Tiempo de expiración en ms
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpiration = jwtExpirationMs;
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param username  Nombre de usuario
     * @param rol       Nombre del rol (ADMINISTRADOR o MECANICO)
     * @return Token JWT generado
     */
    public String generateToken(String username, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String authorities = rol;

        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Genera un token desde un objeto Authentication de Spring Security.
     *
     * @param authentication Objeto de autenticificación
     * @return Token JWT
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String rol = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return generateToken(username, rol);
    }

    /**
     * Obtiene el nombre de usuario desde un token JWT.
     *
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Obtiene el rol desde un token JWT.
     *
     * @param token Token JWT
     * @return Nombre del rol
     */
    public String getRolFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("rol", String.class);
    }

    /**
     * Valida un token JWT.
     *
     * @param token Token a validar
     * @return true si el token es válido, false si es inválido o expirado
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Verifica si un token ha expirado.
     *
     * @param token Token a verificar
     * @return true si ha expirado
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}