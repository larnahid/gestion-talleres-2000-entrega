/**
 * Modelo Rol para la aplicación Flutter.
 * Representa los roles de usuario en el sistema.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class Rol {
  final int idRol;
  final String nombreRol;

  Rol({
    required this.idRol,
    required this.nombreRol,
  });

  /**
   * Crea un Rol desde JSON.
   */
  factory Rol.fromJson(Map<String, dynamic> json) {
    return Rol(
      idRol: json['idRol'] ?? 0,
      nombreRol: json['nombreRol'] ?? '',
    );
  }

  /**
   * Convierte el Rol a JSON.
   */
  Map<String, dynamic> toJson() {
    return {
      'idRol': idRol,
      'nombreRol': nombreRol,
    };
  }

  /**
   * Verifica si es administrador.
   */
  bool get esAdministrador => nombreRol == 'ADMINISTRADOR';

  /**
   * Verifica si es mecánico.
   */
  bool get esMecanico => nombreRol == 'MECANICO';

  @override
  String toString() => 'Rol(idRol: $idRol, nombreRol: $nombreRol)';
}