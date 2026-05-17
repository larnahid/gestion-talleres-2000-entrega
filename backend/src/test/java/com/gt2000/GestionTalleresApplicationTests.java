package com.gt2000;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test de integración que verifica que la aplicación Spring Boot arranca correctamente.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@SpringBootTest
@ActiveProfiles("test")
class GestionTalleresApplicationTests {

    /**
     * Verifica que el contexto de Spring Boot se carga correctamente.
     * Este test asegura que todas las configuraciones y beans están bien definidos.
     */
    @Test
    void contextLoads() {
        assertNotNull(this);
    }
}