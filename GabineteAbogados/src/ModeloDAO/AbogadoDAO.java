package ModeloDAO;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.Cliente;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AbogadoDAO {
    private static ArrayList<Abogado> abogados = new ArrayList<>();

    public ArrayList<Abogado> getAbogados() {
        return abogados;
    }
    public Abogado verAbogado(String dni) {
        Abogado abogado;
        try {
            abogado = abogados.stream().filter(a -> a.getPersona().getDni().equals(dni)).findFirst().get();
        }catch (NoSuchElementException e){
            abogado = null;
        }
        return abogado;
    }
    public void insertarAbogados(Abogado abogado) {
        abogados.add(abogado);
    }
    public void eliminarAbogado(Abogado abogado) {
        abogados.remove(abogado);
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
