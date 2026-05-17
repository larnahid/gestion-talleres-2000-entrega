import 'api_service.dart';
import 'storage_service.dart';
import '../models/usuario.dart';

/**
 * Servicio de autenticación.
 * Gestiona login, logout y estado de sesión.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class AuthService {
  final ApiService _apiService = ApiService();
  final StorageService _storageService = StorageService();

  /**
   * Inicia sesión con credenciales.
   *
   * @param username Nombre de usuario
   * @param password Contraseña
   * @return Usuario logueado
   */
  Future<Usuario> login(String username, String password) async {
    final response = await _apiService.post('/auth/login', {
      'username': username,
      'password': password,
    });

    final data = response['data'];
    final token = data['token'];
    final usuario = Usuario.fromLoginJson(data);

    // Guardar token y datos del usuario
    await _storageService.saveToken(token);
    await _storageService.saveUserData(
      username: usuario.username,
      rol: usuario.rol,
      idUsuario: usuario.idUsuario,
    );

    return usuario;
  }

  /**
   * Cierra la sesión del usuario.
   */
  Future<void> logout() async {
    try {
      await _apiService.post('/auth/logout', {});
    } catch (e) {
      // Ignorar errores en logout
    } finally {
      await _storageService.logout();
    }
  }

  /**
   * Verifica si el usuario está logueado.
   */
  Future<bool> isLoggedIn() async {
    return await _storageService.isLoggedIn();
  }

  /**
   * Obtiene el usuario actual desde almacenamiento local.
   */
  Future<Usuario?> getCurrentUser() async {
    final isLogged = await isLoggedIn();
    if (!isLogged) return null;

    final username = await _storageService.getUsername();
    final rol = await _storageService.getRol();
    final idUsuario = await _storageService.getIdUsuario();

    if (username == null || rol == null || idUsuario == null) return null;

    return Usuario(
      idUsuario: idUsuario,
      username: username,
      rol: rol,
    );
  }

  /**
   * Verifica si el usuario actual es administrador.
   */
  Future<bool> isAdmin() async {
    final rol = await _storageService.getRol();
    return rol == 'ADMINISTRADOR';
  }

  /**
   * Verifica si el usuario actual es mecánico.
   */
  Future<bool> isMecanico() async {
    final rol = await _storageService.getRol();
    return rol == 'MECANICO';
  }

  /**
   * Verifica si es la primera ejecución de la app.
   */
  Future<bool> isPrimerInicio() async {
    return await _storageService.isPrimerInicio();
  }

  /**
   * Marca el tutorial de primera ejecución como completado.
   */
  Future<void> completarPrimerInicio() async {
    await _storageService.marcarPrimerInicioCompletado();
  }
}