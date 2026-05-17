import 'package:flutter/material.dart';
import '../services/auth_service.dart';
import '../models/usuario.dart';

/**
 * Provider para gestionar el estado de autenticación.
 * Usa ChangeNotifier para notificar a los widgets cuando cambia el estado.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class AuthProvider extends ChangeNotifier {
  final AuthService _authService = AuthService();

  Usuario? _usuario;
  bool _isLoading = false;
  String? _error;

  /**
   * Obtiene el usuario actual.
   */
  Usuario? get usuario => _usuario;

  /**
   * Indica si hay una operación en curso.
   */
  bool get isLoading => _isLoading;

  /**
   * Indica si el usuario está logueado.
   */
  bool get isLoggedIn => _usuario != null;

  /**
   * Mensaje de error actual.
   */
  String? get error => _error;

  /**
   * Indica si el usuario es administrador.
   */
  bool get isAdmin => _usuario?.esAdministrador ?? false;

  /**
   * Inicializa el provider cargando el usuario desde almacenamiento local.
   */
  Future<void> init() async {
    _usuario = await _authService.getCurrentUser();
    notifyListeners();
  }

  /**
   * Inicia sesión con credenciales.
   */
  Future<void> login(String username, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _usuario = await _authService.login(username, password);
    } catch (e) {
      _error = e.toString();
      rethrow;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  /**
   * Cierra la sesión del usuario.
   */
  Future<void> logout() async {
    await _authService.logout();
    _usuario = null;
    notifyListeners();
  }

  /**
   * Obtiene el ID del usuario actual.
   */
  Future<int?> getIdUsuario() async {
    return await _authService.getCurrentUser().then((u) => u?.idUsuario);
  }
}