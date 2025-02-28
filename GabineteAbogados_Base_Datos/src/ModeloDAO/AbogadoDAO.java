package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;

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
    private static CasoDAO casoDAO;

    public AbogadoDAO(Connection con, PersonaDAO personaDAO) {
        AbogadoDAO.con = con;
        AbogadoDAO.personaDAO = personaDAO;
    }

    public void setCasoDAO(CasoDAO casoDAO) {
        AbogadoDAO.casoDAO = casoDAO;
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
        }catch (NullPointerException e){
            return abogado;
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
        eliminarCA(a);
        return eliminarAbogadoBD(a);
    }

    private void eliminarCA(Abogado a) throws SQLException {
        if(buscarAbogado(a))
            eliminarCasoAbogado(a);
    }
    private boolean buscarAbogado(Abogado a) throws SQLException {
        plantilla = "select * from caso_abogado where abogado_dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, a.getPersona().getDni());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    private void eliminarCasoAbogado(Abogado a) throws SQLException {
        plantilla = "delete from caso_abogado where abogado_dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, a.getPersona().getDni());
        ps.executeUpdate();
    }
    private int eliminarAbogadoBD(Abogado a) throws SQLException {
        plantilla = "delete from abogado where dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, a.getPersona().getDni());
        return  ps.executeUpdate();
    }

    public ArrayList<Caso> verCasosAbogado(Abogado abogado) throws SQLException {
        ArrayList<Caso> casos = new ArrayList<>();
        plantilla = "select num_expediente from caso_abogado where abogado_dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, abogado.getPersona().getDni());
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            casos.add(casoDAO.verCaso(rs.getInt("num_expediente")));
        }
        return casos;
    }
    public boolean buscarCaso(Abogado abogado, Caso caso)throws SQLException{
        plantilla = "select * from caso_abogado where abogado_dni = ? and num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, abogado.getPersona().getDni());
        ps.setInt(2, caso.getNumExpediente());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    public int anadirCaso(Abogado abogado, Caso caso) throws SQLException{
        plantilla = "insert into caso_abogado values(?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, caso.getNumExpediente());
        ps.setString(2, abogado.getPersona().getDni());
        return ps.executeUpdate();
    }
    public int eliminarCaso(Abogado abogado, Caso caso) throws SQLException{
        plantilla = "delete from caso_abogado where abogado_dni = ? and num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, caso.getNumExpediente());
        ps.setString(2, abogado.getPersona().getDni());
        return  ps.executeUpdate();
    }
    public int modificarAbogado(Abogado abogado) throws SQLException{
        return personaDAO.modificarPersona(abogado.getPersona());
    }
}
