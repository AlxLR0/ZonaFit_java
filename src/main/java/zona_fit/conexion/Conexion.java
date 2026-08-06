package zona_fit.conexion;
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection getConexion(){
        Connection conexion = null;
        var baseDatos = "Zona_fit_java";
        var url = "jdbc:mysql://127.0.0.1:3307/"+baseDatos;
        var usuario ="root";
        var password = "admin";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);

        }catch (Exception e){
            System.out.println("Error al conectar: "+e.getMessage());
        }
        return conexion;
    }

    static void main() {
        var conexion = Conexion.getConexion();

        if (conexion != null)
            System.out.println("ya jalo esto 😎 "+conexion);
        else
            System.out.println("no jalo");
    }
}
