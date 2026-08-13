package zona_fit.conexion;  // 📦 Paquete "conexion": aquí vive todo lo relacionado con conectarnos a la base de datos

// 🔌 Importamos las clases de JDBC de Java que nos permiten hablar con MySQL.
//    Connection   = representa la conexión activa con la base de datos.
//    DriverManager = es el "administrador" que crea la conexión a partir de la URL, usuario y password.
import java.sql.Connection;
import java.sql.DriverManager;

// 🏗️ Clase "Conexion": se encarga únicamente de darnos un objeto Connection listo para usar.
//    Es como la llave que abre la puerta de nuestra base de datos "Zona_fit_java".
public class Conexion {

    // ⚙️ Método estático "getConexion": como es static, NO necesitamos crear un objeto de la clase
    //    para usarlo (se llama así: Conexion.getConexion()). Devuelve una Connection o null si falla.
    public static Connection getConexion(){
        Connection conexion = null;  // 💡 Inicializamos la conexión en null. Si algo sale mal, retornamos null.

        var baseDatos = "Zona_fit_java";                        // 🗄️ Nombre de la base de datos a la que queremos conectarnos.
        var url = "jdbc:mysql://127.0.0.1:3307/"+baseDatos;     // 🌐 URL de conexión:
                                                                //    - "jdbc:mysql://"  → protocolo para MySQL
                                                                //    - "127.0.0.1"      → la máquina local (nuestra propia PC)
                                                                //    - ":3307"          → el PUERTO donde MySQL está escuchando
                                                                //                          (NO es el 3306 por defecto, aquí corre en un contenedor Docker en el 3307)
                                                                //    - +baseDatos       → le agregamos el nombre de la BD al final.
        var usuario ="root";        // 👤 Usuario de MySQL. IMPORTANTE: antes estaba mal escrito como "roo",
                                    //    y por eso daba "Access denied". Con "root" ya entra sin problema.
        var password = "admin";     // 🔑 Contraseña del usuario root en MySQL (la que configuramos al crear el contenedor).

        try {
            // 🧩 Cargamos el driver de MySQL (com.mysql.cj.jdbc.Driver). Este driver viene del JAR
            //    "mysql-connector-j-8.0.33.jar" declarado en el pom.xml (Maven).
            //    En Java moderno no es 100% obligatorio, pero lo dejamos para asegurar el registro del driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 🤝 Aquí es donde se hace la magia: DriverManager usa la URL + usuario + password
            //    para intentar abrir una conexión real contra la base de datos.
            conexion = DriverManager.getConnection(url, usuario, password);

        }catch (Exception e){  // 🚨 Si falla (credenciales malas, servidor apagado, puerto equivocado...)
            System.out.println("Error al conectar: "+e.getMessage());  // mostramos el motivo en consola.
        }
        return conexion;  // 📤 Devolvemos la conexión (o null si no se pudo conectar).
    }

    // 🧪 Método "main": solo para PROBAR la conexión de forma rápida desde la clase.
    //    ⚠️ OJO: aquí está escrito como "static void main()" sin el array de argumentos
    //    (normalmente es "public static void main(String[] args)"). Como IntelliJ igual lo
    //    detecta al correr esta clase, funciona para hacer pruebas manuales.
    static void main() {
        var conexion = Conexion.getConexion();  // 🔌 Llamamos al método de arriba (de esta misma clase) para obtener la conexión.

        if (conexion != null)                       // ✅ Si NO es null, significa que conectamos bien
            System.out.println("ya jalo esto 😎 "+conexion);  // y lo mostramos en consola con emoji de éxito.
        else                                        // ❌ Si es null, algo falló.
            System.out.println("no jalo");
    }
}
