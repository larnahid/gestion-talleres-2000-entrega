package com.gt2000.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad de Spring Boot.
 * Configura JWT, CORS, stateless sessions y endpoints públicos/protegidos.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Filtro de autenticación JWT.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Manejador de autenticación (inyectado).
     */
    private final AuthenticationEntryPointJwt authenticationEntryPoint;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jwtAuthenticationFilter Filtro JWT
     * @param authenticationEntryPoint Manejador de errores de autenticación
     */
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          AuthenticationEntryPointJwt authenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /**
     * Bean para codificar contraseñas con BCrypt.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para obtener el AuthenticationManager.
     *
     * @param authConfig Configuración de autenticación
     * @return AuthenticationManager
     * @throws Exception si hay error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error de configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (no necesario con JWT stateless)
            .csrf(csrf -> csrf.disable())

            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Configurar manejo de excepciones
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
            )

            // Establecer política de sesiones como stateless (JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/health").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Endpoints de vehículos (búsqueda por matrícula) accesibles por todos los autenticados
                .requestMatchers("/api/vehiculos/{matricula}").authenticated()

                // Endpoints de órdenes - mecánicos pueden crear, admin puede todo
                .requestMatchers("/api/ordenes/**").authenticated()

                // Endpoints de citas - solo admin
                .requestMatchers("/api/citas/**").hasRole("ADMINISTRADOR")

                // Endpoints de clientes y vehículos - admin para crear/actualizar, todos pueden leer
                .requestMatchers(HttpMethod.GET, "/api/clientes/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/clientes/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/clientes/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasRole("ADMINISTRADOR")

                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )

            // Añadir filtro JWT antes del filtro de autenticación por defecto
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde la app Flutter.
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}