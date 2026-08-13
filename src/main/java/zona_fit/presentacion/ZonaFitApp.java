package zona_fit.presentacion;  // 📦 Paquete "presentacion": aquí está la capa de INTERFAZ (lo que ve y toca el usuario en consola).

// 📥 Imports de las otras capas del proyecto (esto es la clave para seguir el flujo de datos):
//    - ClienteDAO (paquete zona_fit.datos)        → la implementación concreta de acceso a BD.
//    - IClienteDao (paquete zona_fit.datos)       → el contrato/interfaz (los métodos que la app puede llamar).
//    - Cliente (paquete zona_fit.dominio)         → el modelo de datos (los atributos del cliente).
import zona_fit.datos.ClienteDAO;
import zona_fit.datos.IClienteDao;
import zona_fit.dominio.Cliente;

import java.util.Scanner;  // ⌨️ Scanner: clase de Java para LEER lo que el usuario escribe por consola.

// 🎮 Clase "ZonaFitApp": ES LA APLICACIÓN REAL (el menú que ve el usuario).
//    Esta es la clase que nos da la experiencia completa: menú, pedir datos, y
//    usar ClienteDAO (que a su vez usa Conexion y Cliente) para hacer todo contra MySQL.
public class ZonaFitApp {

    // 🧪 Método "main": punto de entrada de la app (igual que los anteriores, está como "static void main()" sin
    //    String[] args; IntelliJ la reconoce al correr esta clase). Solo llama a lo que hace el trabajo real.
    static void main() {
        zonaFitApp();  // ▶️ Llamamos al método donde está el bucle del menú (está justo abajo).
    }

    // 🔁 Método "zonaFitApp": el corazón de la aplicación. Contiene el CICLO (while) que mantiene el menú
    //    corriendo hasta que el usuario elige "Salir". ES PRIVADO porque solo se usa dentro de esta clase.
    private static void zonaFitApp(){
        var salir = false;                         // 🚩 Bandera de control: false = seguimos en el menú, true = salimos.
        var consola = new Scanner(System.in);      // ⌨️ Creamos el Scanner para leer lo que escriba el usuario.

        // 🏗️ creamos objeto de la clase clientedao
        var clienteDao = new ClienteDAO();         // 🧾 IMPORTANTE: declaramos la variable como interfaz (IClienteDao
                                                   //    diríamos, aunque aquí se ve como ClienteDAO). Este objeto habla
                                                   //    con la BD usando los métodos de ClienteDAO (que a su vez usa
                                                   //    la conexión de Conexion.java).
        while (!salir){                            // 🔁 MIENTRAS la bandera "salir" sea false → el menú sigue vivo.
            try {
                var opcion= mostrarMenu(consola);       // 1️⃣ Mostramos el menú y guardamos el número que eligió el usuario.
                salir=ejecutarOpciones(consola, opcion,clienteDao);  // 2️⃣ Ejecutamos la opción elegida. Devuelve true si eligió salir.
            }catch (Exception e){                        // 🚨 Si algo falla (ej: el usuario escribe letras en vez de números)
                System.out.println("Error al ejeuctar opciones: "+e.getMessage());  // lo atrapamos para que no se caiga la app.
            }
            System.out.println();                        // ✍️ Salto de línea para que la consola se vea ordenada.
        }
    }

    // 🖥️ Método "mostrarMenu": imprime el menú y devuelve la opción que eligió el usuario como INT.
    //    📤 Recibe el Scanner (para leer la respuesta) → 🏷️ Devuelve el número elegido.
    public static int mostrarMenu(Scanner consola){
        System.out.print("""
                ***---ZONA FIT---*** 
                1. lISTAR CLIENTES
                2. BUSCAR CLIENTE
                3. AGREGAR CLIENTE
                4. MODIFICAR CLIENTE
                5. ELIMINAR CLIENTE
                6. SALIR
                ELIJE UNA OPCION:\s""");     // 🔤 EL SIMBOLO """ = "text block" de Java: texto de varias líneas en un solo print.
        return Integer.parseInt(consola.nextLine());  // 🧠 Lógica clave: nextLine() lee TODO lo que el usuario escribe (texto), y luego
                                                      //    Integer.parseInt() lo convierte a número entero. Si escribe "abc",
                                                      //    parseInt lanza excepción → la atrapa el try/catch de zonaFitApp().

    }

