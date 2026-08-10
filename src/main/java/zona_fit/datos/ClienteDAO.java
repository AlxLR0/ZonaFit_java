package zona_fit.datos;

import zona_fit.conexion.Conexion;
import zona_fit.dominio.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static zona_fit.conexion.Conexion.getConexion;

public class ClienteDAO implements IClienteDao {

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConexion();
        var sql = "SELECT * FROM cliente ORDER BY id";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                var cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setMembresia(rs.getInt("membresia"));
                clientes.add(cliente);
            }
        }catch (Exception e){
            System.out.println("Error al list clientes: "+e.getMessage());
        }
        finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar: "+e);
            }
        }

        return clientes;
    }

    @Override
    public boolean buscarClientePorId(Cliente cliente) {
        PreparedStatement ps;
        ResultSet rs;
        var con = getConexion();
        var sql = "SELECT * FROM cliente WHERE id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setMembresia(rs.getInt("membresia"));
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al recuperar cliente por id: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }

        return false;

    }


    @Override
    public boolean agregarCliente(Cliente cliente) {
        PreparedStatement ps;
        var con = getConexion();
        var sql = "INSERT INTO cliente (nombre, apellido, membresia)"+ " values(?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getMembresia());

            var registros = ps.executeUpdate();
            if (registros > 0)
                return true;


        }catch (Exception e){
            System.out.println("Error al insertar el cliente: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean modificarCliente(Cliente cliente) {
        PreparedStatement ps;
        var con = getConexion();
        var sql = "UPDATE cliente SET nombre=?, apellido=?, membresia=?"+ " WHERE id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getMembresia());
            ps.setInt(4, cliente.getId());

            ps.execute();
            return true;


        }catch (Exception e){
            System.out.println("Error al modificar el cliente: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean eliminarCliente(Cliente cliente) {

        PreparedStatement ps;
        var con = getConexion();
        var sql = "DELETE FROM cliente WHERE id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(2, cliente.getId());

            ps.execute();
            return true;


        }catch (Exception e){
            System.out.println("Error al eliminar el cliente: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    static void main() {
        IClienteDao clienteDao = new ClienteDAO();

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
//        var nuevoCliente = new Cliente("Fulanito","detal",300);
//        var agregado = clienteDao.agregarCliente(nuevoCliente);
//        if (agregado)
//            System.out.println("Cliente agregado: "+nuevoCliente);
//        else
//            System.out.println("no se agrego cliente ❌");

        //modificar cliente
//        var modificarCliente = new Cliente(2, "fuulanito","detal",300);
//        var modificado = clienteDao.modificarCliente(modificarCliente);
//        if (modificado)
//            System.out.println("Cliente modificado: "+modificarCliente);
//        else
//            System.out.println("no se modifico el cliente "+modificarCliente);

        //eliminar cliente
        var clienteEliminar = new Cliente(2);
        var eliminado = clienteDao.modificarCliente(clienteEliminar);
        if (eliminado)
            System.out.println("Cliente eliminado: "+clienteEliminar);
        else
            System.out.println("no se elimino el cliente "+clienteEliminar);



        //listar clientes
        System.out.println("*** Listar clientes ***");
        var clientes = clienteDao.listarClientes();
        clientes.forEach(System.out::println);



    }
}
