package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AbogadoDAO {
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;
    private static PersonaDAO personaDAO;

    public AbogadoDAO(Connection con, PersonaDAO personaDAO) {
        AbogadoDAO.con = con;
        AbogadoDAO.personaDAO = personaDAO;
    }

    public ArrayList<Abogado> getAbogados() throws SQLException {
        ArrayList<Abogado> abogados = new ArrayList<>();
        plantilla = "select * from abogado";
        ps = con.prepareStatement(plantilla);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            abogados.add(new Abogado(personaDAO.verPersona(rs.getString("dni"))));
        }
        return abogados;
    }
    public Abogado verAbogado(String dni) throws SQLException {
        Abogado abogado = null;
        try {
            plantilla = "select * from abogado where dni = ?";
            ps = con.prepareStatement(plantilla);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                abogado = new Abogado(personaDAO.verPersona(rs.getString("dni")));
            }
        }catch (NoSuchElementException e){
            abogado = null;
        }
        return abogado;
    }
    public int insertarAbogados(Abogado a) throws SQLException {
        plantilla = "insert into abogado values(?)";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, a.getPersona().getDni());
        return ps.executeUpdate();
    }
    public int eliminarAbogado(Abogado a) throws SQLException {
        plantilla = "delete from abogado where dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, a.getPersona().getDni());
        return  ps.executeUpdate();
    }

    public boolean buscarCaso(Abogado abogado, Caso caso){
        ArrayList<Caso> casos = abogado.getCasos();
        boolean existe;
        try{
            existe = casos.stream().anyMatch(c -> c.getNumExpediente() == caso.getNumExpediente());
        }catch (NullPointerException e){
            existe = false;
        }
        return existe;
    }
    public void anadirCaso(Abogado abogado, Caso caso) {
        try {
            abogado.getCasos().add(caso);
        }catch (NullPointerException e){
            abogado.setCasos(new ArrayList<>());
            abogado.getCasos().add(caso);
        }
    }
    public void eliminarCaso(Abogado abogado, Caso caso) {
        abogado.getCasos().remove(caso);
    }
}
