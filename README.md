# GESTION TALLERES 2000 🌟

Hola, soy **Nahid Larhziale** y este es mi proyecto final del ciclo de DAM.

He desarrollado una aplicación móvil para gestionar un taller mecánico, 
donde puedes llevar el control de clientes, vehículos, órdenes de 
reparación y citas. Todo esto con la ayuda de códigos QR para 
identificar vehículos rápidamente.

---

## ¿Qué hay aquí?

### 📱 La app
En la carpeta `frontend/` está todo el código de la aplicación Flutter. 
Si quieres probarla, el archivo `app-release.apk` es el instalable para Android.

### ⚙️ El backend
En `backend/` está la API que hace funcionar todo. Es un servicio en 
Java/Spring Boot. El archivo `gestion-talleres-backend-1.0.0.jar` es el 
ejecutable listo para funcionar.

### 🗄️ La base de datos
En `docker/` está la configuración de MySQL. Solo necesitas Docker y 
arrancar con `docker-compose up -d`.

### 📋 La documentación
El archivo `GT2000_Documentacion_Tecnica.pdf` contiene toda la 
documentación del proyecto: cómo se hizo, cómo funciona, guía de 
usuario, pruebas...

---

## ¿Cómo probar el proyecto?

1. **Arrancar MySQL:** `cd docker` → `docker-compose up -d`
2. **Ejecutar el backend:** `cd backend` → `java -jar target/gestion-talleres-backend-1.0.0.jar`
3. **Instalar la app:** Copia el APK a tu móvil e instálalo

---

## Guía rápida de arranque

### 1. Arrancar MySQL

```powershell
cd docker
docker-compose up -d
```

### 2. Arrancar backend

```powershell
cd backend
mvn spring-boot:run
```

Backend disponible en: http://localhost:8085

### 3. Verificación

Visita: http://localhost:8085/api/auth/health

Debe mostrar:
```json
{"success":true,"message":"API funcionando correctamente","data":"OK"}
```

---

## Usuario de prueba

| Rol | Usuario | Contraseña |
|-----|---------|------------|
| Admin | nahid_admin | Taller2026* |
| Mecánico | carlos_mecanico | Mecanico123* |

---

## ¿Dudas?

Tienes toda la información en el PDF que acompaña este repo. 
Si te interesa el código, échale un vistazo a las carpetas, 
está todo bastante organizado.

¡Espero que te sea útil! 👋