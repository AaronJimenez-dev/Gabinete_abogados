package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CasoDAO {
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;
    private static ClienteDAO clienteDAO;
    private static JuicioDAO juicioDAO;
    private static PersonaDAO personaDAO;
    private static AbogadoDAO abogadoDAO;

    public CasoDAO(Connection con, ClienteDAO clienteDAO, JuicioDAO juicioDAO, PersonaDAO personaDAO, AbogadoDAO abogadoDAO) {
        CasoDAO.con = con;
        CasoDAO.clienteDAO = clienteDAO;
        CasoDAO.juicioDAO = juicioDAO;
        CasoDAO.personaDAO = personaDAO;
        CasoDAO.abogadoDAO = abogadoDAO;
    }

    public ArrayList<Caso> getCasos() throws SQLException {
        ArrayList<Caso> casos = new ArrayList<>();
        plantilla = "select * from caso";
        ps = con.prepareStatement(plantilla);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            casos.add(new Caso(rs.getInt("num_expediente"),clienteDAO.verCliente(rs.getString("cliente_dni")),juicioDAO.verJuicio(rs.getInt("juicio_id"))));
        }
        return casos;
    }
    public Caso verCaso(int numExpediente) throws SQLException {
        Caso caso = null;
        try {
            plantilla = "select * from caso where num_expediente = ?";
            ps = con.prepareStatement(plantilla);
            ps.setInt(1, numExpediente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                caso = new Caso(rs.getInt("num_expediente"),clienteDAO.verCliente(rs.getString("cliente_dni")),juicioDAO.verJuicio(rs.getInt("juicio_id")));
            }
        }catch (NoSuchElementException e){
            caso = null;
        }
        return caso;
    }
    public Caso verCasoPorJuicio(int id) throws SQLException {
        Caso caso = null;
        try {
            plantilla = "select * from caso where juicio_id = ?";
            ps = con.prepareStatement(plantilla);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                caso = new Caso(rs.getInt("num_expediente"),clienteDAO.verCliente(rs.getString("cliente_dni")),juicioDAO.verJuicio(rs.getInt("juicio_id")));
            }
        }catch (NoSuchElementException e){
            caso = null;
        }
        return caso;
    }
    public int insertarCaso(Caso c) throws SQLException{
        plantilla = "insert into caso (cliente_dni, juicio_id) values(?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, c.getCliente().getPersona().getDni());
        ps.setInt(2, juicioDAO.getJuicios().getLast().getId());
        return ps.executeUpdate();
    }
    public int modificarCaso(Caso c) throws SQLException {
        plantilla = "update caso set cliente_dni = ?, juicio_id = ? where num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, c.getCliente().getPersona().getDni());
        ps.setInt(2, c.getJuicio().getId());
        ps.setInt(3, c.getNumExpediente());
        return  ps.executeUpdate();
    }
    public int eliminarCaso(Caso c) throws SQLException {
        eliminarCA(c);
        return eliminarCasoBD(c);
    }

    private void eliminarCA(Caso c) throws SQLException {
        if(buscarCaso(c))
            eliminarCasoAbogado(c);
    }
    private boolean buscarCaso(Caso c) throws SQLException {
        plantilla = "select * from caso_abogado where num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, c.getNumExpediente());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    private void eliminarCasoAbogado(Caso c) throws SQLException {
        plantilla = "delete from caso_abogado where num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, c.getNumExpediente());
        ps.executeUpdate();
    }
    private int eliminarCasoBD(Caso c) throws SQLException {
        plantilla = "delete from caso where num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, c.getNumExpediente());
        return  ps.executeUpdate();
    }

    //CASOABOGADO
    public ArrayList<Abogado> verAbogadosCaso(Caso caso) throws SQLException {
        ArrayList<Abogado> abogados = new ArrayList<>();
        plantilla = "select abogado_dni from caso_abogado where num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, caso.getNumExpediente());
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            abogados.add(abogadoDAO.verAbogado(rs.getString("abogado_dni")));
        }
        return abogados;
    }
    public boolean buscarAbogado(Abogado abogado, Caso caso) throws SQLException{
        plantilla = "select * from caso_abogado where abogado_dni = ? and num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, abogado.getPersona().getDni());
        ps.setInt(2, caso.getNumExpediente());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    public int anadirAbogado(Abogado abogado, Caso caso) throws SQLException {
        plantilla = "insert into caso_abogado values(?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, caso.getNumExpediente());
        ps.setString(2, abogado.getPersona().getDni());
        return ps.executeUpdate();
    }
    public int eliminarAbogado(Abogado abogado, Caso caso) throws SQLException {
        plantilla = "delete from caso_abogado where abogado_dni = ? and num_expediente = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, caso.getNumExpediente());
        ps.setString(2, abogado.getPersona().getDni());
        return  ps.executeUpdate();
    }
}
