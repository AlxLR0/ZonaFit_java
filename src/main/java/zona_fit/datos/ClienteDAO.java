package zona_fit.datos;  // 📦 Paquete "datos": capa de acceso a datos (la que ejecuta el SQL contra MySQL).

// 🔌 Importamos Conexion (viene del paquete zona_fit.conexion) para pedirle la conexión a la BD.
import zona_fit.conexion.Conexion;
// 🧍 Importamos Cliente (viene del paquete zona_fit.dominio): el objeto modelo con el que "traducimos" las filas de la BD.
import zona_fit.dominio.Cliente;

// 📦 Clases de JDBC de Java (las necesarias para ejecutar SQL):
//    - Connection    → la conexión ya abierta (la que nos devuelve Conexion.getConexion()).
//    - PreparedStatement → un "comando SQL" preparado, segurísimo porque no se puede inyectar SQL malicioso (los ? marcan los datos).
//    - ResultSet     → la "tabla virtual" con los datos resultado de una consulta SELECT (se recorre fila por fila).
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;  // 📋 ArrayList: la implementación más común de List (lista dinámica). La usamos para juntar los clientes.
import java.util.List;       // 📋 Interfaz List: el tipo que devolvemos para "varios clientes".

// ⚙️ Importamos ESTÁTICO el método getConexion de la clase Conexion.
//    La palabra clave en el import es "static GETTERS": es que al ponerlo así, en este archivo podemos escribir
//    directamente getConexion() sin escribir Conexion.getConexion(). Lo traemos de:
//    zona_fit.conexion.Conexion (getConexion nos abre la conexión a la base "Zona_fit_java").
import static zona_fit.conexion.Conexion.getConexion;

// 🏗️ Clase "ClienteDAO" (DAO = Data Access Object): la IMPLEMENTACIÓN real de la interfaz IClienteDao.
//    📌 Punto clave de origen: yo prometo cumplir el contrato de IClienteDao (este archivo: zona_fit.datos.IClienteDao).
//    Como esa interfaz EXIGE 5 métodos, el compilador no nos deja compilar si no los escribimos todos (implementamos,
//    el @Override de cada método lo confirma). ZonaFitApp trabaja contra la interfaz pero instancia Esta clase.
public class ClienteDAO implements IClienteDao {

    // 📋 Implementación del método "listarClientes" (viene de IClienteDao).
    //    Objetivo: traer TODOS los clientes de la tabla y devolverlos como List<Cliente>.
    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();  // 📭 Lista vacía donde iremos guardando cada cliente encontrado.
        PreparedStatement ps;                        // 🪄 Declaramos el comando SQL preparado (aún sin valor).
        ResultSet rs;                                // 🗂️ Declaramos el ResultSet (guardará las filas que devuelva la consulta).
        Connection con = getConexion();              // 🔌 Pedimos la conexión con nuestro método estático (Conexion.java).
        var sql = "SELECT * FROM cliente ORDER BY id";  // 🧾 La consulta: "dame todos (SELECT *) de la tabla cliente, ordenados por id".

        try {
            ps = con.prepareStatement(sql);          // 1️⃣ Traducimos el texto SQL a un PreparedStatement (aquí NO se ejecuta aún).
            rs = ps.executeQuery();                  // 2️⃣ SI se ejecuta: executeQuery() solo sirve para consultas que DEVUELVEN datos (SELECT).
                                                     //    El resultado (todas las filas) queda dentro del ResultSet.
            while (rs.next()){                       // 🔁 next() mueve un "cursor" a la siguiente fila; devuelve true mientras haya filas.
                                                     //    Con true: procesamos una fila. Cuando ya no hay más → false → termina el while.
                var cliente = new Cliente();         // 🧍 Creamos un Cliente VACÍO (Constructor sin argumentos, zona_fit.dominio.Cliente).
                cliente.setId(rs.getInt("id"));                     // ✍️ Llenamos el id con la columna "id" de la fila actual (getters vienen de Cliente).
                cliente.setNombre(rs.getString("nombre"));          // ✍️ Llenamos el nombre con la columna "nombre".
                cliente.setApellido(rs.getString("apellido"));      // ✍️ Llenamos el apellido con la columna "apellido".
                cliente.setMembresia(rs.getInt("membresia"));       // ✍️ Llenamos la membresía con la columna "membresia".
                clientes.add(cliente);               // ➕ Agregamos el cliente YA LLENO a nuestra lista.
            }
        }catch (Exception e){                        // 🚨 Si algo falla (conexión, SQL, etc.):
            System.out.println("Error al list clientes: "+e.getMessage());  // lo mostramos.
        }
        finally {                                    // 🧹 "finally" se ejecuta SIEMPRE, haya error o no.
            try {
                con.close();                         // 🔒 Cerramos la conexión para no dejar recursos colgados en MySQL.
            }catch (Exception e){
                System.out.println("Error al cerrar: "+e);
            }
        }

