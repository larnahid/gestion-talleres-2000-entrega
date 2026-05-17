import 'package:flutter/material.dart';

/**
 * Pantalla de Sistema de Ayuda en línea.
 * Accesible desde el menú de la app.
 * Se muestra en primera ejecución de la app.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class AyudaScreen extends StatelessWidget {
  const AyudaScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF1A1A1A),
      appBar: AppBar(
        backgroundColor: const Color(0xFF1A1A1A),
        title: const Text('AYUDA', style: TextStyle(color: Colors.white)),
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSeccion(' Guia Rapida ', Icons.speed, [
              _buildItem('Escanear QR', 'Pulsa el botón naranja grande para escanear'),
              _buildItem('Ver ordenes', 'Accede desde el Dashboard a "Mis Ordenes"'),
              _buildItem('Crear orden', 'Desde la ficha del vehículo, pulsa "Nueva Orden"'),
              _buildItem('Actualizar estado', 'Pulsa sobre una orden y cambia el estado'),
            ]),
            const SizedBox(height: 16),
            _buildSeccion(' Permisos de la App ', Icons.security, [
              _buildItem('Camara', 'Para escanear codigos QR del vehiculo'),
              _buildItem('Internet', 'Para comunicarse con el servidor'),
              _buildItem('Almacenamiento', 'Para guardar fotos de reparaciones'),
            ]),
            const SizedBox(height: 16),
            _buildSeccion(' Preguntas Frecuentes ', Icons.help_outline, [
              _buildFaq('¿Como empiezo?',
                  'Inicia sesion con tu usuario y contrasena. Si no tienes cuenta, contacta al administrador.'),
              _buildFaq('¿Que hago si el QR no funciona?',
                  'Pulsa el icono del teclado e introduce la matricula manualmente.'),
              _buildFaq('¿Puedo usar la app sin internet?',
                  'No, necesitas conexion para comunicarte con el servidor.'),
              _buildFaq('¿Donde se guardan las fotos?',
                  'Las fotos se almacenan en el servidor, no en tu telefono.'),
              _buildFaq('¿Quien puede ver todas las ordenes?',
                  'Solo los administradores ven todas. Los mecanicos ven solo sus ordenes.'),
            ]),
            const SizedBox(height: 16),
            _buildSeccion(' Contacto ', Icons.contact_phone, [
              _buildItem('Email', 'soporte@gt2000.local'),
              _buildItem('Telefono', '600 XXX XXX'),
              _buildItem('Horario', 'L-V 9:00 - 18:00'),
            ]),
          ],
        ),
      ),
    );
  }

  /**
   * Construye una sección con título e iconos.
   */
  Widget _buildSeccion(String titulo, IconData icono, List<Widget> children) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: const Color(0xFF2D2D2D),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icono, color: const Color(0xFFFF6B00), size: 20),
              const SizedBox(width: 8),
              Text(
                titulo,
                style: const TextStyle(
                  color: Color(0xFFFF6B00),
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          ...children,
        ],
      ),
    );
  }

  /**
   * Construye un item simple.
   */
  Widget _buildItem(String titulo, String descripcion) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('• ', style: TextStyle(color: Colors.white54)),
          Expanded(
            child: RichText(
              text: TextSpan(
                style: const TextStyle(color: Colors.white),
                children: [
                  TextSpan(
                    text: '$titulo: ',
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  TextSpan(text: descripcion),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  /**
   * Construye un FAQ expandible.
   */
  Widget _buildFaq(String pregunta, String respuesta) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'P: $pregunta',
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 2),
          Text(
            'R: $respuesta',
            style: const TextStyle(color: Colors.white54),
          ),
        ],
      ),
    );
  }
}