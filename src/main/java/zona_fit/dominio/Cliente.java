package zona_fit.dominio;  // 📦 Paquete "dominio": aquí están las clases que representan los datos del negocio (el "modelo").

import java.util.Objects;  // 🔧 Herramienta de Java que nos ayuda a comparar objetos de forma segura (para equals y hashCode).

// 🧍 Clase "Cliente": es una "clase de dominio" (POJO / JavaBean).
//    Representa a UN cliente del gimnasio Zona Fit, con los mismos campos que tiene la tabla "cliente" en la base de datos.
//    Es un molde que usamos en TODO el proyecto para transportar los datos entre la base de datos,
//    la lógica (ClienteDAO) y la interfaz (ZonaFitApp).
//    📝 OJO: 1 atributo por cada columna de la tabla → en MySQL la tabla se llama "cliente" (COLUMNAS: id, nombre, apellido, membresia).
public class Cliente {
    // ✏️ Atributos (campos) privados: solo la propia clase puede tocarlos directamente
    //    y el resto del mundo accede a través de los "getters" y "setters" de abajo.
    private int id;            // 🔢 Identificador único del cliente (AUTO_INCREMENT en MySQL).
    private String nombre;     // 🏷️ Primer nombre del cliente.
    private String apellido;   // 🏷️ Apellido del cliente.
    private int membresia;     // 🎟️ Nivel de membresía (100, 200, 300... nosotros decidimos su significado).

    // 🏗️ Constructor vacío: crea un Cliente "en blanco" (sin datos).
    //    Se usa en ClienteDAO.listarClientes() para ir llenando cada atributo con el SETTER
    //    (por ejemplo: cliente.setNombre(rs.getString("nombre"))). 📌 Patrón "crear objeto vacío + setters".
    public Cliente(){}

    // 🏗️ Constructor con SOLO el id: se usa cuando ya conocemos el id y queremos, por ejemplo,
    //    buscarlo o eliminarlo, sin tener que escribir los demás datos.
    //    Se usa en ZonaFitApp en los casos de "buscar" y "eliminar" (ej: new Cliente(idCliente)).
    public Cliente(int id){
        this.id = id;
    }

    // 🏗️ Constructor con datos PERO sin id: esto es para cuando vamos a INSERTAR un cliente nuevo
    //    (el id lo genera automáticamente MySQL). Se usa en ZonaFitApp caso 3 (agregar).
    public Cliente(String nombre, String apellido, int membresia){
        this.nombre = nombre;
        this.apellido = apellido;
        this.membresia = membresia;

    }

    // 🏗️ Constructor COMPLETO (id + datos): para cuando ya tenemos todos los datos, por ejemplo al MODIFICAR
    //    un cliente. Se usa en ZonaFitApp caso 4 (modificar).
    //    🧠 Detalle de lógica: la primera línea "this(nombre, apellido, membresia)" es un "encadenamiento
    //    de constructor": llama al constructor de 3 parámetros de arriba para que asigne los datos,
    //    y luego solo nos falta asignar el id en la línea siguiente. Así no repetimos código.
    public Cliente(int id,String nombre, String apellido, int membresia){
        this(nombre,apellido,membresia);
        this.id = id;


    }

    // 👇👇👇 GETTERS (leer) y SETTERS (escribir) de cada atributo.
    //    Es el "encapsulamiento": quienes usen esta clase tocan los datos por aquí, no directamente.
    //    Por ejemplo ClienteDAO usa setNombre() para llenar al listar, y ZonaFitApp usa getNombre() para imprimir el cliente.

    public int getId() {          // 🔎 Devuelve el id del cliente.
        return id;
    }

    public void setId(int id) {   // ✍️ Asigna el id del cliente (lo usan listarClientes y ... en la modificación).
        this.id = id;
    }

    public String getNombre() {   // 🔎 Devuelve el nombre.
        return nombre;
    }

    public void setNombre(String nombre) {  // ✍️ Asigna el nombre.
        this.nombre = nombre;
    }

    public String getApellido() { // 🔎 Devuelve el apellido.
        return apellido;
    }

    public void setApellido(String apellido) {  // ✍️ Asigna el apellido.
        this.apellido = apellido;
    }

    public int getMembresia() {   // 🔎 Devuelve la membresía.
        return membresia;
    }

    public void setMembresia(int membresia) {  // ✍️ Asigna la membresía.
        this.membresia = membresia;
    }

    // 📄 Sobrescribimos toString(): este método existe en TODAS las clases de Java (viene de Object).
    //    Lo sobreescribimos para que al IMPRIMIR un Cliente (ej: System.out.println(cliente))
    //    se muestre "Cliente{id=1, nombre='Alejandro', apellido='LR', membresia=100}" en vez de algo feo.
    //    Se usa en ZonaFitApp.forEach(System.out::println) y demás mensajes.
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", membresia=" + membresia +
                '}';
    }

    // ⚖️ equals(): también viene de Object. Sirve para comparar SI DOS CLIENTES SON IGUALES por su CONTENIDO
    //    (no solo por dirección de memoria). En este proyecto no se usa en la app todavía, pero es buena práctica
    //    dejarlo definido en la clase de dominio.
    //    🧠 Lógica que puede confundir, línea por línea:
    //        1) if (o == null || o.getClass() != this.getClass()): si el objeto que nos pasan es null,
    //           o bien NO es de la clase Cliente → NO son iguales, retornamos false rápido (corta el método).
    //        2) Cliente cliente = (Cliente) o: aquí ya sabemos que o es un Cliente, entonces lo "moldeamos"
    //           (cast) para poder acceder a sus getters.
    //        3) return id == cliente.id && membresia == ...: comparamos campo por campo.
    //           Los int se comparan con == (igual directo) y los String con Objects.equals()
    //           porque Objects.equals() maneja bien el caso de que alguno sea null.
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id && membresia == cliente.membresia && Objects.equals(nombre, cliente.nombre) && Objects.equals(apellido, cliente.apellido);
    }

    // #️⃣ hashCode(): viene de Object también. Genera un "número de huella digital" del objeto basado en sus datos.
    //    🔑 Regla de oro: si dos objetos son equals(...) → deben tener el mismo hashCode().
    //    Por eso aquí lo calculamos con Objects.hash() usando EXACTAMENTE los mismos campos que en equals().
    //    Es requerido para que la clase funcione bien en colecciones (HashSet, HashMap) en el futuro.
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, membresia);
    }
}