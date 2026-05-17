import 'vehiculo.dart';

/**
 * Modelo Cita para la aplicación Flutter.
 * Representa una cita programada en el taller.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class Cita {
  final int idCita;
  final DateTime fechaHora;
  final String? motivo;
  final String? matricula;
  final String? marcaVehiculo;
  final String? modeloVehiculo;
  final Vehiculo? vehiculo;

  Cita({
    this.idCita = 0,
    required this.fechaHora,
    this.motivo,
    this.matricula,
    this.marcaVehiculo,
    this.modeloVehiculo,
    this.vehiculo,
  });

  factory Cita.fromJson(Map<String, dynamic> json) {
    return Cita(
      idCita: json['idCita'] ?? 0,
      fechaHora: json['fechaHora'] != null
          ? DateTime.parse(json['fechaHora'])
          : DateTime.now(),
      motivo: json['motivo'],
      matricula: json['vehiculo']?['matricula'] ?? json['matricula'],
      marcaVehiculo: json['vehiculo']?['marca'],
      modeloVehiculo: json['vehiculo']?['modelo'],
      vehiculo: json['vehiculo'] != null ? Vehiculo.fromJson(json['vehiculo']) : null,
    );
  }

  factory Cita.fromDto(Map<String, dynamic> json) {
    return Cita(
      idCita: json['idCita'] ?? 0,
      fechaHora: DateTime.parse(json['fechaHora']),
      motivo: json['motivo'],
      matricula: json['matricula'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idCita': idCita,
      'fechaHora': fechaHora.toIso8601String(),
      'motivo': motivo,
      'matricula': matricula,
    };
  }

  bool get esHoy {
    final now = DateTime.now();
    return fechaHora.year == now.year &&
        fechaHora.month == now.month &&
        fechaHora.day == now.day;
  }

  bool get esPasada => fechaHora.isBefore(DateTime.now());

  String get vehiculoDescripcion =>
      vehiculo != null ? '${vehiculo!.marca} ${vehiculo!.modelo}' : '$marcaVehiculo $modeloVehiculo';

  @override
  String toString() => 'Cita(id: $idCita, fecha: $fechaHora, motivo: $motivo)';
}