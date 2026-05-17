import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'providers/auth_provider.dart';
import 'screens/login_screen.dart';
import 'screens/dashboard_screen.dart';
import 'screens/ayuda_screen.dart';
import 'services/auth_service.dart';

const String _keyPrimeraEjecucion = 'primera_ejecucion_ayuda';

/**
 * Punto de entrada de la aplicación GESTION TALLERES 2000.
 * Configura el tema oscuro y los providers globales.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
void main() {
  runApp(const Gt2000App());
}

/**
 * Widget principal de la aplicación.
 */
class Gt2000App extends StatelessWidget {
  const Gt2000App({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
      ],
      child: MaterialApp(
        title: 'GESTION TALLERES 2000',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primaryColor: const Color(0xFFFF6B00),
          brightness: Brightness.dark,
          scaffoldBackgroundColor: const Color(0xFF1A1A1A),
          appBarTheme: const AppBarTheme(
            backgroundColor: Color(0xFF1A1A1A),
            elevation: 0,
          ),
          colorScheme: const ColorScheme.dark(
            primary: Color(0xFFFF6B00),
            secondary: Color(0xFFFF6B00),
            surface: Color(0xFF2D2D2D),
          ),
          elevatedButtonTheme: ElevatedButtonThemeData(
            style: ElevatedButton.styleFrom(
              backgroundColor: const Color(0xFFFF6B00),
              foregroundColor: Colors.white,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
          ),
          inputDecorationTheme: InputDecorationTheme(
            filled: true,
            fillColor: const Color(0xFF2D2D2D),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide.none,
            ),
          ),
        ),
        home: const AppStartup(),
      ),
    );
  }
}

/**
 * Widget que maneja la lógica de inicio de la app.
 * Muestra onboarding en primera ejecución o login/dashboard según estado.
 */
class AppStartup extends StatefulWidget {
  const AppStartup({super.key});

  @override
  State<AppStartup> createState() => _AppStartupState();
}

class _AppStartupState extends State<AppStartup> {
  bool _isInitialized = false;
  bool _mostrarAyuda = false;

  @override
  void initState() {
    super.initState();
    _initialize();
  }

  Future<void> _initialize() async {
    final prefs = await SharedPreferences.getInstance();
    final bool primeraVez = prefs.getBool(_keyPrimeraEjecucion) ?? true;

    if (primeraVez) {
      await prefs.setBool(_keyPrimeraEjecucion, false);
      _mostrarAyuda = true;
    }

    final authProvider = context.read<AuthProvider>();
    await authProvider.init();

    if (mounted) {
      setState(() {
        _isInitialized = true;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (!_isInitialized) {
      return const Scaffold(
        backgroundColor: Color(0xFF1A1A1A),
        body: Center(
          child: CircularProgressIndicator(color: Color(0xFFFF6B00)),
        ),
      );
    }

    if (_mostrarAyuda) {
      return const AyudaScreen();
    }

    return Consumer<AuthProvider>(
      builder: (context, authProvider, _) {
        if (authProvider.isLoggedIn) {
          return const DashboardScreen();
        } else {
          return const LoginScreen();
        }
      },
    );
  }
}

