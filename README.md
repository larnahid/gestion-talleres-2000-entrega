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

## Requisitos previos

### Software necesario

- **Docker Desktop** - Para la base de datos MySQL
- **Java JDK 17+** - Para ejecutar el backend
- **Maven** - Para gestionar dependencias del backend
- **Flutter SDK** - Para la aplicación móvil
- **Android Studio** - Para emulateor/dispositivo Android
- **Git** - Para control de versiones

### Verificar instalaciones

```powershell
docker --version
java -version
mvn -version
flutter --version
```

---

## Guía rápida de arranque

### Orden de ejecución (importante seguir este orden)

1. **Android Studio** - Abrir el proyecto `frontend/` (necesario para que Flutter detecte el emulador)
2. **Docker Desktop** - Esperar a que muestre "Running"
3. **MySQL (Docker):**
   ```powershell
   cd docker
   docker-compose up -d
   ```
4. **Backend (Maven):**
   ```powershell
   cd backend
   mvn spring-boot:run
   ```
5. **Flutter:**
   ```powershell
   cd frontend
   flutter run
   ```

### Verificación

Visita: http://localhost:8085/api/auth/health

Debe mostrar:
```json
{"success":true,"message":"API funciona correctamente","data":"OK"}
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