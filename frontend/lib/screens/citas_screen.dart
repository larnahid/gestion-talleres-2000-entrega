import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/cita.dart';
import 'package:intl/intl.dart';

/**
 * Pantalla de gestión de citas para administradores.
 * Muestra calendario y permite crear/editar citas.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class CitasScreen extends StatefulWidget {
  const CitasScreen({super.key});

  @override
  State<CitasScreen> createState() => _CitasScreenState();
}

class _CitasScreenState extends State<CitasScreen> {
  DateTime _fechaSeleccionada = DateTime.now();
  List<Cita> _citas = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadCitas();
  }

  /**
   * Carga las citas de la fecha seleccionada.
   */
  Future<void> _loadCitas() async {
    setState(() => _isLoading = true);

    try {
      final apiService = ApiService();
      final fechaStr = DateFormat('yyyy-MM-dd').format(_fechaSeleccionada);
      final response = await apiService.get('/citas/dia/$fechaStr');
      final List<dynamic> data = response['data'];

      setState(() {
        _citas = data.map((json) => Cita.fromJson(json)).toList();
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Error al cargar: $e')),
          );
        }
      });
    }
  }

  void _showDeleteCitaDialog() {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Eliminar Cita', style: TextStyle(color: Colors.white)),
        content: SizedBox(
          width: double.maxFinite,
          height: 300,
          child: ListView.builder(
            itemCount: _citas.length,
            itemBuilder: (context, index) {
              final cita = _citas[index];
              return ListTile(
                leading: const Icon(Icons.calendar_today, color: Color(0xFFFF6B00)),
                title: Text(
                  '${_formatTime(cita.fechaHora)} - ${cita.matricula ?? 'Sin matrícula'}',
                  style: const TextStyle(color: Colors.white),
                ),
                subtitle: Text(cita.motivo ?? 'Sin motivo', style: const TextStyle(color: Colors.white54)),
                trailing: const Icon(Icons.delete, color: Colors.red),
                onTap: () {
                  Navigator.pop(ctx);
                  _confirmDeleteCita(cita);
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

  void _confirmDeleteCita(Cita cita) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Confirmar', style: TextStyle(color: Colors.white)),
        content: Text(
          '¿Eliminar cita del ${_formatDate(cita.fechaHora)}?',
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
              _deleteCita(cita.idCita);
            },
            child: const Text('ELIMINAR'),
          ),
        ],
      ),
    );
  }

  Future<void> _deleteCita(int idCita) async {
    try {
      await ApiService().delete('/citas/$idCita');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Cita eliminada')),
        );
        _loadCitas();
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: $e')),
        );
      }
    }
  }

  String _formatTime(DateTime dt) {
    return '${dt.hour.toString().padLeft(2, '0')}:${dt.minute.toString().padLeft(2, '0')}';
  }

  String _formatDate(DateTime dt) {
    return '${dt.day}/${dt.month}/${dt.year}';
  }

  /**
   * Navega al día anterior.
   */
  void _diaAnterior() {
    setState(() {
      _fechaSeleccionada = _fechaSeleccionada.subtract(const Duration(days: 1));
    });
    _loadCitas();
  }

  /**
   * Navega al día siguiente.
   */
  void _diaSiguiente() {
    setState(() {
      _fechaSeleccionada = _fechaSeleccionada.add(const Duration(days: 1));
    });
    _loadCitas();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text('CALENDARIO', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: _showCrearCita,
          ),
          if (_citas.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: _showDeleteCitaDialog,
            ),
        ],
      ),
      body: Column(
        children: [
          _buildDateSelector(),
          const Divider(color: Colors.white24),
          Expanded(child: _buildCitasList()),
        ],
      ),
    );
  }

  /**
   * Construye el selector de fecha.
   */
  Widget _buildDateSelector() {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          IconButton(
            icon: const Icon(Icons.chevron_left, color: Colors.white),
            onPressed: _diaAnterior,
          ),
          Column(
            children: [
              Text(
                DateFormat('EEEE').format(_fechaSeleccionada),
                style: const TextStyle(color: Colors.white54, fontSize: 14),
              ),
              Text(
                DateFormat('dd MMM yyyy').format(_fechaSeleccionada),
                style: const TextStyle(
                  color: Color(0xFFFF6B00),
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
          IconButton(
            icon: const Icon(Icons.chevron_right, color: Colors.white),
            onPressed: _diaSiguiente,
          ),
        ],
      ),
    );
  }

  /**
   * Construye la lista de citas del día.
   */
  Widget _buildCitasList() {
    if (_isLoading) {
      return const Center(
        child: CircularProgressIndicator(color: Color(0xFFFF6B00)),
      );
    }

    if (_citas.isEmpty) {
      return const Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.event_available, color: Colors.white38, size: 64),
            SizedBox(height: 16),
            Text(
              'No hay citas este dia',
              style: TextStyle(color: Colors.white54, fontSize: 18),
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: _citas.length,
      itemBuilder: (context, index) {
        final cita = _citas[index];
        return _buildCitaCard(cita);
      },
    );
  }

  /**
   * Construye una tarjeta de cita.
   */
  Widget _buildCitaCard(Cita cita) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D2D),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: cita.esPasada ? Colors.white24 : const Color(0xFFFF6B00),
          width: 1,
        ),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(16),
        leading: Container(
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            color: const Color(0xFFFF6B00).withOpacity(0.2),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                DateFormat('HH').format(cita.fechaHora),
                style: const TextStyle(
                  color: Color(0xFFFF6B00),
                  fontWeight: FontWeight.bold,
                  fontSize: 18,
                ),
              ),
              Text(
                DateFormat('mm').format(cita.fechaHora),
                style: const TextStyle(color: Color(0xFFFF6B00), fontSize: 12),
              ),
            ],
          ),
        ),
        title: Text(
          cita.vehiculoDescripcion,
          style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 4),
            Text(
              cita.motivo ?? '',
              style: const TextStyle(color: Colors.white54),
            ),
            const SizedBox(height: 4),
            Text(
              'Matricula: ${cita.matricula}',
              style: const TextStyle(color: Colors.white38, fontSize: 12),
            ),
          ],
),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(
              icon: const Icon(Icons.edit_outlined, color: Colors.white54),
              onPressed: () => _showEditarCita(cita),
            ),
            IconButton(
              icon: const Icon(Icons.delete_outline, color: Colors.white38),
              onPressed: () => _eliminarCita(cita),
            ),
          ],
        ),
      ),
    );
  }

  void _showEditarCita(Cita cita) async {
    final vehiculos = await ApiService().get('/vehiculos');
    final List<dynamic> vehiculosList = vehiculos['data'] ?? [];

    if (!mounted) return;

    showDialog(
      context: context,
      builder: (dialogContext) {
        String? selectedMatricula = cita.matricula;
        DateTime selectedDate = cita.fechaHora;
        TimeOfDay selectedTime = TimeOfDay.fromDateTime(cita.fechaHora);
        final motivoController = TextEditingController(text: cita.motivo ?? '');

        return StatefulBuilder(
          builder: (dialogContext, setDialogState) => AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('EDITAR CITA', style: TextStyle(color: Colors.white)),
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
                    controller: motivoController,
                    style: const TextStyle(color: Colors.white),
                    decoration: const InputDecoration(
                      labelText: 'Motivo', labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                  ),
                  const SizedBox(height: 12),
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Fecha', style: TextStyle(color: Colors.white54)),
                    subtitle: Text(
                      '${selectedDate.day}/${selectedDate.month}/${selectedDate.year}',
                      style: const TextStyle(color: Colors.white),
                    ),
                    trailing: const Icon(Icons.calendar_today, color: Color(0xFFFF6B00)),
                    onTap: () async {
                      final date = await showDatePicker(
                        context: dialogContext,
                        initialDate: selectedDate,
                        firstDate: DateTime.now(),
                        lastDate: DateTime.now().add(const Duration(days: 365)),
                      );
                      if (date != null) {
                        setDialogState(() => selectedDate = date);
                      }
                    },
                  ),
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Hora', style: TextStyle(color: Colors.white54)),
                    subtitle: Text(selectedTime.format(dialogContext), style: const TextStyle(color: Colors.white)),
                    trailing: const Icon(Icons.access_time, color: Color(0xFFFF6B00)),
                    onTap: () async {
                      final time = await showTimePicker(context: dialogContext, initialTime: selectedTime);
                      if (time != null) {
                        setDialogState(() => selectedTime = time);
                      }
                    },
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
                  if (selectedMatricula == null || motivoController.text.isEmpty) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      const SnackBar(content: Text('Selecciona vehiculo y motivo')),
                    );
                    return;
                  }
                  final fechaHora = DateTime(
                    selectedDate.year,
                    selectedDate.month,
                    selectedDate.day,
                    selectedTime.hour,
                    selectedTime.minute,
                  );
                  try {
                    await ApiService().put('/citas/${cita.idCita}', {
                      'fechaHora': fechaHora.toIso8601String(),
                      'motivo': motivoController.text,
                      'matricula': selectedMatricula,
                    });
                    if (mounted) {
                      Navigator.pop(dialogContext);
                      _loadCitas();
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Cita actualizada correctamente')),
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
      },
    );
  }

  /**
   * Muestra el dialogo para crear una nueva cita.
   */
  void _showCrearCita() async {
    final vehiculos = await ApiService().get('/vehiculos');
    final List<dynamic> vehiculosList = vehiculos['data'] ?? [];

    if (!mounted) return;

    showDialog(
      context: context,
      builder: (dialogContext) {
        String? selectedMatricula;
        DateTime selectedDate = DateTime.now();
        TimeOfDay selectedTime = const TimeOfDay(hour: 9, minute: 0);
        final motivoController = TextEditingController();

        return StatefulBuilder(
          builder: (dialogContext, setDialogState) => AlertDialog(
            backgroundColor: const Color(0xFF2D2D2D),
            title: const Text('NUEVA CITA', style: TextStyle(color: Colors.white)),
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
                    controller: motivoController,
                    style: const TextStyle(color: Colors.white),
                    decoration: const InputDecoration(
                      labelText: 'Motivo', labelStyle: TextStyle(color: Colors.white54),
                      enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white54)),
                      focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Color(0xFFFF6B00))),
                    ),
                  ),
                  const SizedBox(height: 12),
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Fecha', style: TextStyle(color: Colors.white54)),
                    subtitle: Text(
                      '${selectedDate.day}/${selectedDate.month}/${selectedDate.year}',
                      style: const TextStyle(color: Colors.white),
                    ),
                    trailing: const Icon(Icons.calendar_today, color: Color(0xFFFF6B00)),
                    onTap: () async {
                      final date = await showDatePicker(
                        context: dialogContext,
                        initialDate: selectedDate,
                        firstDate: DateTime.now(),
                        lastDate: DateTime.now().add(const Duration(days: 365)),
                      );
                      if (date != null) {
                        setDialogState(() => selectedDate = date);
                      }
                    },
                  ),
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Hora', style: TextStyle(color: Colors.white54)),
                    subtitle: Text(selectedTime.format(dialogContext), style: const TextStyle(color: Colors.white)),
                    trailing: const Icon(Icons.access_time, color: Color(0xFFFF6B00)),
                    onTap: () async {
                      final time = await showTimePicker(context: dialogContext, initialTime: selectedTime);
                      if (time != null) {
                        setDialogState(() => selectedTime = time);
                      }
                    },
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
                  if (selectedMatricula == null || motivoController.text.isEmpty) {
                    ScaffoldMessenger.of(dialogContext).showSnackBar(
                      const SnackBar(content: Text('Selecciona vehiculo y motivo')),
                    );
                    return;
                  }
                  final fechaHora = DateTime(
                    selectedDate.year,
                    selectedDate.month,
                    selectedDate.day,
                    selectedTime.hour,
                    selectedTime.minute,
                  );
                  try {
                    await ApiService().post('/citas', {
                      'fechaHora': fechaHora.toIso8601String(),
                      'motivo': motivoController.text,
                      'matricula': selectedMatricula,
                    });
                    if (mounted) {
                      Navigator.pop(dialogContext);
                      _loadCitas();
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Cita creada correctamente')),
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

  /**
   * Elimina una cita.
   */
  Future<void> _eliminarCita(Cita cita) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Eliminar cita', style: TextStyle(color: Colors.white)),
        content: const Text('¿Estas seguro de eliminar esta cita?',
            style: TextStyle(color: Colors.white70)),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx, false),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(ctx, true),
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Eliminar'),
          ),
        ],
      ),
    );

    if (confirm == true) {
      try {
        final apiService = ApiService();
        await apiService.delete('/citas/${cita.idCita}');
        _loadCitas();
      } catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Error: $e')),
          );
        }
      }
    }
  }
}