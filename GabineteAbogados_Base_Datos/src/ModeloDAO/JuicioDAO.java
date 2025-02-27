package ModeloDAO;

import Modelo.Cliente;
import Modelo.Juicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class JuicioDAO {
    private static ArrayList<Juicio> juicios = new ArrayList<>();
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;

    public JuicioDAO(Connection con) {
        JuicioDAO.con = con;
    }

    public ArrayList<Juicio> getJuicios() {
        return juicios;
    }
    public void insertarJuicio(Juicio juicio) {
        juicios.add(juicio);
    }
    public Juicio verJuicio(int id) {
        Juicio juicio;
        try {
            juicio = juicios.stream().filter(j -> j.getId() == id).findFirst().get();
        }catch (NoSuchElementException e){
            juicio = null;
        }
        return juicio;
    }
    public void eliminarJuicio(Juicio juicio) {
        juicios.remove(juicio);
    }
}
