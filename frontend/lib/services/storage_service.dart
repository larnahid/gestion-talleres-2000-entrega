/**
 * Servicio de almacenamiento local.
 * Gestiona el token JWT y datos de sesión usando SharedPreferences.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
import 'package:shared_preferences/shared_preferences.dart';

class StorageService {
  static const String _tokenKey = 'jwt_token';
  static const String _usernameKey = 'username';
  static const String _rolKey = 'rol';
  static const String _idUsuarioKey = 'id_usuario';
  static const String _primerInicioKey = 'primer_inicio';

  /**
   * Guarda el token JWT.
   */
  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_tokenKey, token);
  }

  /**
   * Obtiene el token JWT almacenado.
   */
  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_tokenKey);
  }

  /**
   * Elimina el token JWT.
   */
  Future<void> deleteToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
  }

  /**
   * Guarda los datos del usuario.
   */
  Future<void> saveUserData({
    required String username,
    required String rol,
    required int idUsuario,
  }) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_usernameKey, username);
    await prefs.setString(_rolKey, rol);
    await prefs.setInt(_idUsuarioKey, idUsuario);
  }

  /**
   * Obtiene el nombre de usuario.
   */
  Future<String?> getUsername() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_usernameKey);
  }

  /**
   * Obtiene el rol del usuario.
   */
  Future<String?> getRol() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_rolKey);
  }

  /**
   * Obtiene el ID del usuario.
   */
  Future<int?> getIdUsuario() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getInt(_idUsuarioKey);
  }

  /**
   * Verifica si el usuario está logueado.
   */
  Future<bool> isLoggedIn() async {
    final token = await getToken();
    return token != null && token.isNotEmpty;
  }

  /**
   * Verifica si es la primera ejecución de la app.
   */
  Future<bool> isPrimerInicio() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_primerInicioKey) ?? true;
  }

  /**
   * Marca que la app ya ha mostrado el tutorial de primera ejecución.
   */
  Future<void> marcarPrimerInicioCompletado() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool(_primerInicioKey, false);
  }

  /**
   * Cierra la sesión del usuario.
   */
  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
    await prefs.remove(_usernameKey);
    await prefs.remove(_rolKey);
    await prefs.remove(_idUsuarioKey);
  }
}