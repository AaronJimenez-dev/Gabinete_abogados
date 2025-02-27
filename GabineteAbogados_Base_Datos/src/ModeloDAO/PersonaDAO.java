package ModeloDAO;

import Modelo.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PersonaDAO {
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;

    public PersonaDAO(Connection con) {
        PersonaDAO.con = con;
    }

    public ArrayList<Persona> getPersonas() throws SQLException {
        ArrayList<Persona> personas = new ArrayList<>();
        plantilla = "select * from persona";
        ps = con.prepareStatement(plantilla);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            personas.add(new Persona(rs.getString("dni"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("direccion")));
        }
        return personas;
    }
    public Persona verPersona(String dni) throws SQLException {
        Persona persona = null;
        try {
            plantilla = "select * from persona where dni = ?";
            ps = con.prepareStatement(plantilla);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                persona = new Persona(rs.getString("dni"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("direccion"));
            }
        }catch (NoSuchElementException e){
            persona = null;
        }
        return persona;
    }
    public void insertarPersona(Persona p) throws SQLException {
        plantilla = "insert into persona values(?,?,?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, p.getDni());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getApellido());
        ps.setString(4, p.getDireccion());
        ps.executeUpdate();
    }
    public void eliminarPersona(Persona p) throws SQLException {
        plantilla = "delete from persona where dni = ?";
        ps = con.prepareStatement(plantilla);
        ps.setString(1, p.getDni());
        ps.executeUpdate();
    }
}
