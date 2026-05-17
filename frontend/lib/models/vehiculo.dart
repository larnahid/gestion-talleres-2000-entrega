/**
 * Modelo Vehiculo para la aplicación Flutter.
 * Representa un vehículo registrado en el taller.
 * La matrícula es el identificador único usado para QR.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class Vehiculo {
  final String matricula;
  final String marca;
  final String modelo;
  final int? idCliente;
  final String? nombreCliente;
  final String? dniCliente;

  Vehiculo({
    required this.matricula,
    required this.marca,
    required this.modelo,
    this.idCliente,
    this.nombreCliente,
    this.dniCliente,
  });

  /**
   * Crea un Vehiculo desde JSON.
   */
  factory Vehiculo.fromJson(Map<String, dynamic> json) {
    return Vehiculo(
      matricula: json['matricula'] ?? '',
      marca: json['marca'] ?? '',
      modelo: json['modelo'] ?? '',
      idCliente: json['cliente']?['idCliente'],
      nombreCliente: json['cliente']?['nombre'],
      dniCliente: json['cliente']?['dni'],
    );
  }

  /**
   * Crea un Vehiculo desde un DTO.
   */
  factory Vehiculo.fromDto(Map<String, dynamic> json) {
    return Vehiculo(
      matricula: json['matricula'] ?? '',
      marca: json['marca'] ?? '',
      modelo: json['modelo'] ?? '',
      idCliente: json['idCliente'],
      nombreCliente: json['nombreCliente'],
    );
  }

  /**
   * Convierte el Vehiculo a JSON.
   */
  Map<String, dynamic> toJson() {
    return {
      'matricula': matricula,
      'marca': marca,
      'modelo': modelo,
      'idCliente': idCliente,
    };
  }

  /**
   * Descripción completa del vehículo.
   */
  String get descripcionCompleta => '$marca $modelo ($matricula)';

  @override
  String toString() => 'Vehiculo(matricula: $matricula, marca: $marca, modelo: $modelo)';
}