        return clientes;  // 📤 Devolvemos la lista (vacía si no había clientes o si hubo error).
    }

    // 🔎 Implementación de "buscarClientePorId" (viene de IClienteDao).
    //    Objetivo: buscar UN cliente por su id. Si existe, EL MISMO objeto cliente recibido queda lleno con los datos
    //    (por eso no se devuelve el Cliente, solo true/false). Devuelve true = encontrado.
    @Override
    public boolean buscarClientePorId(Cliente cliente) {
        PreparedStatement ps;   // 🪄 Comando SQL preparado.
        ResultSet rs;           // 🗂️ Resultado de la consulta.
        var con = getConexion();  // 🔌 Conexión a la BD (método estático de Conexion.java).
        var sql = "SELECT * FROM cliente WHERE id = ?";  // 🧾 Consulta que recibe UN dato variable (el id, marcado con ?).

        try {
            ps = con.prepareStatement(sql);  // 1️⃣ Preparamos el comando SQL.
            ps.setInt(1, cliente.getId());   // 2️⃣ Sustituimos el ? con el id del cliente recibido (getter getId, de zona_fit.dominio.Cliente).
            rs = ps.executeQuery();          // 3️⃣ Ejecutamos la consulta (SELECT → sí devuelve ResultSet).
            if (rs.next()) {                 // 4️⃣ next(): ¿hay al menos una fila? (como el id es único, habrá 0 o 1).
                                             //    Si devolvió true → el cliente SÍ existe en la BD.
                cliente.setNombre(rs.getString("nombre"));     // ✍️ Rellenamos los datos del mismo objeto cliente con lo que vino de la BD
                cliente.setApellido(rs.getString("apellido")); //    (así ZonaFitApp luego imprime todo con getters).
                cliente.setMembresia(rs.getInt("membresia"));
                return true;                 // ✅ Encontrado → retornamos true.
            }

        } catch (Exception e) {
            System.out.println("Error al recuperar cliente por id: " + e.getMessage());  // 🚨 Error → mensaje.
        } finally {
            try {
                con.close();  // 🔒 Siempre cerramos la conexión.
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }

        return false;  // ❌ Si no hubo fila o hubo error → retornamos false.

    }


    // ➕ Implementación de "agregarCliente" (viene de IClienteDao).
    //    Objetivo: INSERTAR un cliente nuevo en la tabla. Devuelve true si se insertó al menos 1 fila.
    @Override
    public boolean agregarCliente(Cliente cliente) {
        PreparedStatement ps;  // 🪄 Comando SQL preparado.
        Connection con = getConexion();  // 🔌 Conexión a la BD (método estático de Conexion.java).
        // 🧾 SQL de inserción: le decimos qué columnas llenamos (nombre, apellido, membresia) y los VALUES son 3 "?".
        //    ⚠️ NO ponemos el id porque la columna es AUTO_INCREMENT: MySQL lo genera solo.
        var sql = "INSERT INTO cliente (nombre, apellido, membresia)"+ " values(?,?,?)";

        try {
            ps = con.prepareStatement(sql);             // 1️⃣ Preparamos el comando con los "?" como espacios reservados.
            ps.setString(1, cliente.getNombre());       // 2️⃣ El 1er "?" = nombre (getter getNombre, de cliente).
            ps.setString(2, cliente.getApellido());     // 2️⃣ El 2do "?" = apellido (getter getApellido).
            ps.setInt(3, cliente.getMembresia());       // 2️⃣ El 3er "?" = membresía (getter getMembresia).

            // 3️⃣ Ejecutamos la inserción. 🧠 Detalle importante que causó un bug antes:
            //    - executeQuery() es SOLO para SELECT (devuelve ResultSet) → con un INSERT daba el error
            //      "Statement.executeQuery() cannot issue statements that do not produce result sets."
            //    - executeUpdate() es para INSERT / UPDATE / DELETE → devuelve un INT = cuántas filas se afectaron.
            //    Aquí usamos la versión correcta. Para MySQL, un INSERT que se ejecuta bien devuelve 1 (una fila nueva).
            var registros = ps.executeUpdate();
            if (registros > 0)   // ✅ Si hubo 1 o más filas afectadas → se insertó bien.
                return true;     //    Devuelve true para que ZonaFitApp muestre "Cliente agregado".


        }catch (Exception e){
            System.out.println("Error al insertar el cliente: " + e.getMessage());  // 🚨 Error → mensaje.
        } finally {
            try {
                con.close();  // 🔒 Siempre cerramos la conexión.
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;  // ❌ Si hubo error o 0 filas → false. (ZonaFitApp mostrará "cliente no agregado".)
    }

    // ✏️ Implementación de "modificarCliente" (viene de IClienteDao).
    //    ⚠️ AVISO: esta método aún NO está implementado (solo devuelve false). Es la parte que queda pendiente.
    @Override
    public boolean modificarCliente(Cliente cliente) {

        return false;  // 🚧 TODO (pendiente): aquí irá un UPDATE ... SET nombre=?, apellido=?, membresia=? WHERE id=?
    }

    // 🗑️ Implementación de "eliminarCliente" (viene de IClienteDao).
    //    ⚠️ AVISO: esta método aún NO está implementado (solo devuelve false). Es la parte que queda pendiente.
    @Override
    public boolean eliminarCliente(Cliente cliente) {
        return false;  // 🚧 TODO (pendiente): aquí irá un DELETE FROM cliente WHERE id=?
    }

    // 🧪 Método "main": solo para hacer PRUEBAS rápidas de los métodos (como listar o agregar).
    //    ⚠️ Igual que en Conexion, aquí es "static void main()" sin el array de argumentos.String[]; IntelliJ lo
    //    reconoce al correr ESTA clase directamente. La app "de verdad" es ZonaFitApp, no esto.
    static void main() {
        IClienteDao clienteDao = new ClienteDAO();  // 🏗️ Creamos el DAO. OJO: tipo declarado = interfaz, objeto real = ClienteDAO.

        //listar clientes
//        System.out.println("*** Listar clientes ***");
//        var clientes = clienteDao.listarClientes();
//        clientes.forEach(System.out::println);

        //buscar por id
//        var cliente1 = new Cliente(1);
//        System.out.println("Cliente antes de la busqueda: "+ cliente1);
//        var encontrado = clienteDao.buscarClientePorId(cliente1);
//        if (encontrado)
//            System.out.println("Cliente encontrado: "+cliente1);
//        else
//            System.out.println("no se encontro registro ❌");


        //agregar clientes
        var nuevoCliente = new Cliente("Fulanito","detal",300);  // 🧍 Nuevo cliente SIN id (constructor de 3 args, Cliente.java).
        var agregado = clienteDao.agregarCliente(nuevoCliente);  // ➕ Lo mandamos a insertar (método de ARRIBA).
        if (agregado)                                            // ✅
            System.out.println("Cliente agregado: "+nuevoCliente);
        else                                                     // ❌
            System.out.println("no se agrego cliente ❌");

        //listar clientes
        System.out.println("*** Listar clientes ***");
        var clientes = clienteDao.listarClientes();  // 📋 Traemos todos y...
        clientes.forEach(System.out::println);       // ...los imprimimos con el toString() de Cliente.



    }
}