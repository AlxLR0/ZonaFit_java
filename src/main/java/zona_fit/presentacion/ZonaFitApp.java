package zona_fit.presentacion;

import zona_fit.datos.ClienteDAO;
import zona_fit.datos.IClienteDao;
import zona_fit.dominio.Cliente;

import java.util.Scanner;

public class ZonaFitApp {
    static void main() {
        zonaFitApp();
    }

    private static void zonaFitApp(){
        var salir = false;
        var consola = new Scanner(System.in);

        //crear obj de la clase clientedao
        var clienteDao = new ClienteDAO();
        while (!salir){
            try {
                var opcion= mostrarMenu(consola);
                salir=ejecutarOpciones(consola, opcion,clienteDao);
            }catch (Exception e){
                System.out.println("Error al ejeuctar opciones: "+e.getMessage());
            }
            System.out.println();
        }
    }

    public static int mostrarMenu(Scanner consola){
        System.out.print("""
                ***---ZONA FIT---*** 
                1. lISTAR CLIENTES
                2. BUSCAR CLIENTE
                3. AGREGAR CLIENTE
                4. MODIFICAR CLIENTE
                5. ELIMINAR CLIENTE
                6. SALIR
                ELIJE UNA OPCION:\s""");
        return Integer.parseInt(consola.nextLine());

    }

    private static boolean ejecutarOpciones(Scanner consola, int opcion, IClienteDao clienteDao){
        var salir = false;
        switch (opcion){
            case 1->{// listar clientes
                System.out.println("---Listado de clientes---");
                var clientes = clienteDao.listarClientes();
                clientes.forEach(System.out::println);
            }
            case 2 ->{// buscar cliente por id
                System.out.print("Introduce ID del cliente: ");
                var idCliente = Integer.parseInt(consola.nextLine());
                var cliente = new Cliente(idCliente);
                var encontrado = clienteDao.buscarClientePorId(cliente);
                if (encontrado)
                    System.out.println("Cliente encontrado: "+cliente);
                else
                    System.out.println("Cliente no encontrado"+ cliente);

            }
            case 3 ->{// agregar cliente
                System.out.println("--- Agregar cliente ---");
                System.out.print("Nombre: ");
                var nombre = consola.nextLine();
                System.out.print("Apellido: ");
                var apellido = consola.nextLine();
                System.out.print("Membresia: ");
                var membresia = Integer.parseInt(consola.nextLine());

                //crear objeto cliente (sin id)
                var cliente = new Cliente(nombre,apellido,membresia);
                var agregado = clienteDao.agregarCliente(cliente);
                if (agregado)
                    System.out.println("Cliente agregado: "+cliente);
                else
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

                //crear objeto a modificar
                var cliente = new Cliente(idCliente, nombre, apellido, membresia);
                var modificado = clienteDao.modificarCliente(cliente);
                if(modificado)
                    System.out.println("cliente modificado: "+cliente);
                else
                    System.out.println("cliente no modificado");


            }
            case 5->{// eliminar cliente
                System.out.println("--- Eliminar cliente ---");
                System.out.print("Id Cliente: ");
                var idCliente = Integer.parseInt(consola.nextLine());
                var cliente = new Cliente(idCliente);
                var eliminado = clienteDao.eliminarCliente(cliente);

                if(eliminado)
                    System.out.println("cliente eliminado: "+cliente);
                else
                    System.out.println("cliente no eliminado");

            }
            case 6->{//salir
                System.out.println("saliendo...");
                salir = true;

            }
            default -> System.out.println("opcion no valida");
        }
        return salir;
    }
}
