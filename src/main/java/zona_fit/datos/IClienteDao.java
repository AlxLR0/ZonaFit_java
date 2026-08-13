package zona_fit.datos;  // 📦 Paquete "datos": aquí vivirá la capa de acceso a datos (lo que habla con la BD).

import zona_fit.dominio.Cliente;  // 🧍 Importamos Cliente (viene del paquete zona_fit.dominio). Lo usamos como el "objeto" que viaja
                                  //    de un lado a otro: la BD guarda filas, pero para este proyecto esas filas se convierten
                                  //    en objetos Cliente que la app manipula.
import java.util.List;            // 📋 Importamos la interfaz List (colección ordenada). Se usa para devolver "varios clientes a la vez".

// 🧾 INTERFAZ "IClienteDao": es el "contrato" de la capa de acceso a datos (DAO = Data Access Object).
//    Aquí SOLO se DECLARAN los métodos (firmas), NO se implementan.
//    🧠 Por qué existe el contrato aparte (patrón de diseño DAO): la clase ClienteDAO va a IMPLEMENTAR
//    estos métodos para MySQL. Si mañana quisiéramos usar otra base de datos (PostgreSQL, SQL Server...),
//    solo crearíamos otro ClienteDAO y el resto del proyecto (ZonaFitApp) seguiría como está,
//    porque trabajaría contra esta interfaz, no contra la implementación concreta.
//    📌 En ZonaFitApp se ve esto: la variable se declara como IClienteDao clienteDao = new ClienteDAO();
//    → programamos contra la interfaz, pero el objeto real es ClienteDAO (que proviene de este mismo paquete).

public interface IClienteDao {
    // 📋 Lista TODOS los clientes de la BD. Devuelve una List<Cliente> (varios Cliente).
    //    Lo implementa ClienteDAO.listarClientes() y lo llama ZonaFitApp (caso 1).
    List<Cliente> listarClientes();

    // 🔎 Busca un cliente por su id. Recibe un objeto Cliente (viene del paquete dominio).
    //    Devuelve true/false: true = lo encontró (y ADEMÁS deja el Cliente lleno con sus datos),
    //    false = no existe. Lo implementa ClienteDAO y lo llama ZonaFitApp (caso 2).
    boolean buscarClientePorId(Cliente cliente);

    // ➕ Inserta un cliente NUEVO en la BD. Devuelve true si se guardó bien, false si falló.
    //    Lo implementa ClienteDAO y lo llama ZonaFitApp (caso 3).
    boolean agregarCliente(Cliente cliente);

    // ✏️ Actualiza (modifica) los datos de un cliente ya existente. Devuelve true/false.
    //    Lo implementa ClienteDAO y lo llama ZonaFitApp (caso 4).
    boolean modificarCliente(Cliente cliente);

    // 🗑️ Elimina un cliente de la BD (por su id). Devuelve true/false.
    //    Lo implementa ClienteDAO y lo llama ZonaFitApp (caso 5).
    boolean eliminarCliente(Cliente cliente);

}