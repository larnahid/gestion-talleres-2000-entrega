import 'package:flutter/material.dart';
import '../models/vehiculo.dart';
import '../models/cliente.dart';
import '../services/api_service.dart';
import 'orden_form_screen.dart';
import 'ordenes_list_screen.dart';
import 'clientes_screen.dart';

/**
 * Pantalla de detalle de un vehículo.
 * Muestra datos del vehículo y opciones para crear órdenes.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class VehiculoDetailScreen extends StatefulWidget {
  final Vehiculo vehiculo;

  const VehiculoDetailScreen({super.key, required this.vehiculo});

  @override
  State<VehiculoDetailScreen> createState() => _VehiculoDetailScreenState();
}

class _VehiculoDetailScreenState extends State<VehiculoDetailScreen> {
  int _ordenesCount = 0;

  @override
  void initState() {
    super.initState();
    _loadOrdenesCount();
  }

  /**
   * Carga el número de órdenes del vehículo.
   */
  Future<void> _loadOrdenesCount() async {
    try {
      final apiService = ApiService();
      final response = await apiService.get('/ordenes/vehiculo/${widget.vehiculo.matricula}');
      final ordenes = response['data'] as List;
      if (mounted) {
        setState(() => _ordenesCount = ordenes.length);
      }
    } catch (e) {
      // Ignorar errores
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text('TALLER DIGITAL', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            icon: const Icon(Icons.delete, color: Colors.red),
            onPressed: _confirmDeleteVehiculo,
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildDatosVehiculo(),
            const SizedBox(height: 24),
            _buildDatosCliente(),
            const SizedBox(height: 24),
            _buildHistorialOrdenes(),
            const SizedBox(height: 32),
            _buildNuevaOrdenButton(),
          ],
        ),
      ),
    );
  }

  /**
   * Construye la sección de datos del vehículo.
   */
  Widget _buildDatosVehiculo() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text(
              'DATOS DEL VEHICULO',
              style: TextStyle(
                color: Colors.white,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            IconButton(
              icon: const Icon(Icons.edit, color: Color(0xFFFF6B00), size: 20),
              onPressed: () => _showEditVehiculoDialog(),
            ),
          ],
        ),
        const SizedBox(height: 12),
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: const Color(0xFF2D2D2D),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            children: [
              _buildInfoRow('Matricula', widget.vehiculo.matricula, highlight: true),
              const Divider(color: Colors.white24),
              _buildInfoRow('Marca', widget.vehiculo.marca),
              const Divider(color: Colors.white24),
              _buildInfoRow('Modelo', widget.vehiculo.modelo),
            ],
          ),
        ),
      ],
    );
  }

  /**
   * Construye la sección de datos del cliente.
   */
  Widget _buildDatosCliente() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            const Text(
              'PROPIETARIO',
              style: TextStyle(
                color: Colors.white,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            if (widget.vehiculo.idCliente != null)
              IconButton(
                icon: const Icon(Icons.edit, color: Color(0xFFFF6B00), size: 20),
                onPressed: () => _showEditClienteFromVehiculo(),
              ),
          ],
        ),
        const SizedBox(height: 12),
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: const Color(0xFF2D2D2D),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            children: [
              _buildInfoRow('Nombre', widget.vehiculo.nombreCliente ?? 'N/A'),
              if (widget.vehiculo.dniCliente != null) ...[
                const Divider(color: Colors.white24),
                _buildInfoRow('DNI', widget.vehiculo.dniCliente!),
              ],
            ],
          ),
        ),
      ],
    );
  }

  /**
   * Construye la sección del historial de órdenes.
   */
  Widget _buildHistorialOrdenes() {
    return InkWell(
      onTap: () {
        Navigator.of(context).push(
          MaterialPageRoute(
            builder: (_) => OrdenesListScreen(matricula: widget.vehiculo.matricula),
          ),
        );
      },
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: const Color(0xFF2D2D2D),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          children: [
            const Icon(Icons.history, color: Color(0xFFFF6B00)),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Historial de ordenes',
                    style: TextStyle(color: Colors.white, fontSize: 16),
                  ),
                  Text(
                    '$_ordenesCount ordenes registradas',
                    style: const TextStyle(color: Colors.white54, fontSize: 12),
                  ),
                ],
              ),
            ),
            const Icon(Icons.arrow_forward_ios, color: Colors.white54, size: 16),
          ],
        ),
      ),
    );
  }

  /**
   * Construye el botón para crear nueva orden.
   */
  Widget _buildNuevaOrdenButton() {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton.icon(
        onPressed: () {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (_) => OrdenFormScreen(vehiculo: widget.vehiculo),
            ),
          );
        },
        icon: const Icon(Icons.add),
        label: const Text(
          'NUEVA ORDEN DE REPARACION',
          style: TextStyle(fontWeight: FontWeight.bold, letterSpacing: 1),
        ),
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color(0xFFFF6B00),
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
      ),
    );
  }

  /**
   * Construye una fila de información.
   */
  Widget _buildInfoRow(String label, String value, {bool highlight = false}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            label,
            style: const TextStyle(color: Colors.white54),
          ),
          Text(
            value,
            style: TextStyle(
              color: highlight ? const Color(0xFFFF6B00) : Colors.white,
              fontWeight: highlight ? FontWeight.bold : FontWeight.normal,
              fontSize: highlight ? 18 : 14,
            ),
          ),
        ],
      ),
);
  }

  void _showEditVehiculoDialog() {
    final marcaController = TextEditingController(text: widget.vehiculo.marca);
    final modeloController = TextEditingController(text: widget.vehiculo.modelo);

    showDialog(
      context: context,
      builder: (dialogContext) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('EDITAR VEHICULO', style: TextStyle(color: Colors.white)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: marcaController,
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                labelText: 'Marca', labelStyle: TextStyle(color: Colors.white54),
                enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
              ),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: modeloController,
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                labelText: 'Modelo', labelStyle: TextStyle(color: Colors.white54),
                enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(dialogContext),
            child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
            onPressed: () async {
              if (marcaController.text.isEmpty || modeloController.text.isEmpty) {
                ScaffoldMessenger.of(dialogContext).showSnackBar(
                  const SnackBar(content: Text('Marca y Modelo son obligatorios')),
                );
                return;
              }
              try {
                await ApiService().put('/vehiculos/${widget.vehiculo.matricula}', {
                  'marca': marcaController.text,
                  'modelo': modeloController.text,
                });
                if (mounted) {
                  Navigator.pop(dialogContext);
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Vehiculo actualizado')),
                  );
                }
              } catch (e) {
                ScaffoldMessenger.of(dialogContext).showSnackBar(
                  SnackBar(content: Text('Error: $e')),
                );
              }
            },
            child: const Text('GUARDAR'),
          ),
        ],
      ),
    );
  }

  void _showEditClienteFromVehiculo() async {
    if (widget.vehiculo.idCliente == null) return;

    try {
      final response = await ApiService().get('/clientes/${widget.vehiculo.idCliente}');
      final clienteData = response['data'];
      if (clienteData == null) return;

      final cliente = Cliente(
        idCliente: clienteData['idCliente'],
        dni: clienteData['dni'],
        nombre: clienteData['nombre'],
        telefono: clienteData['telefono'],
      );

      if (!mounted) return;

      showDialog(
        context: context,
        builder: (dialogContext) {
          final nombreController = TextEditingController(text: cliente.nombre);
          final telefonoController = TextEditingController(text: cliente.telefono ?? '');

          return AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('EDITAR PROPIETARIO', style: TextStyle(color: Colors.white)),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                _detailRow('DNI', cliente.dni),
                const SizedBox(height: 12),
                TextField(
                  controller: nombreController,
                  style: const TextStyle(color: Colors.white),
                  decoration: const InputDecoration(
                    labelText: 'Nombre', labelStyle: TextStyle(color: Colors.white54),
                    enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                    focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                  ),
                ),
                const SizedBox(height: 12),
                TextField(
                  controller: telefonoController,
                  style: const TextStyle(color: Colors.white),
                  decoration: const InputDecoration(
                    labelText: 'Telefono', labelStyle: TextStyle(color: Colors.white54),
                    enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                    focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                  ),
                ),
              ],
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(dialogContext),
                child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
              ),
              ElevatedButton(
                style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
                onPressed: () async {
                  if (nombreController.text.isEmpty) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      const SnackBar(content: Text('El nombre es obligatorio')),
                    );
                    return;
                  }
                  try {
                    await ApiService().put('/clientes/${cliente.idCliente}', {
                      'dni': cliente.dni,
                      'nombre': nombreController.text,
                      'telefono': telefonoController.text.isEmpty ? null : telefonoController.text,
                    });
                    if (mounted) {
                      Navigator.pop(dialogContext);
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Propietario actualizado')),
                      );
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      SnackBar(content: Text('Error: $e')),
                    );
                  }
                },
                child: const Text('GUARDAR'),
              ),
            ],
          );
        },
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  Widget _detailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 80,
            child: Text(label, style: const TextStyle(color: Colors.white54, fontSize: 14)),
          ),
          Expanded(
            child: Text(value, style: const TextStyle(color: Colors.white, fontSize: 14)),
          ),
        ],
      ),
    );
  }

  void _confirmDeleteVehiculo() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Eliminar Vehículo', style: TextStyle(color: Colors.white)),
        content: Text(
          '¿Seguro que quieres eliminar el vehículo ${widget.vehiculo.matricula}?',
          style: const TextStyle(color: Colors.white70),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            onPressed: () {
              Navigator.pop(ctx);
              _deleteVehiculo();
            },
            child: const Text('ELIMINAR'),
          ),
        ],
      ),
    );
  }

  Future<void> _deleteVehiculo() async {
    try {
      await ApiService().delete('/vehiculos/${widget.vehiculo.matricula}');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Vehículo eliminado')),
        );
        Navigator.pop(context, true);
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: $e')),
        );
      }
    }
  }
}