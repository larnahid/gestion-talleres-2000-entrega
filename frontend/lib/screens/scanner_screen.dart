import 'package:flutter/material.dart';
import 'package:mobile_scanner/mobile_scanner.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../services/api_service.dart';
import '../models/vehiculo.dart';
import 'vehiculo_detail_screen.dart';

/**
 * Pantalla del escáner QR para identificar vehículos.
 * Usa la cámara del dispositivo y el paquete mobile_scanner.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class ScannerScreen extends StatefulWidget {
  const ScannerScreen({super.key});

  @override
  State<ScannerScreen> createState() => _ScannerScreenState();
}

class _ScannerScreenState extends State<ScannerScreen> {
  MobileScannerController? _controller;
  bool _isProcessing = false;
  String? _lastScannedCode;

  @override
  void initState() {
    super.initState();
    _controller = MobileScannerController(
      detectionSpeed: DetectionSpeed.normal,
      facing: CameraFacing.back,
    );
  }

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  /**
   * Maneja la detección de un código QR.
   */
  Future<void> _onDetect(BarcodeCapture capture) async {
    if (_isProcessing) return;

    final List<Barcode> barcodes = capture.barcodes;
    if (barcodes.isEmpty) return;

    final String? code = barcodes.first.rawValue;
    if (code == null || code.isEmpty) return;
    if (code == _lastScannedCode) return;

    setState(() {
      _isProcessing = true;
      _lastScannedCode = code;
    });

    // Vibración háptica para confirmar lectura
    //HapticFeedback.mediumImpact();

    await _buscarVehiculo(code);
  }

  /**
   * Busca un vehículo por su matrícula (contenido del QR).
   */
  Future<void> _buscarVehiculo(String matricula) async {
    try {
      final apiService = ApiService();
      final response = await apiService.get('/vehiculos/$matricula');
      final vehiculo = Vehiculo.fromJson(response['data']);

      if (mounted) {
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(
            builder: (_) => VehiculoDetailScreen(vehiculo: vehiculo),
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Vehiculo no encontrado: ${e.toString()}'),
            backgroundColor: Colors.orange,
            action: SnackBarAction(
              label: 'Manual',
              textColor: Colors.white,
              onPressed: () => _showMatriculaManual(),
            ),
          ),
        );

        setState(() {
          _isProcessing = false;
          _lastScannedCode = null;
        });
      }
    }
  }

  /**
   * Permite introducir la matrícula manualmente.
   */
  void _showMatriculaManual() {
    final controller = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Introducir matricula',
            style: TextStyle(color: Colors.white)),
        content: TextField(
          controller: controller,
          style: const TextStyle(color: Colors.white),
          decoration: InputDecoration(
            hintText: 'Ej: 1234-ABC',
            hintStyle: const TextStyle(color: Colors.white38),
            filled: true,
            fillColor: const Color(0xFF1A1A1A),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide.none,
            ),
          ),
          textCapitalization: TextCapitalization.characters,
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              final matricula = controller.text.trim().toUpperCase();
              if (matricula.isNotEmpty) {
                _buscarVehiculo(matricula);
              }
            },
            style: ElevatedButton.styleFrom(
              backgroundColor: const Color(0xFFFF6B00),
            ),
            child: const Text('Buscar'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        title: const Text(
          'TALLER DIGITAL SCAN',
          style: TextStyle(color: Colors.white),
        ),
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Stack(
        children: [
          // Cámara
          MobileScanner(
            controller: _controller,
            onDetect: _onDetect,
          ),
          // Overlay con guía
          _buildScanOverlay(),
          // Instrucciones
          _buildInstructions(),
          // Herramientas inferiores
          _buildBottomTools(),
        ],
      ),
    );
  }

  /**
   * Construye el overlay con el marco de escaneo.
   */
  Widget _buildScanOverlay() {
    return Center(
      child: Container(
        width: 280,
        height: 280,
        decoration: BoxDecoration(
          border: Border.all(
            color: const Color(0xFFFF6B00),
            width: 3,
          ),
          borderRadius: BorderRadius.circular(20),
        ),
        child: const Center(
          child: Text(
            '',
            style: TextStyle(color: Colors.white54),
          ),
        ),
      ),
    );
  }

  /**
   * Construye las instrucciones de uso.
   */
  Widget _buildInstructions() {
    return const Positioned(
      top: 100,
      left: 0,
      right: 0,
      child: Center(
        child: Text(
          'APUNTA AL CODIGO QR',
          style: TextStyle(
            color: Colors.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  /**
   * Construye las herramientas inferiores.
   */
  Widget _buildBottomTools() {
    return Positioned(
      bottom: 40,
      left: 0,
      right: 0,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          // Linterna
          IconButton(
            icon: const Icon(Icons.flash_on, color: Colors.white, size: 30),
            onPressed: () => _controller?.toggleTorch(),
          ),
          // Galería (futuro)
          IconButton(
            icon: const Icon(Icons.photo_library, color: Colors.white, size: 30),
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Funcion en desarrollo')),
              );
            },
          ),
          // Entrada manual
          IconButton(
            icon: const Icon(Icons.keyboard, color: Colors.white, size: 30),
            onPressed: _showMatriculaManual,
          ),
        ],
      ),
    );
  }
}