package ModeloDAO;

import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Juicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class JuicioDAO {
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;

    public JuicioDAO(Connection con) {
        JuicioDAO.con = con;
    }

    public ArrayList<Juicio> getJuicios() throws SQLException {
        ArrayList<Juicio> juicios = new ArrayList<>();
        plantilla = "select * from juicio";
        ps = con.prepareStatement(plantilla);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Juicio juicio = new Juicio(LocalDate.parse(rs.getString("fecha_inicio")),rs.getString("estado"));
            juicio.setId(rs.getInt("id"));
            if (rs.getString("fecha_fin") != null)
                juicio.setFechaFin(LocalDate.parse(rs.getString("fecha_fin")));
            juicios.add(juicio);
        }
        return juicios;
    }
    public Juicio verJuicio(int id) throws SQLException {
        Juicio juicio = null;
        try {
            plantilla = "select * from juicio where id = ?";
            ps = con.prepareStatement(plantilla);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                juicio = new Juicio(LocalDate.parse(rs.getString("fecha_inicio")),rs.getString("estado"));
                juicio.setId(rs.getInt("id"));
                if (rs.getString("fecha_fin") != null)
                    juicio.setFechaFin(LocalDate.parse(rs.getString("fecha_fin")));
            }
        }catch (NoSuchElementException e){
            juicio = null;
        }
        return juicio;
    }
    public int insertarJuicio(Juicio j) throws SQLException {
        plantilla = "insert into juicio (fecha_inicio,estado,fecha_fin) values(?,?,?)";
        ps = con.prepareStatement(plantilla);
        ps.setDate(1, convertir(j.getFechaInicio()));
        ps.setString(2, j.getEstado());
        ps.setDate(3, null);
        if(j.getFechaFin() != null)
            ps.setDate(3, convertir(j.getFechaFin()));
        return ps.executeUpdate();
    }
    public int eliminarJuicio(Juicio j) throws SQLException {
        plantilla = "delete from juicio where id = ?";
        ps = con.prepareStatement(plantilla);
        ps.setInt(1, j.getId());
        return  ps.executeUpdate();
    }
    public int modificarJuicio(Juicio j) throws SQLException {
        plantilla = "update juicio set fecha_fin = ?, estado = ? where id = ?";
        ps = con.prepareStatement(plantilla);
        ps.setDate(1, null);
        if(j.getFechaFin() != null)
            ps.setDate(1, convertir(j.getFechaFin()));
        ps.setString(2, j.getEstado());
        ps.setInt(3, j.getId());
        return  ps.executeUpdate();
    }

    private java.sql.Date convertir(LocalDate fechaSalida){
        java.sql.Date fecha;
        fecha = java.sql.Date.valueOf(fechaSalida);
        return fecha;
    }

}
