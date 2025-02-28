package ModeloDAO;

import Modelo.Caso;
import Modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ClienteDAO {
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;
    private static PersonaDAO personaDAO;
    private static CasoDAO casoDAO;

    public ClienteDAO(Connection con, PersonaDAO personaDAO) {
        ClienteDAO.con = con;
        ClienteDAO.personaDAO = personaDAO;
    }

    public void setCasoDAO(CasoDAO casoDAO) {
        ClienteDAO.casoDAO = casoDAO;
    }

    public ArrayList<Cliente> getClientes() throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        plantilla = "select * from cliente";
        ps = con.prepareStatement(plantilla);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            clientes.add(new Cliente(personaDAO.verPersona(rs.getString("dni")), rs.getInt("num_tlfn"),rs.getString("correo")));
        }
        return clientes;
    }
    public Cliente verCliente(String dni) throws SQLException {
        Cliente cliente = null;
        try {
            plantilla = "select * from cliente where dni = ?";
            ps = con.prepareStatement(plantilla);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                cliente = new Cliente(personaDAO.verPersona(rs.getString("dni")), rs.getInt("num_tlfn"),rs.getString("correo"));
            }
        }catch (NoSuchElementException e){
            cliente = null;
        }
        return cliente;
    }
    public Cliente verCasoCliente(Cliente cliente) throws SQLException {
        ArrayList<Caso> casos = new ArrayList<>();
        plantilla = "select num_expediente from caso where cliente_dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, cliente.getPersona().getDni());
        ResultSet rsCasos = ps.executeQuery();
        while (rsCasos.next()){
            casos.add(casoDAO.verCaso(rsCasos.getInt("num_expediente")));
        }
        try {
            if (!casos.isEmpty())
                cliente.setCasos(casos);
        }catch (Exception e){
            return cliente;
        }
        return cliente;
    }
    public int insertarCliente(Cliente c) throws SQLException {
        plantilla = "insert into cliente values(?,?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, c.getPersona().getDni());
        ps.setInt(2, c.getTelefono());
        ps.setString(3, c.getCorreo());
        return ps.executeUpdate();
    }
    public int eliminarClientes(Cliente c) throws SQLException {
        plantilla = "delete from cliente where dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, c.getPersona().getDni());
        return  ps.executeUpdate();
    }
    public int modificarCliente(Cliente c) throws SQLException {
        if (personaDAO.modificarPersona(c.getPersona()) > 0) {
            plantilla = "update cliente set num_tlfn = ?, correo = ? where dni = ?";
            ps = con.prepareStatement(plantilla);
            ps.setInt(1, c.getTelefono());
            ps.setString(2, c.getCorreo());
            ps.setString(3, c.getPersona().getDni());
            return ps.executeUpdate();
        }else {
            plantilla = "rollback";
            ps = con.prepareStatement(plantilla);
            ps.executeUpdate();
            return 0;
        }
    }

}
