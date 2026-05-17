import 'rol.dart';

/**
 * Modelo Usuario para la aplicación Flutter.
 * Representa un usuario autenticado en el sistema.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class Usuario {
  final int idUsuario;
  final String username;
  final String rol;
  final String? nombreCompleto;

  Usuario({
    required this.idUsuario,
    required this.username,
    required this.rol,
    this.nombreCompleto,
  });

  /**
   * Crea un Usuario desde JSON (respuesta de login).
   */
  factory Usuario.fromLoginJson(Map<String, dynamic> json) {
    return Usuario(
      idUsuario: json['idUsuario'] ?? 0,
      username: json['username'] ?? '',
      rol: json['rol'] ?? '',
      nombreCompleto: json['nombreCompleto'],
    );
  }

  /**
   * Crea un Usuario desde JSON genérico.
   */
  factory Usuario.fromJson(Map<String, dynamic> json) {
    return Usuario(
      idUsuario: json['idUsuario'] ?? 0,
      username: json['username'] ?? '',
      rol: json['rol']?['nombreRol'] ?? json['rol'] ?? '',
      nombreCompleto: json['nombreCompleto'],
    );
  }

  /**
   * Convierte el Usuario a JSON.
   */
  Map<String, dynamic> toJson() {
    return {
      'idUsuario': idUsuario,
      'username': username,
      'rol': rol,
      'nombreCompleto': nombreCompleto,
    };
  }

  /**
   * Verifica si es administrador.
   */
  bool get esAdministrador => rol == 'ADMINISTRADOR';

  /**
   * Verifica si es mecánico.
   */
  bool get esMecanico => rol == 'MECANICO';

  @override
  String toString() => 'Usuario(id: $idUsuario, username: $username, rol: $rol)';
}