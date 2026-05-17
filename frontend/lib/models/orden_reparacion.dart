import 'vehiculo.dart';

/**
 * Modelo OrdenReparacion para la aplicación Flutter.
 * Representa una orden de reparación en el taller.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class OrdenReparacion {
  final int idOrden;
  final DateTime? fechaEntrada;
  final String? descripcion;
  final String estado;
  final String? urlFoto;
  final String? matricula;
  final int? idUsuario;
  final String? nombreMecanico;
  final String? marcaVehiculo;
  final String? modeloVehiculo;
  final Vehiculo? vehiculo;

  OrdenReparacion({
    this.idOrden = 0,
    this.fechaEntrada,
    this.descripcion,
    this.estado = 'Pendiente',
    this.urlFoto,
    this.matricula,
    this.idUsuario,
    this.nombreMecanico,
    this.marcaVehiculo,
    this.modeloVehiculo,
    this.vehiculo,
  });

  factory OrdenReparacion.fromJson(Map<String, dynamic> json) {
    return OrdenReparacion(
      idOrden: json['idOrden'] ?? 0,
      fechaEntrada: json['fechaEntrada'] != null
          ? DateTime.tryParse(json['fechaEntrada'])
          : null,
      descripcion: json['descripcion'],
      estado: json['estado'] ?? 'Pendiente',
      urlFoto: json['urlFoto'],
      matricula: json['vehiculo']?['matricula'] ?? json['matricula'],
      idUsuario: json['usuario']?['idUsuario'] ?? json['idUsuario'],
      nombreMecanico: json['usuario']?['username'],
      marcaVehiculo: json['vehiculo']?['marca'],
      modeloVehiculo: json['vehiculo']?['modelo'],
      vehiculo: json['vehiculo'] != null ? Vehiculo.fromJson(json['vehiculo']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'idOrden': idOrden,
      'matricula': matricula,
      'descripcion': descripcion,
      'estado': estado,
      'urlFoto': urlFoto,
      'idUsuario': idUsuario,
    };
  }

  bool get esPendiente => estado == 'Pendiente';
  bool get esEnProceso => estado == 'En Proceso';
  bool get esTerminado => estado == 'Terminado';

  String get vehiculoDescripcion =>
      vehiculo != null ? '${vehiculo!.marca} ${vehiculo!.modelo}' : '$marcaVehiculo $modeloVehiculo';

  @override
  String toString() =>
      'OrdenReparacion(id: $idOrden, estado: $estado, matricula: $matricula)';
}