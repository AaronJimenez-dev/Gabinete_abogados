package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDatos {
    private static Connection con;
    public static void abrirConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gabinete_abogados";
            String user = "root";
            String passwd = "";
            con = DriverManager.getConnection(url, user, passwd);
        }catch (ClassNotFoundException e){
            System.out.println("Falta el Driver para conectar con la base de datos: " + e.getMessage());
        }catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
    public static Connection getCon() {
        return con;
    }
    public static void cerrarConexion() {
        try{
            con.close();
        }catch(SQLException e){
            System.out.println("Error al cerrar conexion: " + e.getMessage());
        }
    }
}
