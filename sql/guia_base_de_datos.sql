-- =====================================================================================
-- 🗄️ GUÍA DE BASE DE DATOS — PROYECTO ZONA FIT (Java)
-- =====================================================================================
-- Útil para futuro: si borras la BD o cambias de máquina, con este script la recreas
-- en 1 minuto. 📌 EJECÚTALO EN ORDEN: 1) Crear BD → 2) Crear tabla → 3) Usar la BD.
-- Diccionario de la tabla "cliente" (coincide con los atributos de la clase Cliente.java):
--   id         (int, PK, AUTO_INCREMENT)  → atributo "id"         (lo genera MySQL solo).
--   nombre     (varchar)                   → atributo "nombre"
--   apellido   (varchar)                   → atributo "apellido"
--   membresia  (int)                       → atributo "membresia"
-- =====================================================================================

-- 1️⃣ Crear la base de datos (si aún no existe).
--    ⚠️ El nombre DEBE ser exactamente: Zona_fit_java  → porque así está escrito en
--    Conexion.java dentro de la variable baseDatos = "Zona_fit_java".
--    (en Windows/locales a veces MySQL ignora mayúsculas en nombres de BD, pero mejor
--    mantener el mismo nombre que usa el código para no liarse).
CREATE DATABASE IF NOT EXISTS Zona_fit_java;

-- 2️⃣ Decirle "a partir de ahora trabajo en esta base de datos".
USE Zona_fit_java;

-- 3️⃣ Crear la tabla "cliente" si aún no existe.
CREATE TABLE IF NOT EXISTS cliente (
    id         INT NOT NULL AUTO_INCREMENT,   -- 🔢 id: número entero, NO nulo, se auto-incrementa solo (PK).
    nombre     VARCHAR(45) NOT NULL,          -- 🏷️ nombre: texto de máx. 45 caracteres, obligatorio (no null).
    apellido   VARCHAR(45) NOT NULL,          -- 🏷️ apellido: igual que nombre.
    membresia  INT NOT NULL,                  -- 🎟️ membresia: entero, obligatorio.
    PRIMARY KEY (id)                          -- 🔑 La llave primaria es "id" (único y no nulo).
);

-- (OPCIONAL) 🧪 Insertar un cliente de prueba para ver que todo funciona de entrada.
--    Mismo formato que hace el método agregarCliente() de ClienteDAO.java:
--    no ponemos id (AUTO_INCREMENT lo genera) y los valores van en el orden nombre, apellido, membresia.
-- INSERT INTO cliente (nombre, apellido, membresia) VALUES ('Alejandro', 'LR', 100);

-- (OPCIONAL) 🔍 Consulta para ver todo lo que hay en la tabla.
--    Es exactamente el SELECT que ejecuta ClienteDAO.listarClientes().
-- SELECT * FROM cliente ORDER BY id;

-- =====================================================================================
-- ⚙️ CONFIGURACIÓN DE LA CONEXIÓN (para que sepas de dónde salen los datos de Conexion.java)
-- =====================================================================================
-- La clase Conexion.java usa estos valores cuando pides la conexión:
--    url       = jdbc:mysql://127.0.0.1:3307/Zona_fit_java
--                │          │        │        │
--                │          │        │        └── base de datos (paso 1 de arriba)
--                │          │        └────────── Puerto 3307 (NO el 3306 por defecto)
--                │          └─────────────────── 127.0.0.1 = "localhost" (nuestra propia máquina)
--                └───────────────────────────── protocolo JDBC para MySQL
--    usuario   = root
--    password  = admin
--
-- 💡 Si te sale "Access denied" al correr el proyecto, revisa sobre todo el usuario y
--    la password del contenedor/servicio de MySQL, no solo el código.
-- =====================================================================================