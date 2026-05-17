import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../services/api_service.dart';
import '../models/orden_reparacion.dart';
import '../models/vehiculo.dart';
import 'orden_form_screen.dart';
import 'vehiculo_detail_screen.dart';

/**
 * Pantalla de lista de órdenes de reparación.
 * Muestra todas las órdenes o las de un vehículo específico.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class OrdenesListScreen extends StatefulWidget {
  final String? matricula; // Null = todas las órdenes

  const OrdenesListScreen({super.key, this.matricula});

  @override
  State<OrdenesListScreen> createState() => _OrdenesListScreenState();
}

class _OrdenesListScreenState extends State<OrdenesListScreen> {
  List<OrdenReparacion> _ordenes = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadOrdenes();
  }

  /**
   * Carga las órdenes desde la API.
   */
  Future<void> _loadOrdenes() async {
    setState(() => _isLoading = true);

    try {
      final apiService = ApiService();
      final endpoint = widget.matricula != null
          ? '/ordenes/vehiculo/${widget.matricula}'
          : '/ordenes';

      final response = await apiService.get(endpoint);
      final List<dynamic> data = response['data'];

      setState(() {
        _ordenes = data.map((json) => OrdenReparacion.fromJson(json)).toList();
        _isLoading = false;
      });
    } catch (e) {
      setState(() => _isLoading = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error al cargar: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: Text(
          widget.matricula != null
              ? 'Ordenes: ${widget.matricula}'
              : 'MIS ORDENES',
          style: const TextStyle(color: Colors.white),
        ),
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadOrdenes,
          ),
          if (_ordenes.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: _showDeleteOrdenDialog,
            ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: const Color(0xFFFF6B00),
        onPressed: _showCrearOrden,
        child: const Icon(Icons.add, color: Colors.white),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator(color: Color(0xFFFF6B00)))
          : _ordenes.isEmpty
              ? _buildEmpty()
              : _buildOrdenesList(),
    );
  }

  /**
   * Construye el mensaje de lista vacía.
   */
  Widget _buildEmpty() {
    return const Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.assignment_outlined, color: Colors.white38, size: 64),
          SizedBox(height: 16),
          Text(
            'No hay ordenes registradas',
            style: TextStyle(color: Colors.white54, fontSize: 18),
          ),
        ],
      ),
    );
  }

  /**
   * Construye la lista de órdenes.
   */
  Widget _buildOrdenesList() {
    return RefreshIndicator(
      onRefresh: _loadOrdenes,
      color: const Color(0xFFFF6B00),
      child: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: _ordenes.length,
        itemBuilder: (context, index) {
          final orden = _ordenes[index];
          return _buildOrdenCard(orden);
        },
      ),
    );
  }

  /**
   * Construye una tarjeta de orden.
   */
  Widget _buildOrdenCard(OrdenReparacion orden) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D2D),
        borderRadius: BorderRadius.circular(12),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(16),
        leading: Container(
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            color: _getEstadoColor(orden.estado).withOpacity(0.2),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(
            Icons.assignment,
            color: _getEstadoColor(orden.estado),
          ),
        ),
        title: Row(
          children: [
            Text(
              '#${orden.idOrden}',
              style: const TextStyle(color: Color(0xFFFF6B00), fontWeight: FontWeight.bold, fontSize: 12),
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Text(
                orden.vehiculoDescripcion,
                style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 4),
            Text(
              orden.descripcion ?? 'Sin descripción',
              style: const TextStyle(color: Colors.white54),
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 8),
            Row(
              children: [
                _buildEstadoChip(orden.estado),
                const SizedBox(width: 8),
                if (orden.fechaEntrada != null)
                  Text(
                    _formatDate(orden.fechaEntrada!),
                    style: const TextStyle(color: Colors.white38, fontSize: 12),
                  ),
              ],
            ),
          ],
        ),
        trailing: const Icon(Icons.arrow_forward_ios, color: Colors.white38, size: 16),
        onTap: () => _showOrdenDetalle(orden),
      ),
    );
  }

