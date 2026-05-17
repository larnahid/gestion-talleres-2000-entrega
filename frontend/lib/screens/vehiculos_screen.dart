import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/vehiculo.dart';
import 'vehiculo_detail_screen.dart';
import 'qr_scanner_screen.dart';

/**
 * Pantalla de gestión de vehículos para administradores.
 * Permite crear, editar y buscar vehículos.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class VehiculosScreen extends StatefulWidget {
  const VehiculosScreen({super.key});

  @override
  State<VehiculosScreen> createState() => _VehiculosScreenState();
}

class _VehiculosScreenState extends State<VehiculosScreen> {
  List<Vehiculo> _vehiculos = [];
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
      final response = await apiService.get('/vehiculos');
      final List<dynamic> data = response['data'];
      setState(() {
        _vehiculos = data.map((json) => Vehiculo.fromJson(json)).toList();
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

  void _showQRScanner() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => const QRScannerScreen()));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text('VEHICULOS', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            icon: const Icon(Icons.qr_code_scanner, color: Color(0xFFFF6B00)),
            onPressed: _showQRScanner,
          ),
          IconButton(icon: const Icon(Icons.add), onPressed: _showCrearVehiculo),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator(color: Color(0xFFFF6B00)))
          : _vehiculos.isEmpty
              ? const Center(
                  child: Text('No hay vehiculos', style: TextStyle(color: Colors.white54)),
                )
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _vehiculos.length,
                  itemBuilder: (context, index) {
                    final vehiculo = _vehiculos[index];
                    return _buildVehiculoCard(vehiculo);
                  },
                ),
    );
  }

  Widget _buildVehiculoCard(Vehiculo vehiculo) {
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
          child: Icon(Icons.directions_car, color: Colors.white),
        ),
        title: Text(
          vehiculo.matricula,
          style: const TextStyle(color: Color(0xFFFF6B00), fontWeight: FontWeight.bold),
        ),
        subtitle: Text(
          '${vehiculo.marca} ${vehiculo.modelo} • ${vehiculo.nombreCliente ?? 'Sin cliente'}',
          style: const TextStyle(color: Colors.white54),
        ),
        trailing: const Icon(Icons.arrow_forward_ios, color: Colors.white38, size: 16),
        onTap: () async {
          final result = await Navigator.push(context, MaterialPageRoute(
            builder: (_) => VehiculoDetailScreen(vehiculo: vehiculo),
          ));
          if (result == true) {
            _loadVehiculos();
          }
        },
      ),
    );
  }

  void _showCrearVehiculo() async {
    final clientesData = await ApiService().get('/clientes');
    final List<dynamic> clientes = clientesData['data'] ?? [];

    if (!mounted) return;

    final marcasCoches = [
      'Alfa Romeo', 'Aston Martin', 'Audi', 'Bentley', 'BMW', 'Bugatti',
      'Cadillac', 'Chevrolet', 'Chrysler', 'Citroen', 'Cupra', 'Dacia',
      'Dodge', 'Ferrari', 'Fiat', 'Ford', 'Honda', 'Hyundai', 'Infiniti',
      'Jaguar', 'Jeep', 'Kia', 'Lamborghini', 'Lancia', 'Land Rover',
      'Lexus', 'Maserati', 'Mazda', 'Mercedes-Benz', 'Mini', 'Mitsubishi',
      'Nissan', 'Opel', 'Peugeot', 'Porsche', 'Renault', 'Rolls-Royce',
      'Seat', 'Skoda', 'Subaru', 'Suzuki', 'Tesla', 'Toyota', 'Volkswagen', 'Volvo'
    ];

    showDialog(
      context: context,
      builder: (dialogContext) {
        final matriculaController = TextEditingController();
        String? selectedMarca;
        final modeloController = TextEditingController();
        int? selectedClienteId;

        return StatefulBuilder(
          builder: (dialogContext, setDialogState) => AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('NUEVO VEHICULO', style: TextStyle(color: Colors.white)),
            content: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextField(
                    controller: matriculaController,
                    style: const TextStyle(color: Colors.white),
                    textCapitalization: TextCapitalization.characters,
                    decoration: const InputDecoration(
                      labelText: 'Matricula', hintText: '1234ABC / 1234-ABC', hintStyle: TextStyle(color: Colors.white24),
                      labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                  ),
                  const SizedBox(height: 12),
                  DropdownButtonFormField<String>(
                    value: selectedMarca,
                    dropdownColor: const Color(0xFF2D2D2D),
                    style: const TextStyle(color: Colors.white),
                    decoration: const InputDecoration(
                      labelText: 'Marca', labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                    items: marcasCoches.map<DropdownMenuItem<String>>((m) {
                      return DropdownMenuItem<String>(value: m, child: Text(m, style: const TextStyle(color: Colors.white)));
                    }).toList(),
                    onChanged: (value) => setDialogState(() => selectedMarca = value),
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
                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          selectedClienteId != null
                            ? (clientes.firstWhere((c) => c['idCliente'] == selectedClienteId, orElse: () => {'nombre': ''})['nombre'] ?? '')
                            : 'Cliente opcional - pulsa lupa para buscar',
                          style: TextStyle(
                            color: selectedClienteId != null ? Colors.white : Colors.white54,
                            fontSize: 14,
                          ),
                        ),
                      ),
                      IconButton(
                        icon: const Icon(Icons.person_search, color: Color(0xFFFF6B00)),
                        onPressed: () => _showClienteSelectorDialog(context, clientes, (clienteId) {
                          setState(() {
                            selectedClienteId = clienteId;
                          });
                        }),
                      ),
                      if (selectedClienteId != null)
                        IconButton(
                          icon: const Icon(Icons.close, color: Colors.white38, size: 18),
                          onPressed: () => setState(() {
                            selectedClienteId = null;
                          }),
                        ),
                    ],
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
                  final matricula = matriculaController.text.trim().toUpperCase().replaceAll('-', '');
                  if (matricula.isEmpty || selectedMarca == null || modeloController.text.isEmpty) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      const SnackBar(content: Text('Matricula, Marca y Modelo son obligatorios')),
                    );
                    return;
                  }
                  final matriculaRegex = RegExp(r'^[A-Z0-9]{4}[A-Z]{2,3}(M)?$|^[A-Z]{3}[A-Z0-9]{3,4}([A-Z])?$');
                  if (!matriculaRegex.hasMatch(matricula)) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      const SnackBar(content: Text('Formato matricula invalido')),
                    );
                    return;
                  }
                  String formattedMatricula = matricula;
                  if (matricula.length == 7 && RegExp(r'^[A-Z0-9]{4}[A-Z]{3}$').hasMatch(matricula)) {
                    formattedMatricula = '${matricula.substring(0,4)}-${matricula.substring(4)}';
                  } else if (matricula.length == 8 && matricula.endsWith('M')) {
                    formattedMatricula = '${matricula.substring(0,4)}-${matricula.substring(4,7)}-${matricula.substring(7)}';
                  }
                  try {
                    await ApiService().post('/vehiculos', {
                      'matricula': formattedMatricula,
                      'marca': selectedMarca,
                      'modelo': modeloController.text.trim(),
                      'idCliente': selectedClienteId,
                    });
                    if (mounted) {
                      Navigator.pop(dialogContext);
                      _loadVehiculos();
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Vehiculo creado correctamente')),
                      );
                    }
                  } catch (e) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      SnackBar(content: Text('Error: $e')),
                    );
                  }
                },
                child: const Text('CREAR'),
              ),
            ],
          ),
        );
      },
    );
  }

  void _showClienteSelectorDialog(BuildContext parentContext, List<dynamic> clientes, Function(int) onSelect) {
    final searchController = TextEditingController();
    List<dynamic> filteredClientes = List.from(clientes);

    showDialog(
      context: parentContext,
      builder: (dialogContext) => StatefulBuilder(
        builder: (dialogContext, setDialogState) {
          return AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('SELECCIONAR CLIENTE', style: TextStyle(color: Colors.white)),
            content: SizedBox(
              width: double.maxFinite,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextField(
                    controller: searchController,
                    style: const TextStyle(color: Colors.white),
                    decoration: const InputDecoration(
                      labelText: 'Buscar', hintText: 'Nombre o DNI...', hintStyle: TextStyle(color: Colors.white24),
                      labelStyle: TextStyle(color: Colors.white54),
                      prefixIcon: Icon(Icons.search, color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                    onChanged: (value) {
                      setDialogState(() {
                        if (value.isEmpty) {
                          filteredClientes = clientes;
                        } else {
                          filteredClientes = clientes.where((c) {
                            final nombre = (c['nombre'] ?? '').toString().toLowerCase();
                            final dni = (c['dni'] ?? '').toString().toLowerCase();
                            return nombre.contains(value.toLowerCase()) || dni.contains(value.toLowerCase());
                          }).toList();
                        }
                      });
                    },
                  ),
                  const SizedBox(height: 12),
                  Flexible(
                    child: ListView.builder(
                      shrinkWrap: true,
                      itemCount: filteredClientes.length,
                      itemBuilder: (_, i) {
                        final c = filteredClientes[i];
                        return ListTile(
                          title: Text('${c['nombre']}', style: const TextStyle(color: Colors.white)),
                          subtitle: Text('${c['dni']}', style: const TextStyle(color: Colors.white54)),
                          onTap: () {
                            Navigator.pop(dialogContext);
                            onSelect(c['idCliente']);
                          },
                        );
                      },
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
            ],
          );
        },
      ),
    );
  }
}