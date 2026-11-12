CREATE DATABASE IF NOT EXISTS db_biblioteca;

USE db_biblioteca;

CREATE TABLE IF NOT EXISTS usuarios (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
cod_usuario VARCHAR(50) NOT NULL UNIQUE,
nombres VARCHAR(50) NOT NULL,
apellidos VARCHAR(50) NOT NULL,
identificacion VARCHAR(50) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
telefono VARCHAR(20),
password_hash VARCHAR(255) NOT NULL,
rol ENUM('publico', 'administrador') NOT NULL DEFAULT 'publico',
estado ENUM('activo', 'inactivo') DEFAULT 'activo',
fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS materiales (
id_material INT AUTO_INCREMENT PRIMARY KEY,
cod_material VARCHAR(50) NOT NULL UNIQUE,
titulo VARCHAR(200) NOT NULL,
tipo ENUM('Libro', 'Revista') NOT NULL,
autor VARCHAR(100) NOT NULL,
ISBN VARCHAR(50) NOT NULL,
edicion VARCHAR(100) NOT NULL,
publicacion INT,
editorial VARCHAR(150),
stock INT NOT NULL DEFAULT 0,
estado ENUM('disponible', 'agotado','inoperativo') DEFAULT 'disponible'
);

CREATE TABLE IF NOT EXISTS prestamos (
id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
cod_prestamo VARCHAR(50) NOT NULL UNIQUE,
id_usuario INT NOT NULL,
id_material INT NOT NULL,
fecha_prestamo DATE NOT NULL,
fecha_entrega DATE NOT NULL,
fecha_entregado DATE DEFAULT NULL,
estado ENUM('activo', 'devuelto', 'vencido') DEFAULT 'activo',

CONSTRAINT fk_prestamo_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
CONSTRAINT fk_prestamo_material FOREIGN KEY (id_material) REFERENCES materiales(id_material)
);

-- ==========================================
-- DATOS DE PRUEBA INICIALES
-- ==========================================

-- Insertar usuarios de prueba (La contraseña cifrada corresponde a 'Patito123')
INSERT INTO usuarios (cod_usuario, nombres, apellidos, identificacion, email, telefono, password_hash, rol, estado) 
VALUES 
('LISSALINAS', 'Liseth Alejandra', 'Salinas Vicente', '00000000-0', 'alejandra@admin.com', '7000-0000', '$2a$10$7XvW5y6Z8v9w0x1y2z3A4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0', 'administrador', 'activo'),
('JUAPEREZ', 'Juan', 'Pérez', '11111111-1', 'juan.perez@email.com', '7111-2222', '$2a$10$7XvW5y6Z8v9w0x1y2z3A4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0', 'publico', 'activo');

-- Insertar un material de prueba
INSERT INTO materiales (cod_material, titulo, tipo, autor, ISBN, edicion, publicacion, editorial, stock, estado) 
VALUES 
('LIB-001', 'Programación en Java para Ingenieros', 'Libro', 'Deitel & Deitel', '978-607-15-1495-2', '1ª Edición', 2022, 'Pearson', 5, 'disponible');

-- Insertar un préstamo de prueba
INSERT INTO prestamos (cod_prestamo, id_usuario, id_material, fecha_prestamo, fecha_entrega, fecha_entregado, estado) 
VALUES 
('PRES-001', 2, 1, '2026-11-01', '2026-11-15', NULL, 'activo');

