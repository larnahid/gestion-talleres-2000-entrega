package com.gt2000.service;

import com.gt2000.dto.LoginRequest;
import com.gt2000.dto.LoginResponse;
import com.gt2000.exception.AuthenticationException;
import com.gt2000.model.Rol;
import com.gt2000.model.Usuario;
import com.gt2000.repository.RolRepository;
import com.gt2000.repository.UsuarioRepository;
import com.gt2000.config.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para UsuarioService.
 * Verifica la lógica de autenticación y gestión de usuarios.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UsuarioService usuarioService;

    private Rol rolAdmin;
    private Rol rolMecanico;
    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol("ADMINISTRADOR");
        rolAdmin.setIdRol(1);

        rolMecanico = new Rol("MECANICO");
        rolMecanico.setIdRol(2);

        usuarioAdmin = new Usuario("nahid_admin", "hashedPassword", rolAdmin);
        usuarioAdmin.setIdUsuario(1);
    }

    /**
     * Test 1: Login con credenciales válidas devuelve token JWT.
     */
    @Test
    void login_conCredencialesValidas_devuelveToken() {
        LoginRequest loginRequest = new LoginRequest("nahid_admin", "password123");
        when(usuarioRepository.findByUsernameWithRol("nahid_admin")).thenReturn(Optional.of(usuarioAdmin));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn("jwt.token.here");

        LoginResponse response = usuarioService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt.token.here", response.getToken());
        assertEquals("nahid_admin", response.getUsername());
        assertEquals("ADMINISTRADOR", response.getRol());
    }

    /**
     * Test 2: Login con usuario inexistente lanza excepción.
     */
    @Test
    void login_conUsuarioInexistente_lanzaExcepcion() {
        LoginRequest loginRequest = new LoginRequest("nobody", "password");
        when(usuarioRepository.findByUsernameWithRol("nobody")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> usuarioService.login(loginRequest));
    }

    /**
     * Test 3: Login con contraseña incorrecta lanza excepción.
     */
    @Test
    void login_conPasswordIncorrecto_lanzaExcepcion() {
        LoginRequest loginRequest = new LoginRequest("nahid_admin", "wrongpassword");
        when(usuarioRepository.findByUsernameWithRol("nahid_admin")).thenReturn(Optional.of(usuarioAdmin));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> usuarioService.login(loginRequest));
    }

    /**
     * Test 4: Verificar si existe nombre de usuario.
     */
    @Test
    void existsByUsername_conUsuarioExistente_devuelveTrue() {
        when(usuarioRepository.existsByUsername("nahid_admin")).thenReturn(true);
        assertTrue(usuarioService.existsByUsername("nahid_admin"));
    }

    /**
     * Test 5: Verificar si NO existe nombre de usuario.
     */
    @Test
    void existsByUsername_conUsuarioInexistente_devuelveFalse() {
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        assertFalse(usuarioService.existsByUsername("newuser"));
    }

    /**
     * Test 6: Obtener usuario por ID.
     */
    @Test
    void findById_conIdExistente_devuelveUsuario() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioAdmin));
        Optional<Usuario> result = usuarioService.findById(1);
        assertTrue(result.isPresent());
        assertEquals("nahid_admin", result.get().getUsername());
    }

    /**
     * Test 7: Obtener usuario por ID inexistente devuelve vacío.
     */
    @Test
    void findById_conIdInexistente_devuelveVacio() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());
        Optional<Usuario> result = usuarioService.findById(999);
        assertFalse(result.isPresent());
    }

    /**
     * Test 8: Crear usuario con rol existente.
     */
    @Test
    void createUser_conRolValido_creaUsuario() {
        Usuario newUser = new Usuario("mecanico1", "password123", rolMecanico);
        when(usuarioRepository.existsByUsername("mecanico1")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(newUser);

        Usuario created = usuarioService.createUser(newUser, 2);
        assertNotNull(created);
        assertEquals("mecanico1", created.getUsername());
    }

    /**
     * Test 9: Crear usuario con nombre duplicado lanza excepción.
     */
    @Test
    void createUser_conNombreDuplicado_lanzaExcepcion() {
        Usuario newUser = new Usuario("nahid_admin", "password", rolMecanico);
        when(usuarioRepository.existsByUsername("nahid_admin")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.createUser(newUser, 2));
    }

    /**
     * Test 10: Obtener todos los usuarios.
     */
    @Test
    void findAll_devuelveTodosLosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(java.util.List.of(usuarioAdmin));
        var users = usuarioService.findAll();
        assertEquals(1, users.size());
    }
}