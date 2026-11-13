CREATE DATABASE IF NOT EXISTS biblioteca_db;
USE biblioteca_db;

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS revistas;
DROP TABLE IF EXISTS materiales;

CREATE TABLE materiales (
    id VARCHAR(20) PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    tipo ENUM('Libro', 'Revista') NOT NULL,
    anio_publicacion INT NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    cantidad_disponible INT NOT NULL DEFAULT 1,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE libros (
    id_material VARCHAR(20) PRIMARY KEY,
    autor VARCHAR(120) NOT NULL,
    isbn VARCHAR(30) NOT NULL,
    num_paginas INT NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiales(id) ON DELETE CASCADE
);

CREATE TABLE revistas (
    id_material VARCHAR(20) PRIMARY KEY,
    edicion INT NOT NULL,
    volumen INT NOT NULL,
    mes_publicacion VARCHAR(30) NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiales(id) ON DELETE CASCADE
);

CREATE TABLE prestamos (
    id_prestamo VARCHAR(20) PRIMARY KEY,
    id_material VARCHAR(20) NOT NULL,
    id_usuario VARCHAR(20) NOT NULL,
    fecha_prestamo DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_material) REFERENCES materiales(id)
);

INSERT INTO materiales (id, titulo, tipo, anio_publicacion, editorial, cantidad_disponible, disponible) VALUES
('L01', 'Don Quijote de la Mancha', 'Libro', 1605, 'Francisco de Robles', 3, TRUE),
('L02', 'Crimen y Castigo', 'Libro', 1866, 'El Mensajero Ruso', 2, TRUE),
('L03', 'Orgullo y Prejuicio', 'Libro', 1813, 'T. Egerton', 4, TRUE),
('L04', 'Cien Años de Soledad', 'Libro', 1967, 'Sudamericana', 5, TRUE),
('R01', 'National Geographic', 'Revista', 2024, 'National Geographic Society', 6, TRUE),
('R02', 'Muy Interesante', 'Revista', 2025, 'Zinet Media', 4, TRUE);

INSERT INTO libros (id_material, autor, isbn, num_paginas) VALUES
('L01', 'Miguel de Cervantes', '978-8420412146', 1400),
('L02', 'Fiodor Dostoyevski', '978-8420651330', 776),
('L03', 'Jane Austen', '978-8491051329', 450),
('L04', 'Gabriel Garcia Marquez', '978-8437604947', 496);

INSERT INTO revistas (id_material, edicion, volumen, mes_publicacion) VALUES
('R01', 245, 12, 'Septiembre'),
('R02', 512, 42, 'Marzo');
