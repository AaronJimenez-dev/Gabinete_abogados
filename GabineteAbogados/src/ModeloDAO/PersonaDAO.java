package ModeloDAO;

import Modelo.Cliente;
import Modelo.Persona;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PersonaDAO {
    private ArrayList<Persona> personas = new ArrayList<>();

    public ArrayList<Persona> getPersonas() {
        return personas;
    }
    public Persona verPersona(String dni) {
        Persona persona;
        try {
            persona = personas.stream().filter(c -> c.getDni().equals(dni)).findFirst().get();
        }catch (NoSuchElementException e){
            persona = null;
        }
        return persona;
    }
    public void insertarPersona(Persona persona){
        personas.add(persona);
    }
    public void eliminarPersona(Persona persona) {
        personas.remove(persona);
    }
}
