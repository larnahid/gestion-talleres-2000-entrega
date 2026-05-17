import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../models/usuario.dart';
import 'scanner_screen.dart';
import 'ordenes_list_screen.dart';
import 'citas_screen.dart';
import 'clientes_screen.dart';
import 'vehiculos_screen.dart';
import 'login_screen.dart';
import 'ayuda_screen.dart';

/**
 * Pantalla principal Dashboard después del login.
 * Muestra los accesos rápidos según el rol del usuario.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  int _pendientesCount = 0;
  int _citasHoy = 0;

  @override
  void initState() {
    super.initState();
    _loadCounts();
  }

  /**
   * Carga los contadores para mostrar en el dashboard.
   */
  Future<void> _loadCounts() async {
    // Aquí se cargarían los contadores desde la API
    // Por ahora valores de ejemplo
    setState(() {
      _pendientesCount = 5;
      _citasHoy = 3;
    });
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = context.watch<AuthProvider>();
    final usuario = authProvider.usuario;

    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text(
          'TALLER CONNECT',
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold,
            letterSpacing: 1,
          ),
        ),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.help_outline, color: Colors.white70),
            onPressed: () {
              Navigator.of(context).push(
                MaterialPageRoute(builder: (_) => const AyudaScreen()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.logout, color: Colors.white70),
            onPressed: () => _handleLogout(context),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Saludo al usuario
            _buildUserGreeting(usuario),
            const SizedBox(height: 24),
            // Botón principal - Escanear QR
            _buildScanButton(),
            const SizedBox(height: 24),
            // Grid de accesos rápidos
            _buildQuickAccessGrid(usuario),
          ],
        ),
      ),
    );
  }

  /**
   * Construye el saludo personalizado.
   */
  Widget _buildUserGreeting(Usuario? usuario) {
    return Row(
      children: [
        Container(
          width: 50,
          height: 50,
          decoration: BoxDecoration(
            color: const Color(0xFFFF6B00),
            borderRadius: BorderRadius.circular(12),
          ),
          child: const Icon(Icons.person, color: Colors.white),
        ),
        const SizedBox(width: 16),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'TALLER DIGITAL',
              style: TextStyle(
                color: Color(0xFFFF6B00),
                fontSize: 14,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              usuario?.username ?? 'Usuario',
              style: const TextStyle(
                color: Colors.white,
                fontSize: 18,
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ),
      ],
    );
  }

  /**
   * Construye el botón principal de escanear QR.
   */
  Widget _buildScanButton() {
    return InkWell(
      onTap: () {
        Navigator.of(context).push(
          MaterialPageRoute(builder: (_) => const ScannerScreen()),
        );
      },
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.all(24),
        decoration: BoxDecoration(
          color: const Color(0xFFFF6B00),
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          children: [
            Container(
              width: 60,
              height: 60,
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.2),
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Icon(Icons.qr_code_scanner, color: Colors.white, size: 32),
            ),
            const SizedBox(width: 20),
            const Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'ESCANEAR VEHICULO',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(height: 4),
                  Text(
                    'Escanea el codigo QR del vehiculo',
                    style: TextStyle(color: Colors.white70, fontSize: 14),
                  ),
                ],
              ),
            ),
            const Icon(Icons.arrow_forward_ios, color: Colors.white),
          ],
        ),
      ),
    );
  }

  /**
   * Construye el grid de accesos rápidos.
   */
  Widget _buildQuickAccessGrid(Usuario? usuario) {
    final isAdmin = usuario?.esAdministrador ?? false;

    return GridView.count(
      crossAxisCount: 2,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      mainAxisSpacing: 16,
      crossAxisSpacing: 16,
      childAspectRatio: 1.3,
      children: [
        _buildQuickAccessTile(
          icon: Icons.assignment,
          title: 'MIS ORDENES',
          subtitle: '$_pendientesCount Pendientes',
          onTap: () => Navigator.of(context).push(
            MaterialPageRoute(builder: (_) => const OrdenesListScreen()),
          ),
        ),
        if (isAdmin)
          _buildQuickAccessTile(
            icon: Icons.calendar_today,
            title: 'CITAS',
            subtitle: '$_citasHoy Hoy',
            onTap: () => Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const CitasScreen()),
            ),
          ),
        if (isAdmin)
          _buildQuickAccessTile(
            icon: Icons.people,
            title: 'CLIENTES',
            subtitle: 'Gestionar',
            onTap: () => Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const ClientesScreen()),
            ),
          ),
        if (isAdmin)
          _buildQuickAccessTile(
            icon: Icons.directions_car,
            title: 'VEHICULOS',
            subtitle: 'Gestionar',
            onTap: () => Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const VehiculosScreen()),
            ),
          ),
      ],
    );
  }

  /**
   * Construye una celda del grid de accesos rápidos.
   */
  Widget _buildQuickAccessTile({
    required IconData icon,
    required String title,
    required String subtitle,
    required VoidCallback onTap,
  }) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: const Color(0xFF2D2D2D),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, color: const Color(0xFFFF6B00), size: 28),
            const SizedBox(height: 12),
            Text(
              title,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 14,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              subtitle,
              style: const TextStyle(color: Colors.white54, fontSize: 12),
            ),
          ],
        ),
      ),
    );
  }

  /**
   * Maneja el cierre de sesión.
   */
  Future<void> _handleLogout(BuildContext context) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        backgroundColor: const Color(0xFF2D2D2D),
        title: const Text('Cerrar sesion', style: TextStyle(color: Colors.white)),
        content: const Text('¿Estas seguro de que quieres cerrar sesion?',
            style: TextStyle(color: Colors.white70)),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFF6B00)),
            child: const Text('Cerrar sesion'),
          ),
        ],
      ),
    );

    if (confirmed == true && context.mounted) {
      await context.read<AuthProvider>().logout();
      if (context.mounted) {
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(builder: (_) => const LoginScreen()),
        );
      }
    }
  }
}