    // 🎛️ Método "ejecutarOpciones": ES EL MENÚ REAL. Según el número elegido (opcion), hace una cosa u otra.
    //    🧠 Es un "switch de flecha" (sintaxis moderna de Java, quedó expresiva).
    //    📤 Recibe Scanner + opcion + el DAO (IClienteDao) → 🏷️ Devuelve true si el usuario eligió salir.
    //    (El DAO se pasa como la INTERFAZ IClienteDao para desacoplar y poder cambiar de DAO si quisieras.)
    private static boolean ejecutarOpciones(Scanner consola, int opcion, IClienteDao clienteDao){
        var salir = false;  // 🚩 Bandera de control local: se vuelve true solo en el caso "6. Salir".
        switch (opcion){
            case 1->{// listar clientes
                System.out.println("---Listado de clientes---");
                var clientes = clienteDao.listarClientes();  // 📋 Llama al método de ClienteDAO (que trae todos de la BD).
                clientes.forEach(System.out::println);       // 🖨️ Imprime cada uno usando el toString() de Cliente.
            }
            case 2 ->{// buscar cliente por id
                System.out.print("Introduce ID del cliente: ");
                var idCliente = Integer.parseInt(consola.nextLine());  // 🧠 Leemos y convertimos a int (clave: parseInt).
                var cliente = new Cliente(idCliente);                  // 🧍 Creamos un Cliente SOLO con el id (constructor Cliente(id), Cliente.java).
                var encontrado = clienteDao.buscarClientePorId(cliente); // 🔎 Lo mandamos a buscar (la BD lo llena de datos si existe).
                if (encontrado)                                         // ✅ Si existe, el mismo objeto ya tiene sus datos llenos:
                    System.out.println("Cliente encontrado: "+cliente); //    lo imprimimos y se ven nombre, apellido, membresía.
                else                                                    // ❌
                    System.out.println("Cliente no encontrado"+ cliente);

            }
            case 3 ->{// agregar cliente
                System.out.println("--- Agregar cliente ---");
                System.out.print("Nombre: ");
                var nombre = consola.nextLine();                        // ⌨️ Pedimos nombre.
                System.out.print("Apellido: ");
                var apellido = consola.nextLine();                      // ⌨️ Pedimos apellido.
                System.out.print("Membresia: ");
                var membresia = Integer.parseInt(consola.nextLine());   // ⌨️ Membresía como número.

                //🧍 crear objeto cliente (sin id)
                var cliente = new Cliente(nombre,apellido,membresia);   //    Constructor de 3 args (sin id → MySQL lo genera).
                var agregado = clienteDao.agregarCliente(cliente);      // ➕ Se inserta (usa executeUpdate de ClienteDAO).
                if (agregado)                                           // ✅
                    System.out.println("Cliente agregado: "+cliente);
                else                                                    // ❌
                    System.out.println("cliente no agregado");

            }
            case 4->{//modificar cliente
                System.out.println("--- Modificar cliente ---");
                System.out.print("Id Cliente: ");
                var idCliente = Integer.parseInt(consola.nextLine());
                System.out.print("Nombre: ");
                var nombre = consola.nextLine();
                System.out.print("Apellido: ");
                var apellido = consola.nextLine();
                System.out.println("membresia");
                var membresia = Integer.parseInt(consola.nextLine());

                //🧍 crear objeto a modificar
                var cliente = new Cliente(idCliente, nombre, apellido, membresia);  // Constructor de 4 args (id + datos).
                var modificado = clienteDao.modificarCliente(cliente);   // ✏️ ⚠️ OJO: este método en ClienteDAO todavía
                                                                         //    devuelve siempre false (falta implementarlo).
                if(modificado)                                           // ✅
                    System.out.println("cliente modificado: "+cliente);
                else                                                     // ❌ (siempre cae aquí por el momento)
                    System.out.println("cliente no modificado");


            }
            case 5->{// eliminar cliente
                System.out.println("--- Eliminar cliente ---");
                System.out.print("Id Cliente: ");
                var idCliente = Integer.parseInt(consola.nextLine());
                var cliente = new Cliente(idCliente);                    // 🧍 Cliente solo con id.
                var eliminado = clienteDao.eliminarCliente(cliente);     // 🗑️ ⚠️ OJO: en ClienteDAO todavía devuelve
                                                                         //    siempre false (falta implementarlo).
                if(eliminado)                                            // ✅
                    System.out.println("cliente eliminado: "+cliente);
                else                                                     // ❌ (siempre cae aquí por el momento)
                    System.out.println("cliente no eliminado");

            }
            case 6->{//salir
                System.out.println("saliendo...");
                salir = true;  // 🚩 Activamos la bandera → el while de zonaFitApp() se detiene y la app termina.
            }
            default -> System.out.println("opcion no valida");  // ⚠️ Si el número no es del 1 al 6.
        }
        return salir;  // 📤 Devolvemos la bandera: true solo cuando el usuario eligió salir.
    }
}