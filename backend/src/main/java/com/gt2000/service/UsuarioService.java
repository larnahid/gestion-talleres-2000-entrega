package com.gt2000.service;

import com.gt2000.dto.LoginRequest;
import com.gt2000.dto.LoginResponse;
import com.gt2000.exception.AuthenticationException;
import com.gt2000.exception.ResourceNotFoundException;
import com.gt2000.model.Usuario;
import com.gt2000.repository.UsuarioRepository;
import com.gt2000.config.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Usuarios.
 * Incluye autenticación y gestión de credenciales.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
@Service
@Transactional
public class UsuarioService {

    /**
     * Repositorio para acceso a datos de Usuario.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Codificador de contraseñas (BCrypt).
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Proveedor de tokens JWT.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository Repositorio de usuarios
     * @param passwordEncoder    Codificador de contraseñas
     * @param jwtTokenProvider   Proveedor JWT
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * autentica a un usuario y genera un token JWT.
     *
     * @param loginRequest Credenciales de login
     * @return LoginResponse con token y datos del usuario
     * @throws AuthenticationException si las credenciales son inválidas
     */
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameWithRol(loginRequest.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new AuthenticationException("Usuario o contrasena incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new AuthenticationException("Usuario o contrasena incorrectos");
        }

        String token = jwtTokenProvider.generateToken(usuario.getUsername(), usuario.getRol().getNombreRol());

        return new LoginResponse(
                token,
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getRol().getNombreRol(),
                usuario.getUsername()
        );
    }

    /**
     * Obtiene todos los usuarios.
     *
     * @return Lista de usuarios
     */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Obtiene usuarios por ID de rol.
     *
     * @param idRol ID del rol
     * @return Lista de usuarios con ese rol
     */
    public List<Usuario> findByRolId(Integer idRol) {
        return usuarioRepository.findByRolId(idRol);
    }

    /**
     * Obtiene todos los mecánicos (rol MECANICO).
     *
     * @return Lista de usuarios mecánicos
     */
    public List<Usuario> findMecanicos() {
        return usuarioRepository.findByRolId(2); // Asumiendo que 2 es el ID de MECANICO
    }

    /**
     * Crea un nuevo usuario (registro).
     *
     * @param usuario Usuario a crear
     * @param rolId   ID del rol a asignar
     * @return Usuario creado
     */
    public Usuario createUser(Usuario usuario, Integer rolId) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param idUsuario       ID del usuario
     * @param nuevaPassword   Nueva contraseña (sin cifrar)
     */
    public void updatePassword(Integer idUsuario, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    /**
     * Verifica si existe un nombre de usuario.
     *
     * @param username Nombre a verificar
     * @return true si existe
     */
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar
     */
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }
}