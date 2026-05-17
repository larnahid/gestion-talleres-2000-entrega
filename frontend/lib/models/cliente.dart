import 'vehiculo.dart';

/**
 * Modelo Cliente para la aplicación Flutter.
 * Representa un cliente del taller.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class Cliente {
  final int idCliente;
  final String dni;
  final String nombre;
  final String? telefono;
  final List<Vehiculo>? vehiculos;

  Cliente({
    required this.idCliente,
    required this.dni,
    required this.nombre,
    this.telefono,
    this.vehiculos,
  });

  factory Cliente.fromJson(Map<String, dynamic> json) {
    return Cliente(
      idCliente: json['idCliente'] ?? 0,
      dni: json['dni'] ?? '',
      nombre: json['nombre'] ?? '',
      telefono: json['telefono'],
      vehiculos: json['vehiculos'] != null
          ? (json['vehiculos'] as List)
              .map((v) => Vehiculo.fromJson(v))
              .toList()
          : null,
    );
  }

  factory Cliente.fromDto(Map<String, dynamic> json) {
    return Cliente(
      idCliente: json['idCliente'] ?? 0,
      dni: json['dni'] ?? '',
      nombre: json['nombre'] ?? '',
      telefono: json['telefono'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idCliente': idCliente,
      'dni': dni,
      'nombre': nombre,
      'telefono': telefono,
    };
  }

  int get numeroVehiculos => vehiculos?.length ?? 0;

  @override
  String toString() => 'Cliente(id: $idCliente, dni: $dni, nombre: $nombre)';
}