/**
   * Muestra el dialogo de detalle de orden.
   */
  void _showOrdenDetalle(OrdenReparacion orden) {
    final descripcionController = TextEditingController(text: orden.descripcion ?? '');
    String estadoSeleccionado = orden.estado;
    final estados = ['PENDIENTE', 'EN_PROCESO', 'TERMINADO'];

    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF2D2D2D),
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (sheetContext) => StatefulBuilder(
        builder: (sheetContext, setSheetState) => DraggableScrollableSheet(
          initialChildSize: 0.7,
          maxChildSize: 0.95,
          minChildSize: 0.5,
          expand: false,
          builder: (sheetContext, scrollController) => SingleChildScrollView(
            controller: scrollController,
            padding: EdgeInsets.only(
              left: 24, right: 24, top: 24,
              bottom: MediaQuery.of(sheetContext).viewInsets.bottom + 24,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Center(
                  child: Container(
                    width: 40, height: 4,
                    decoration: BoxDecoration(
                      color: Colors.white38,
                      borderRadius: BorderRadius.circular(2),
                    ),
                  ),
                ),
                const SizedBox(height: 20),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('Orden #${orden.idOrden}',
                        style: const TextStyle(color: Color(0xFFFF6B00), fontSize: 20, fontWeight: FontWeight.bold)),
                    IconButton(
                      icon: const Icon(Icons.edit, color: Color(0xFFFF6B00)),
                      onPressed: () {
                        Navigator.pop(sheetContext);
                        _showEditarOrdenDialog(orden);
                      },
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                _buildDetailRow('Vehiculo', orden.vehiculoDescripcion),
                const SizedBox(height: 12),
                Text('Estado:', style: const TextStyle(color: Colors.white54, fontSize: 14)),
                const SizedBox(height: 8),
                Wrap(
                  spacing: 8,
                  children: estados.map((e) {
                    final isSelected = estadoSeleccionado == e;
                    return ChoiceChip(
                      label: Text(e.replaceAll('_', ' ')),
                      selected: isSelected,
                      onSelected: (selected) {
                        if (selected) {
                          setSheetState(() => estadoSeleccionado = e);
                        }
                      },
                      selectedColor: const Color(0xFFFF6B00),
                      labelStyle: TextStyle(color: isSelected ? Colors.white : Colors.white70),
                    );
                  }).toList(),
                ),
                const SizedBox(height: 12),
                if (orden.nombreMecanico != null) _buildDetailRow('Mecanico', orden.nombreMecanico!),
                if (orden.fechaEntrada != null) _buildDetailRow('Fecha', _formatDate(orden.fechaEntrada!)),
                const SizedBox(height: 16),
                const Text('DESCRIPCION:', style: TextStyle(color: Colors.white54, fontSize: 12)),
                const SizedBox(height: 8),
                TextField(
                  controller: descripcionController,
                  style: const TextStyle(color: Colors.white),
                  maxLines: 4,
                  decoration: InputDecoration(
                    hintText: 'Escribe la descripcion...',
                    hintStyle: const TextStyle(color: Colors.white38),
                    filled: true,
                    fillColor: const Color(0xFF3D3D3D),
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
                  ),
                ),
                const SizedBox(height: 24),
                Row(
                  children: [
                    if (orden.esPendiente || orden.esEnProceso)
                      Expanded(
                        child: ElevatedButton(
                          onPressed: () async {
                            try {
                              await ApiService().put('/ordenes/${orden.idOrden}', {
                                'descripcion': descripcionController.text,
                                'estado': estadoSeleccionado,
                              });
                              if (mounted) {
                                Navigator.pop(sheetContext);
                                _loadOrdenes();
                                ScaffoldMessenger.of(context).showSnackBar(
                                  const SnackBar(content: Text('Orden actualizada')),
                                );
                              }
                            } catch (e) {
                              ScaffoldMessenger.of(sheetContext).showSnackBar(
                                SnackBar(content: Text('Error: $e')),
                              );
                            }
                          },
                          style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
                          child: const Text('GUARDAR'),
                        ),
                      ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _showEditarOrdenDialog(OrdenReparacion orden) {
    final descripcionController = TextEditingController(text: orden.descripcion ?? '');
    String estadoSeleccionado = orden.estado;
    final estados = ['PENDIENTE', 'EN_PROCESO', 'TERMINADO'];

    showDialog(
      context: context,
      builder: (dialogContext) => StatefulBuilder(
        builder: (dialogContext, setDialogState) => AlertDialog(
          backgroundColor: const Color(0xFF2D2D2D),
          title: Text('Editar Orden #${orden.idOrden}', style: const TextStyle(color: Colors.white)),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Estado:', style: const TextStyle(color: Colors.white54)),
                const SizedBox(height: 8),
                Wrap(
                  spacing: 8,
                  children: estados.map((e) {
                    final isSelected = estadoSeleccionado == e;
                    return ChoiceChip(
                      label: Text(e.replaceAll('_', ' ')),
                      selected: isSelected,
                      onSelected: (selected) {
                        if (selected) setDialogState(() => estadoSeleccionado = e);
                      },
                      selectedColor: const Color(0xFFFF6B00),
                      labelStyle: TextStyle(color: isSelected ? Colors.white : Colors.white70, fontSize: 12),
                    );
                  }).toList(),
                ),
                const SizedBox(height: 16),
                const Text('Descripcion:', style: TextStyle(color: Colors.white54)),
                const SizedBox(height: 8),
                TextField(
                  controller: descripcionController,
                  style: const TextStyle(color: Colors.white),
                  maxLines: 4,
                  decoration: InputDecoration(
                    hintText: 'Descripcion...',
                    hintStyle: const TextStyle(color: Colors.white38),
                    filled: true,
                    fillColor: const Color(0xFF3D3D3D),
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(8), borderSide: BorderSide.none),
                  ),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(dialogContext),
              child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
            ),
            ElevatedButton(
              style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
              onPressed: () async {
                try {
                  await ApiService().put('/ordenes/${orden.idOrden}', {
                    'descripcion': descripcionController.text,
                    'estado': estadoSeleccionado,
                  });
                  if (mounted) {
                    Navigator.pop(dialogContext);
                    _loadOrdenes();
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Orden actualizada correctamente')),
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
      ),
    );
  }

  /**
   * Construye una fila de detalle.
   */
  Widget _buildDetailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: Colors.white54)),
          Text(value, style: const TextStyle(color: Colors.white)),
        ],
      ),
    );
  }

  /**
   * Obtiene el color según el estado.
   */
  Color _getEstadoColor(String estado) {
    switch (estado) {
      case 'Pendiente':
        return Colors.orange;
      case 'En Proceso':
        return Colors.blue;
      case 'A falta de piezas':
        return Colors.purple;
      case 'Terminado':
        return Colors.green;
      default:
        return Colors.grey;
    }
  }

  /**
   * Construye el chip de estado.
   */
  Widget _buildEstadoChip(String estado) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: _getEstadoColor(estado).withOpacity(0.2),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(
        estado,
        style: TextStyle(
          color: _getEstadoColor(estado),
          fontSize: 12,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }

  /**
   * Formatea una fecha.
   */
  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }

  void _showCrearOrden() async {
    final vehiculos = await ApiService().get('/vehiculos');
    final List<dynamic> vehiculosList = vehiculos['data'] ?? [];

    if (!mounted) return;

    showDialog(
      context: context,
      builder: (context) {
        String? selectedMatricula;
        final descController = TextEditingController();
        return StatefulBuilder(
          builder: (context, setDialogState) => AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('CREAR ORDEN', style: TextStyle(color: Colors.white)),
            content: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  DropdownButtonFormField<String>(
                    value: selectedMatricula,
                    dropdownColor: const Color(0xFF2D2D2D),
                    style: const TextStyle(color: Colors.white),
                    decoration: const InputDecoration(
                      labelText: 'Vehiculo', labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                    items: vehiculosList.map<DropdownMenuItem<String>>((v) {
                      return DropdownMenuItem<String>(
                        value: v['matricula'] as String,
                        child: Text('${v['matricula']} - ${v['marca']} ${v['modelo']}', style: const TextStyle(color: Colors.white)),
                      );
                    }).toList(),
                    onChanged: (value) => setDialogState(() => selectedMatricula = value),
                  ),
                  const SizedBox(height: 12),
                  TextField(
                    controller: descController,
                    style: const TextStyle(color: Colors.white),
                    maxLines: 4,
                    decoration: const InputDecoration(
                      labelText: 'Descripcion', labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
              ),
              ElevatedButton(
                style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
                onPressed: () async {
                  if (selectedMatricula == null) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Selecciona un vehiculo')),
                    );
                    return;
                  }
                  final selectedV = vehiculosList.firstWhere((v) => v['matricula'] == selectedMatricula);
                  final vehiculo = Vehiculo(
                    matricula: selectedV['matricula'],
                    marca: selectedV['marca'] ?? '',
                    modelo: selectedV['modelo'] ?? '',
                    idCliente: selectedV['idCliente'],
                  );
                  Navigator.pop(context);
                  await Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => OrdenFormScreen(vehiculo: vehiculo),
                    ),
                  );
                  _loadOrdenes();
                },
                child: const Text('SIGUIENTE'),
              ),
            ],
          ),
        );
      },
    );
  }

  void _showDeleteOrdenDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Eliminar Orden', style: TextStyle(color: Colors.white)),
        content: SizedBox(
          width: double.maxFinite,
          height: 300,
          child: ListView.builder(
            itemCount: _ordenes.length,
            itemBuilder: (context, index) {
              final orden = _ordenes[index];
              return ListTile(
                leading: Icon(Icons.assignment, color: _getEstadoColor(orden.estado)),
                title: Text('Orden #${orden.idOrden}', style: const TextStyle(color: Colors.white)),
                subtitle: Text(orden.vehiculoDescripcion, style: const TextStyle(color: Colors.white54)),
                trailing: const Icon(Icons.delete, color: Colors.red),
                onTap: () {
                  Navigator.pop(ctx);
                  _confirmDeleteOrden(orden);
                },
              );
            },
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('CERRAR', style: TextStyle(color: Colors.white54)),
          ),
        ],
      ),
    );
  }

  void _confirmDeleteOrden(OrdenReparacion orden) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Confirmar', style: TextStyle(color: Colors.white)),
        content: Text(
          '¿Eliminar orden #${orden.idOrden}?',
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
              _deleteOrden(orden.idOrden);
            },
            child: const Text('ELIMINAR'),
          ),
        ],
      ),
    );
  }

  Future<void> _deleteOrden(int idOrden) async {
    try {
      await ApiService().delete('/ordenes/$idOrden');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Orden eliminada')),
        );
        _loadOrdenes();
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