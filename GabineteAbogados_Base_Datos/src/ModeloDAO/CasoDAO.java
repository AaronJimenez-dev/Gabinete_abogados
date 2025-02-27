package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CasoDAO {
    private static ArrayList<Caso> casos = new ArrayList<>();
    private static Connection con;
    private static String plantilla;
    private static PreparedStatement ps;

    public CasoDAO(Connection con) {
        CasoDAO.con = con;
    }

    public ArrayList<Caso> getCasos() {
        return casos;
    }
    public void insertarCaso(Caso caso) {
        casos.add(caso);
    }
    public Caso verCaso(int numExpediente) {
        Caso caso;
        try {
            caso = casos.stream().filter(c -> c.getNumExpediente() == numExpediente).findFirst().get();
        }catch (NoSuchElementException e){
            caso = null;
        }
        return caso;
    }
    public void eliminarCaso(Caso caso) {
        casos.remove(caso);
    }

    public boolean buscarAbogado(Abogado abogado, Caso caso){
        ArrayList<Abogado> abogados = caso.getAbogados();
        boolean existe;
        try{
            existe = abogados.stream().anyMatch(a -> a.getPersona().getDni().equals(abogado.getPersona().getDni()));
        }catch (NullPointerException e){
            existe = false;
        }
        return existe;
    }
    public void anadirAbogado(Abogado abogado, Caso caso) {
        try {
            caso.getAbogados().add(abogado);
        }catch (NullPointerException e){
            caso.setAbogados(new ArrayList<>());
            caso.getAbogados().add(abogado);
        }
    }
    public void eliminarAbogado(Abogado abogado, Caso caso) {
        caso.getAbogados().remove(abogado);
    }
}
