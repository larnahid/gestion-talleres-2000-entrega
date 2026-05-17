import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../services/api_service.dart';
import '../models/vehiculo.dart';
import '../models/orden_reparacion.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';

/**
 * Pantalla para crear o editar una orden de reparación.
 * Permite escribir diagnóstico, tomar fotos y cambiar estado.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class OrdenFormScreen extends StatefulWidget {
  final Vehiculo vehiculo;
  final OrdenReparacion? orden; // Null para crear, no null para editar

  const OrdenFormScreen({
    super.key,
    required this.vehiculo,
    this.orden,
  });

  @override
  State<OrdenFormScreen> createState() => _OrdenFormScreenState();
}

class _OrdenFormScreenState extends State<OrdenFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _descripcionController = TextEditingController();
  String _estadoSeleccionado = 'Pendiente';
  File? _imagenSeleccionada;
  bool _isLoading = false;

  final List<String> _estados = [
    'Pendiente',
    'En Proceso',
    'A falta de piezas',
    'Terminado',
  ];

  @override
  void initState() {
    super.initState();
    if (widget.orden != null) {
      _descripcionController.text = widget.orden!.descripcion ?? '';
      _estadoSeleccionado = widget.orden!.estado;
    }
  }

  @override
  void dispose() {
    _descripcionController.dispose();
    super.dispose();
  }

  /**
   * Selecciona una imagen de la galería o cámara.
   */
  Future<void> _seleccionarImagen() async {
    final ImagePicker picker = ImagePicker();

    showModalBottomSheet(
      context: context,
      backgroundColor: const Color(0xFF2D2D2D),
      builder: (context) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              leading: const Icon(Icons.camera_alt, color: Color(0xFFFF6B00)),
              title: const Text('Tomar foto', style: TextStyle(color: Colors.white)),
              onTap: () async {
                Navigator.pop(context);
                final XFile? image = await picker.pickImage(source: ImageSource.camera);
                if (image != null) {
                  setState(() => _imagenSeleccionada = File(image.path));
                }
              },
            ),
            ListTile(
              leading: const Icon(Icons.photo, color: Color(0xFFFF6B00)),
              title: const Text('Elegir de galeria', style: TextStyle(color: Colors.white)),
              onTap: () async {
                Navigator.pop(context);
                final XFile? image = await picker.pickImage(source: ImageSource.gallery);
                if (image != null) {
                  setState(() => _imagenSeleccionada = File(image.path));
                }
              },
            ),
          ],
        ),
      ),
    );
  }

  /**
   * Guarda la orden de reparación.
   */
  Future<void> _guardarOrden() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isLoading = true);

    try {
      final authProvider = context.read<AuthProvider>();
      final idUsuario = await authProvider.getIdUsuario();

      final apiService = ApiService();
      final data = {
        'matricula': widget.vehiculo.matricula,
        'descripcion': _descripcionController.text,
        'estado': _estadoSeleccionado,
        'idUsuario': idUsuario,
      };

      await apiService.post('/ordenes', data);

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Orden guardada correctamente'),
            backgroundColor: Colors.green,
          ),
        );
        Navigator.pop(context);
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error al guardar: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text('NUEVA ORDEN', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Form(
        key: _formKey,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildDatosVehiculo(),
              const SizedBox(height: 24),
              _buildFotoSection(),
              const SizedBox(height: 24),
              _buildEstadoSelector(),
              const SizedBox(height: 24),
              _buildDiagnosticoField(),
              const SizedBox(height: 32),
              _buildGuardarButton(),
            ],
          ),
        ),
      ),
    );
  }

  /**
   * Construye la sección de datos del vehículo.
   */
  Widget _buildDatosVehiculo() {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D2D),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            widget.vehiculo.matricula,
            style: const TextStyle(
              color: Color(0xFFFF6B00),
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 8),
          Text(
            '${widget.vehiculo.marca} ${widget.vehiculo.modelo}',
            style: const TextStyle(color: Colors.white70, fontSize: 16),
          ),
        ],
      ),
    );
  }

  /**
   * Construye la sección de selección de foto.
   */
  Widget _buildFotoSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'AÑADIR FOTOGRAFIAS',
          style: TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        InkWell(
          onTap: _seleccionarImagen,
          child: Container(
            width: double.infinity,
            height: 120,
            decoration: BoxDecoration(
              color: const Color(0xFF2D2D2D),
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: Colors.white24),
            ),
            child: _imagenSeleccionada != null
                ? ClipRRect(
                    borderRadius: BorderRadius.circular(12),
                    child: Image.file(_imagenSeleccionada!, fit: BoxFit.cover),
                  )
                : const Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(Icons.camera_alt, color: Color(0xFFFF6B00), size: 40),
                      SizedBox(height: 8),
                      Text(
                        'AÑADIR FOTO',
                        style: TextStyle(color: Colors.white54),
                      ),
                      Text(
                        '(Max. 5 fotos)',
                        style: TextStyle(color: Colors.white38, fontSize: 12),
                      ),
                    ],
                  ),
          ),
        ),
      ],
    );
  }

  /**
   * Construye el selector de estado.
   */
  Widget _buildEstadoSelector() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'ESTADO DE REPARACION',
          style: TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),
        Wrap(
          spacing: 8,
          children: _estados.map((estado) {
            final isSelected = _estadoSeleccionado == estado;
            return ChoiceChip(
              label: Text(estado),
              selected: isSelected,
              onSelected: (selected) {
                if (selected) {
                  setState(() => _estadoSeleccionado = estado);
                }
              },
              selectedColor: const Color(0xFFFF6B00),
              labelStyle: TextStyle(
                color: isSelected ? Colors.white : Colors.white70,
              ),
            );
          }).toList(),
        ),
      ],
    );
  }

  /**
   * Construye el campo de diagnóstico.
   */
  Widget _buildDiagnosticoField() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'DIAGNOSTICO MECANICO',
          style: TextStyle(color: Colors.white, fontSize: 14, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 12),
        TextFormField(
          controller: _descripcionController,
          maxLines: 6,
          style: const TextStyle(color: Colors.white),
          decoration: InputDecoration(
            hintText: 'Escribe el diagnostico tecnico detallado aqui...',
            hintStyle: const TextStyle(color: Colors.white38),
            filled: true,
            fillColor: const Color(0xFF2D2D2D),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide.none,
            ),
          ),
          validator: (value) {
            if (value == null || value.isEmpty) {
              return 'El diagnostico es obligatorio';
            }
            return null;
          },
        ),
      ],
    );
  }

  /**
   * Construye el botón de guardar.
   */
  Widget _buildGuardarButton() {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: _isLoading ? null : _guardarOrden,
        style: ElevatedButton.styleFrom(
          backgroundColor: const Color(0xFFFF6B00),
          foregroundColor: Colors.white,
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
        child: _isLoading
            ? const SizedBox(
                height: 20,
                width: 20,
                child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
              )
            : const Text(
                'GUARDAR ORDEN',
                style: TextStyle(fontWeight: FontWeight.bold, letterSpacing: 1),
              ),
      ),
    );
  }
}