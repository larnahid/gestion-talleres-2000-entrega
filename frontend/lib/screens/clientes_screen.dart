import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/cliente.dart';
import '../models/vehiculo.dart';
import 'vehiculos_screen.dart';
import 'vehiculo_detail_screen.dart';

/**
 * Pantalla de gestion de clientes para administradores.
 * Permite ver y buscar clientes.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class ClientesScreen extends StatefulWidget {
  const ClientesScreen({super.key});

  @override
  State<ClientesScreen> createState() => _ClientesScreenState();
}

class _ClientesScreenState extends State<ClientesScreen> {
  List<Cliente> _clientes = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadClientes();
  }

  Future<void> _loadClientes() async {
    setState(() => _isLoading = true);
    try {
      final apiService = ApiService();
      final response = await apiService.get('/clientes');
      final List<dynamic> data = response['data'];
      setState(() {
        _clientes = data.map((json) => Cliente.fromDto(json)).toList();
        _isLoading = false;
      });
    } catch (e) {
      setState(() => _isLoading = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: $e')),
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
        title: const Text('CLIENTES', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            icon: const Icon(Icons.add, color: Color(0xFFFF6B00)),
            onPressed: () => _showAddClienteDialog(),
          ),
          if (_clientes.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: () => _showDeleteClienteDialog(),
            ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator(color: Color(0xFFFF6B00)))
          : _clientes.isEmpty
              ? const Center(
                  child: Text('No hay clientes', style: TextStyle(color: Colors.white54)),
                )
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _clientes.length,
                  itemBuilder: (context, index) {
                    final cliente = _clientes[index];
                    return _buildClienteCard(cliente);
                  },
                ),
    );
  }

  Widget _buildClienteCard(Cliente cliente) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D2D),
        borderRadius: BorderRadius.circular(12),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(16),
        leading: const CircleAvatar(
          backgroundColor: Color(0xFFFF6B00),
          child: Icon(Icons.person, color: Colors.white),
        ),
        title: Text(cliente.nombre, style: const TextStyle(color: Colors.white)),
        subtitle: Text('${cliente.dni} • ${cliente.telefono ?? 'Sin telefono'}',
            style: const TextStyle(color: Colors.white54)),
        trailing: const Icon(Icons.arrow_forward_ios, color: Colors.white38, size: 16),
        onTap: () {
          _showClienteDetail(cliente);
        },
      ),
    );
  }

  void _showClienteDetail(Cliente cliente) {
    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF2D2D2D),
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) {
        return Padding(
          padding: EdgeInsets.only(
            left: 24, right: 24, top: 24,
            bottom: MediaQuery.of(context).viewInsets.bottom + 24,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
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
                  const Text('DETALLE DEL CLIENTE',
                      style: TextStyle(color: Color(0xFFFF6B00), fontSize: 18, fontWeight: FontWeight.bold)),
                  IconButton(
                    icon: const Icon(Icons.edit, color: Color(0xFFFF6B00)),
                    onPressed: () {
                      Navigator.pop(context);
                      _showEditClienteDialog(cliente);
                    },
                  ),
                ],
              ),
              const SizedBox(height: 20),
              _detailRow('DNI', cliente.dni),
              _detailRow('Nombre', cliente.nombre),
              _detailRow('Telefono', cliente.telefono ?? 'No registrado'),
              const SizedBox(height: 20),
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.pop(context);
                        Navigator.push(context, MaterialPageRoute(
                          builder: (_) => VehiculoPorClienteScreen(idCliente: cliente.idCliente, nombreCliente: cliente.nombre),
                        ));
                      },
                      style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
                      child: const Text('VER VEHICULOS'),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 10),
            ],
          ),
        );
      },
    );
  }

  void _showEditClienteDialog(Cliente cliente) {
    final nombreController = TextEditingController(text: cliente.nombre);
    final telefonoController = TextEditingController(text: cliente.telefono ?? '');

    showDialog(
      context: context,
      builder: (dialogContext) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('EDITAR CLIENTE', style: TextStyle(color: Colors.white)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
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
                  _loadClientes();
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Cliente actualizado correctamente')),
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

  Widget _detailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 90,
            child: Text(label, style: const TextStyle(color: Colors.white54, fontSize: 14)),
          ),
          Expanded(
            child: Text(value, style: const TextStyle(color: Colors.white, fontSize: 14)),
          ),
        ],
      ),
    );
  }

  void _showAddClienteDialog() {
    final dniController = TextEditingController();
    final nombreController = TextEditingController();
    final telefonoController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('NUEVO CLIENTE', style: TextStyle(color: Colors.white)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: dniController,
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                labelText: 'DNI', labelStyle: TextStyle(color: Colors.white54),
                enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
              ),
            ),
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
            onPressed: () => Navigator.pop(context),
            child: const Text('CANCELAR', style: TextStyle(color: Colors.white54)),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
            onPressed: () async {
              if (dniController.text.isEmpty || nombreController.text.isEmpty) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('DNI y Nombre son obligatorios')),
                );
                return;
              }
              try {
                final apiService = ApiService();
                await apiService.post('/clientes', {
                  'dni': dniController.text,
                  'nombre': nombreController.text,
                  'telefono': telefonoController.text.isEmpty ? null : telefonoController.text,
                });
                if (mounted) {
                  Navigator.pop(context);
                  _loadClientes();
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Cliente creado correctamente')),
                  );
                }
              } catch (e) {
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Error: $e')),
                );
              }
            },
            child: const Text('CREAR'),
          ),
        ],
      ),
    );
  }

  void _showDeleteClienteDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Eliminar Cliente', style: TextStyle(color: Colors.white)),
        content: SizedBox(
          width: double.maxFinite,
          height: 300,
          child: ListView.builder(
            itemCount: _clientes.length,
            itemBuilder: (context, index) {
              final cliente = _clientes[index];
              return ListTile(
                leading: const CircleAvatar(
                  backgroundColor: Color(0xFFFF6B00),
                  child: Icon(Icons.person, color: Colors.white),
                ),
                title: Text(cliente.nombre, style: const TextStyle(color: Colors.white)),
                subtitle: Text(cliente.dni, style: const TextStyle(color: Colors.white54)),
                trailing: const Icon(Icons.delete, color: Colors.red),
                onTap: () {
                  Navigator.pop(ctx);
                  _confirmDeleteCliente(cliente);
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

  void _confirmDeleteCliente(Cliente cliente) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Confirmar', style: TextStyle(color: Colors.white)),
        content: Text(
          '¿Eliminar cliente ${cliente.nombre}?\n\nSe eliminarán en cascada todos sus vehículos, órdenes y citas.',
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
              _deleteCliente(cliente.idCliente);
            },
            child: const Text('ELIMINAR'),
          ),
        ],
      ),
    );
  }

  Future<void> _deleteCliente(int idCliente) async {
    try {
      await ApiService().delete('/clientes/$idCliente');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Cliente eliminado')),
        );
        _loadClientes();
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

class VehiculoPorClienteScreen extends StatefulWidget {
  final int idCliente;
  final String nombreCliente;
  const VehiculoPorClienteScreen({super.key, required this.idCliente, required this.nombreCliente});

  @override
  State<VehiculoPorClienteScreen> createState() => _VehiculoPorClienteScreenState();
}

class _VehiculoPorClienteScreenState extends State<VehiculoPorClienteScreen> {
  List _vehiculos = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadVehiculos();
  }

  Future<void> _loadVehiculos() async {
    setState(() => _isLoading = true);
    try {
      final apiService = ApiService();
      final response = await apiService.get('/vehiculos/cliente/${widget.idCliente}');
      final List<dynamic> data = response['data'] ?? [];
      setState(() {
        _vehiculos = data;
        _isLoading = false;
      });
    } catch (e) {
      setState(() => _isLoading = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: $e')),
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
        title: Text('Vehículos de ${widget.nombreCliente}',
            style: const TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator(color: Color(0xFFFF6B00)))
          : _vehiculos.isEmpty
              ? const Center(child: Text('No hay vehiculos', style: TextStyle(color: Colors.white54)))
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _vehiculos.length,
                  itemBuilder: (context, index) {
                    final v = _vehiculos[index];
                    final vehiculo = Vehiculo(
                      matricula: v['matricula'] ?? '',
                      marca: v['marca'] ?? '',
                      modelo: v['modelo'] ?? '',
                      idCliente: v['idCliente'],
                      nombreCliente: v['nombreCliente'],
                    );
                    return Container(
                      margin: const EdgeInsets.only(bottom: 12),
                      decoration: BoxDecoration(
                        color: const Color(0xFF2D2D2D),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: ListTile(
                        contentPadding: const EdgeInsets.all(16),
                        title: Text(v['matricula'] ?? '', style: const TextStyle(color: Color(0xFFFF6B00), fontWeight: FontWeight.bold)),
                        subtitle: Text('${v['marca'] ?? ''} ${v['modelo'] ?? ''}', style: const TextStyle(color: Colors.white54)),
                        trailing: const Icon(Icons.arrow_forward_ios, color: Colors.white38, size: 16),
                        onTap: () async {
                          final result = await Navigator.push(context, MaterialPageRoute(
                            builder: (_) => VehiculoDetailScreen(vehiculo: vehiculo),
                          ));
                          if (result == true && mounted) {
                            _loadVehiculos();
                          }
                        },
                      ),
                    );
                  },
                ),
    );
  }
}