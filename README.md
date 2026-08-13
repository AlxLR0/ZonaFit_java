# 🏋️‍♂️ Zona Fit - Sistema de Gestión de Clientes

![Zona Fit Banner](assets/zona_fit_banner.png)

¡**Zona Fit**! 📊 Un sistema ligero de consola en Java diseñado para administrar la información de los clientes de un gimnasio o centro deportivo de forma sencilla y eficiente.

---

## 🎯 ¿Qué hace el proyecto?

El sistema se conecta a una base de datos MySQL para gestionar la información de los miembros en tiempo real. Desde un menú interactivo en la consola, se pueden realizar las operaciones CRUD principales:

* 📋 **Listar clientes**: Muestra la lista completa de miembros registrados en la base de datos.
* 🔍 **Buscar cliente**: Consulta los detalles de un usuario a partir de su ID.
* ➕ **Agregar cliente**: Registra nuevos clientes ingresando su nombre, apellido y número de membresía.
* ✏️ **Modificar cliente**: Actualiza los datos de un cliente existente.
* 🗑️ **Eliminar cliente**: Da de baja a un miembro del sistema mediante su ID.

---

## 🏗️ Arquitectura del Proyecto

El proyecto aplica una arquitectura en capas limpia y organizada para separar las responsabilidades del código:

* 📦 **`zona_fit.dominio`**: Contiene la clase modelo `Cliente.java` que representa la entidad del cliente y sus atributos.
* 🔌 **`zona_fit.conexion`**: Administra la conexión a la base de datos MySQL mediante JDBC (`Conexion.java`).
* 🗄️ **`zona_fit.datos`**: Capa de acceso a datos que incluye la interfaz `IClienteDao.java` y su implementación `ClienteDAO.java` para ejecutar las sentencias SQL.
* 💻 **`zona_fit.presentacion`**: Contiene la clase principal `ZonaFitApp.java`, encargada de desplegar el menú interactivo en consola y procesar las peticiones del usuario.

---

## 🛠️ Tecnologías Utilizadas

* ☕ **Java** (JDK 17+)
* 🐘 **MySQL** (Base de datos relacional)
* 📦 **Apache Maven** (Gestor de dependencias y construcción del proyecto)
* 🔌 **MySQL Connector/J** (Driver JDBC para la integración con MySQL)

---

## 🚀 Guía de Configuración e Instalación

### 1. Requisitos Previos
* Tener instalado **Java JDK** (versión 17 o superior).
* Tener un servidor **MySQL** en funcionamiento.
* Contar con **Maven** instalado o integrado en tu IDE de preferencia (IntelliJ IDEA, Eclipse, VS Code, etc.).

### 2. Configuración de la Base de Datos
1. Abrir el gestor de base de datos MySQL (MySQL Workbench, DBeaver, CLI, etc.).
2. Ejecutar el script SQL ubicado en `sql/guia_base_de_datos.sql`. Este script creará la base de datos y la tabla necesaria:

```sql
CREATE DATABASE IF NOT EXISTS Zona_fit_java;
USE Zona_fit_java;

CREATE TABLE IF NOT EXISTS cliente (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    membresia INT NOT NULL,
    PRIMARY KEY (id)
);
```

### 3. Ajuste de Parámetros de Conexión
En el archivo `Conexion.java` (`src/main/java/zona_fit/conexion/Conexion.java`), ajustar los datos de conexión según la configuración del entorno local:

```java
var url = "jdbc:mysql://localhost:3306/Zona_fit_java"; // Cambiar puerto si es necesario (ej. 3307)
var usuario = "TU_USUARIO";                            // Reemplazar con el usuario de MySQL
var password = "TU_PASSWORD";                          // Reemplazar con la contraseña de MySQL
```

---

## 💻 Ejecución de la Aplicación

### Opción 1: Desde un IDE (IntelliJ IDEA / Eclipse / VS Code)
1. Abrir el proyecto como un proyecto Maven.
2. Abrir la clase `src/main/java/zona_fit/presentacion/ZonaFitApp.java`.
3. Ejecutar el método `main()`.

### Opción 2: Desde la consola mediante Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="zona_fit.presentacion.ZonaFitApp"
```

¡Todo listo! El menú de **Zona Fit** se desplegará en la consola para comenzar a interactuar con la aplicación 🚀.
