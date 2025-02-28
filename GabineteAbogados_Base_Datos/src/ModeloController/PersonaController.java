package ModeloController;

import Modelo.Abogado;
import Modelo.Caso;
import Modelo.Cliente;
import Modelo.Persona;
import ModeloDAO.AbogadoDAO;
import ModeloDAO.ClienteDAO;
import ModeloDAO.PersonaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class PersonaController {
    private PersonaDAO personaDAO;
    private ClienteDAO clienteDAO;
    private AbogadoDAO abogadoDAO;

    public PersonaController(PersonaDAO personaDAO, ClienteDAO clienteDAO, AbogadoDAO abogadoDAO) {
        this.personaDAO = personaDAO;
        this.clienteDAO = clienteDAO;
        this.abogadoDAO = abogadoDAO;
    }

    public Persona validarPersona(String tipo){
        String dni = validar("DNI","^[0-9]{8}[A-Z]");
        Persona persona = validarExistencia(dni);
        if (persona == null) {
            return insertarPersona(dni);
        }else {
            System.out.println("Se ha encontrado una persona con ese DNI, procedemos a añadir la información automáticamente.");
            return persona;
        }
    }
    private Persona insertarPersona(String dni) {
        Persona persona = crearPersona(dni);
        try {
            personaDAO.insertarPersona(persona);
        }catch (SQLException e){
            System.out.println("Ha ocurrido un problema con la base de datos.\n" + e.getMessage());
            persona = null;
        }
        return persona;
    }
    private Persona crearPersona(String dni){
        String nombre = validar("Nombre", "^[A-Z][a-z]{0,50}");
        String apellido = validar("Apellido", "^[A-Z][a-z]{0,50}");
        String direccion = validar("Direccion", "^[A-Z][a-z]+ ([A-Z][a-z]+) [0-9]{0,2}, [0-9][A-Z]");
        Persona persona = new Persona(dni, nombre, apellido, direccion);
        return persona;
    }
    public Persona modificarPersona(Persona persona){
        persona.setNombre(validar("Nombre", "^[A-Z][a-z]{0,50}"));
        persona.setApellido(validar("Apellido", "^[A-Z][a-z]{0,50}"));
        persona.setDireccion(validar("Direccion", "^[A-Z][a-z]+ ([A-Z][a-z]+) [0-9]{0,2}, [0-9][A-Z]"));
        return persona;
    }
    public String validar(String quePreguntar, String patron){
        Scanner sc = new Scanner(System.in);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patron);
        boolean invalido;
        String preguntado;
        do{
            invalido = false;
            System.out.println("Introduzca el " + quePreguntar.toLowerCase() + ": ");
            preguntado = sc.nextLine();
            java.util.regex.Matcher matcher = pattern.matcher(preguntado);
            if (!matcher.matches()) {
                System.out.println(quePreguntar + " con formato incorrecto");
                invalido = true;
            }
        }while (invalido);
        return preguntado;
    }
    private Persona validarExistencia(String dni){
        Persona personaDevolver = null;
        try {
            ArrayList<Persona> personas = personaDAO.getPersonas();
            Optional<Persona> p = personas
                    .stream()
                    .filter(persona -> persona
                            .getDni().equalsIgnoreCase(dni))
                    .findFirst();
            if (p.isPresent())
                personaDevolver = p.get();
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error con la base de datos. \n" + e.getMessage());
        }catch (NoSuchElementException e){
            personaDevolver = null;
        }
        return personaDevolver;
    }
    public void eliminarPersona(Persona persona){
        boolean borrar;
        try {
            if (clienteDAO.verCliente(persona.getDni()) == null && abogadoDAO.verAbogado(persona.getDni()) == null)
                borrar = true;
            else
                borrar = false;
            if (borrar) {
                try {
                    personaDAO.eliminarPersona(persona);
                } catch (SQLException e) {
                    System.out.println("Ha ocurrido un error con la base de datos.\n" + e.getMessage());
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error con la base de datos.\n" + e.getMessage());
        }
    }
}
