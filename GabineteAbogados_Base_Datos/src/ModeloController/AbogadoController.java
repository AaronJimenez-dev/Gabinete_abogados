package ModeloController;

import Modelo.*;
import ModeloDAO.AbogadoDAO;
import ModeloDAO.CasoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class AbogadoController {
    private PersonaController personaController;
    private AbogadoDAO abogadoDAO;
    private CasoController casoController;
    private CasoDAO casoDAO;

    public AbogadoController(PersonaController personaController,AbogadoDAO abogadoDAO, CasoController casoController, CasoDAO casoDAO) {
        this.personaController = personaController;
        this.abogadoDAO = abogadoDAO;
        this.casoController = casoController;
        this.casoDAO = casoDAO;
    }

    public void setCasoController(CasoController casoController) {
        this.casoController = casoController;
    }

    public void verAbogado() {
        Abogado abogado = conseguirAbogado();
        if (abogado != null)
            System.out.println(abogado.toString());
    }
    public Abogado conseguirAbogado() {
        String dni = personaController.validar("DNI","^[0-9]{8}[A-Z]");
        try {
            Abogado abogado = abogadoDAO.verAbogado(dni);
            if (abogado == null)
                System.out.println("El abogado no existe.");
            return abogado;
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
            return null;
        }
    }
    public void insertarAbogado(){
        Abogado abogado = crearAbogado();
        if (abogado == null)
            System.out.println("El abogado ya existe.");
        else {
            try {
                abogadoDAO.insertarAbogados(abogado);
            }catch (SQLException e) {
                System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
            }
        }
    }
    public void modificarAbogado() {
        try {
            Abogado abogado = crearAbogado();
            System.out.println("Digame la informacion del nuevo abogado a editar.");
            int size = editarAbogado(abogado);
            if (size > 0) //Si el tamaño de abogados recupera el tamaño original se ha modificado correctamente.
                System.out.println("El abogado se ha editado correctamente");
            else {
                System.out.println("El abogado no se ha podido editar");
                abogadoDAO.insertarAbogados(abogado);
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }
    private int editarAbogado(Abogado abogado) {
        int size = 0;
        try {
            abogado.setPersona(personaController.modificarPersona(abogado.getPersona()));
            size = abogadoDAO.modificarAbogado(abogado);
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
    }
    public void eliminarAbogado() {
        try {
            System.out.println("Digame el dni del abogado a eliminar.");
            Abogado abogado = conseguirAbogado();
            if (abogado != null) {
                int size = abogadoDAO.eliminarAbogado(abogado);
                if (size > 0) {
                    System.out.println("Abogado eliminado");
                    personaController.eliminarPersona(abogado.getPersona());
                } else {
                    System.out.println("No se ha podido eliminar el abogado");
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
    public void verTodos(){
        try {
            ArrayList<Abogado> abogados = abogadoDAO.getAbogados();
            for (Abogado abogado : abogados)
                System.out.println(abogado.getPersona().getDni());
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }

    public Abogado crearAbogado() {
        Persona persona = personaController.validarPersona("abogado");
        try {
            if (abogadoDAO.verAbogado(persona.getDni()) == null)
                return new Abogado(persona);
            else
                return null;
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
            return null;
        }
    }

    public void verCasosAbogado(){
        try {
            ArrayList<Caso> casos = abogadoDAO.verCasosAbogado(conseguirAbogado());
            for (Caso caso : casos){
                System.out.println(caso.toString());
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }catch (NullPointerException e){
            System.out.println("Todavia no hay ninguna relacion.");
        }
    }
    public void anadirCaso(){
        try {
            Abogado abogado = conseguirAbogado();
            if (abogado != null) {
                System.out.println("Dime que caso quieres añadir:");
                Caso caso = casoController.conseguirCaso();
                if (caso != null) {
                    if (abogadoDAO.buscarCaso(abogado, caso))
                        System.out.println("El caso ya está relacionado al abogado");
                    else {
                        if(abogadoDAO.anadirCaso(abogado, caso) > 0)
                            System.out.println("El abogado y el caso se han relacionado exitosamente.");
                        else
                            System.out.println("No se ha podido relacionar el caso y el abogado.");
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
    public void eliminarCaso(){
        try {
            Abogado abogado = conseguirAbogado();
            if (abogado != null) {
                System.out.println("Dime que caso quieres eliminar:");
                Caso caso = casoController.conseguirCaso();
                if (caso != null) {
                    if (abogadoDAO.buscarCaso(abogado, caso)) {
                        if (abogadoDAO.eliminarCaso(abogado, caso) > 0)
                            System.out.println("El abogado se ha dejado de relacionar con el caso exitosamente.");
                        else
                            System.out.println("El abogado no se ha podido dejar de relacionar con el caso.");
                    } else
                        System.out.println("El caso no está relacionado al abogado");
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
    }
}
