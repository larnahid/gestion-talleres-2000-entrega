-- =====================================================================
-- SCRIPT DE INICIALIZACIÓN - GESTION TALLERES 2000
-- Base de datos MySQL 8.x
-- =====================================================================

-- Usar la base de datos
USE gestion_talleres;

-- =====================================================================
-- TABLA: ROLES
-- =====================================================================
CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- TABLA: USUARIOS
-- =====================================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- TABLA: CLIENTES
-- =====================================================================
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(12) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- TABLA: VEHICULOS
-- =====================================================================
CREATE TABLE IF NOT EXISTS vehiculos (
    matricula VARCHAR(10) PRIMARY KEY,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- TABLA: ORDENES DE REPARACION
-- =====================================================================
CREATE TABLE IF NOT EXISTS ordenes_reparacion (
    id_orden INT AUTO_INCREMENT PRIMARY KEY,
    fecha_entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    url_foto VARCHAR(255),
    matricula VARCHAR(10),
    id_usuario INT,
    FOREIGN KEY (matricula) REFERENCES vehiculos(matricula),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- TABLA: CITAS
-- =====================================================================
CREATE TABLE IF NOT EXISTS citas (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(255),
    matricula VARCHAR(10),
    FOREIGN KEY (matricula) REFERENCES vehiculos(matricula)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- CARGA DE DATOS INICIALES
-- =====================================================================

-- Insertar roles
INSERT INTO roles (nombre_rol) VALUES
    ('ADMINISTRADOR'),
    ('MECANICO');

-- Insertar usuarios (contraseñas en BCrypt hash)
-- Password: "Taller2026*" para admin / "Mecanico123*" para mecánico
INSERT INTO usuarios (username, password, id_rol) VALUES
    ('nahid_admin', '$2b$10$zR01xkkYhXO3X1h3z35yWODmAW.djM9JEbBCjbM1UBSXoaEIHEABe', 1),
    ('carlos_mecanico', '$2b$10$ay90zd.aEiaDrcFQvH9LG.miB8JaI7nm.2HMf3yP2RTqeKkFnvYPO', 2);

-- Insertar clientes
INSERT INTO clientes (dni, nombre, telefono) VALUES
    ('12345678A', 'Juan Garcia', '600111222'),
    ('87654321B', 'Maria Lopez', '650333444'),
    ('11223344C', 'Talleres Unidos SL', '912345678');

-- Insertar vehículos
INSERT INTO vehiculos (matricula, marca, modelo, id_cliente) VALUES
    ('1234ABC', 'Toyota', 'Corolla', 1),
    ('5678DEF', 'Seat', 'Ibiza', 2),
    ('9012GHI', 'Ford', 'Transit', 3);

-- Insertar órdenes de reparación
INSERT INTO ordenes_reparacion (descripcion, estado, matricula, id_usuario) VALUES
    ('Revision de aceite y filtros', 'TERMINADO', '1234ABC', 2),
    ('Cambio de pastillas de freno', 'EN_PROCESO', '5678DEF', 2),
    ('Reparacion embrague', 'PENDIENTE', '9012GHI', 2);

-- Insertar citas
INSERT INTO citas (fecha_hora, motivo, matricula) VALUES
    ('2026-05-10 09:00:00', 'ITV Anual', '1234ABC'),
    ('2026-05-12 16:30:00', 'Ruido en motor', '5678DEF');

-- =====================================================================
-- VERIFICACIÓN
-- =====================================================================
SELECT 'Base de datos creada y datos cargados correctamente' AS status;
SELECT COUNT(*) AS total_roles FROM roles;
SELECT COUNT(*) AS total_usuarios FROM usuarios;
SELECT COUNT(*) AS total_clientes FROM clientes;
SELECT COUNT(*) AS total_vehiculos FROM vehiculos;
SELECT COUNT(*) AS total_ordenes FROM ordenes_reparacion;
SELECT COUNT(*) AS total_citas FROM citas;