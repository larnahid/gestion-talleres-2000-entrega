import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'storage_service.dart';

/**
 * Servicio API para comunicación con el backend Spring Boot.
 * Gestiona todas las peticiones HTTP con autenticación JWT.
 *
 * @author Nahid Larhziale Oullada
 * @version 1.0.0
 * @since 2026-05-01
 */
class ApiService {
  // URL base del backend (ajustar segun entorno)
  // En desarrollo Android Studio: http://10.0.2.2:8085/api
  // En emulador: http://localhost:8085/api
  // En dispositivo real: http://IP_DEL_SERVIDOR:8085/api
  static const String baseUrl = 'http://10.0.2.2:8085/api';

  final StorageService _storageService = StorageService();
  final http.Client _client = http.Client();

  /**
   * Headers por defecto con autenticación JWT.
   */
  Future<Map<String, String>> _getHeaders() async {
    final token = await _storageService.getToken();
    return {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      if (token != null) 'Authorization': 'Bearer $token',
    };
  }

  /**
   * Método GET genérico.
   */
  Future<Map<String, dynamic>> get(String endpoint) async {
    try {
      final headers = await _getHeaders();
      final response = await _client
          .get(Uri.parse('$baseUrl$endpoint'), headers: headers)
          .timeout(const Duration(seconds: 30));

      return _handleResponse(response);
    } on SocketException {
      throw ApiException('No hay conexion a internet');
    } catch (e) {
      throw ApiException('Error en la peticion: $e');
    }
  }

  /**
   * Método POST genérico.
   */
  Future<Map<String, dynamic>> post(
    String endpoint,
    Map<String, dynamic> data,
  ) async {
    try {
      final headers = await _getHeaders();
      final response = await _client
          .post(
            Uri.parse('$baseUrl$endpoint'),
            headers: headers,
            body: jsonEncode(data),
          )
          .timeout(const Duration(seconds: 30));

      return _handleResponse(response);
    } on SocketException {
      throw ApiException('No hay conexion a internet');
    } catch (e) {
      throw ApiException('Error en la peticion: $e');
    }
  }

  /**
   * Método PUT genérico.
   */
  Future<Map<String, dynamic>> put(
    String endpoint,
    Map<String, dynamic> data,
  ) async {
    try {
      final headers = await _getHeaders();
      final response = await _client
          .put(
            Uri.parse('$baseUrl$endpoint'),
            headers: headers,
            body: jsonEncode(data),
          )
          .timeout(const Duration(seconds: 30));

      return _handleResponse(response);
    } on SocketException {
      throw ApiException('No hay conexion a internet');
    } catch (e) {
      throw ApiException('Error en la peticion: $e');
    }
  }

  /**
   * Método DELETE genérico.
   */
  Future<Map<String, dynamic>> delete(String endpoint) async {
    try {
      final headers = await _getHeaders();
      final response = await _client
          .delete(Uri.parse('$baseUrl$endpoint'), headers: headers)
          .timeout(const Duration(seconds: 30));

      return _handleResponse(response);
    } on SocketException {
      throw ApiException('No hay conexion a internet');
    } catch (e) {
      throw ApiException('Error en la peticion: $e');
    }
  }

  /**
   * Maneja la respuesta HTTP y convierte a JSON.
   */
  Map<String, dynamic> _handleResponse(http.Response response) {
    final body = jsonDecode(response.body);

    if (response.statusCode >= 200 && response.statusCode < 300) {
      return body;
    } else if (response.statusCode == 401) {
      throw AuthException(body['message'] ?? 'No autenticado');
    } else if (response.statusCode == 403) {
      throw AuthException('No tienes permiso para esta accion');
    } else if (response.statusCode == 404) {
      throw NotFoundException(body['message'] ?? 'Recurso no encontrado');
    } else if (response.statusCode == 409) {
      throw ConflictException(body['message'] ?? 'Conflicto de datos');
    } else {
      throw ApiException(body['message'] ?? 'Error del servidor');
    }
  }

  /**
   * Verifica si el servidor está activo.
   */
  Future<bool> healthCheck() async {
    try {
      final response = await _client
          .get(Uri.parse('$baseUrl/auth/health'))
          .timeout(const Duration(seconds: 10));
      return response.statusCode == 200;
    } catch (e) {
      return false;
    }
  }
}

/**
 * Excepciones personalizadas de la API.
 */
class ApiException implements Exception {
  final String message;
  ApiException(this.message);

  @override
  String toString() => message;
}

class AuthException extends ApiException {
  AuthException(super.message);
}

class NotFoundException extends ApiException {
  NotFoundException(super.message);
}

class ConflictException extends ApiException {
  ConflictException(super.message);
}