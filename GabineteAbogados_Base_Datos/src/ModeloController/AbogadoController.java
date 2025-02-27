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
    public void modificarAbogado(String queHacer) {
        try {
            ArrayList<Abogado> abogados = abogadoDAO.getAbogados();
            System.out.println("Digame el dni del abogado a " + queHacer + ".");

            Abogado abogadoBorrado = conseguirAbogado();
            if (abogadoBorrado != null) {
                int size = eliminarAbogado(abogadoBorrado);

                if (queHacer.equals("eliminar")) {
                    if (size > 0) {
                    System.out.println("Abogado eliminado");
                    personaController.eliminarPersona(abogadoBorrado.getPersona());
                    }else{
                        System.out.println("No se ha podido eliminar el cliente");
                    }

                } else if (queHacer.equals("editar")) {
                    if(size > 0) {
                        System.out.println("Digame la informacion del nuevo abogado a " + queHacer + ".");
                        size = editarAbogado(abogadoBorrado);

                        if (size > 0) //Si el tamaño de abogados recupera el tamaño original se ha modificado correctamente.
                            System.out.println("El abogado se ha editado correctamente");
                        else {
                            System.out.println("El abogado no se ha podido editar");
                            abogadoDAO.insertarAbogados(abogadoBorrado);
                        }
                    }else
                        System.out.println("No se ha podido continuar.");
                }
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos.\n" + e.getMessage());
        }
    }
    private int editarAbogado(Abogado abogadoBorrado) {
        int size = 0;
        try {
            Abogado abogado = new Abogado(personaController.modificarPersona(abogadoBorrado.getPersona()));
            abogado.setCasos(abogadoBorrado.getCasos());
            size = abogadoDAO.insertarAbogados(abogado);
        }catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
    }
    public int eliminarAbogado(Abogado abogado) {
        int size = 0;
        try {
            size = abogadoDAO.eliminarAbogado(abogado);
            for (Caso caso : abogado.getCasos()) {
                casoDAO.eliminarAbogado(abogado, caso);
            }
        }catch (SQLException e){
            System.out.println("Ha ocurrido un error en la base de datos\n" + e.getMessage());
        }
        return size;
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

    //PASAR A CASOABOGADO
    public void anadirCaso(){
        Abogado abogado = conseguirAbogado();
        if (abogado != null){
            System.out.println("Dime que caso quieres añadir:");
            Caso caso = casoController.conseguirCaso();
            if (caso != null){
                if (abogadoDAO.buscarCaso(abogado, caso))
                    System.out.println("El caso ya está relacionado al abogado");
                else {
                    abogadoDAO.anadirCaso(abogado, caso);
                    casoDAO.anadirAbogado(abogado, caso);
                }
            }
        }
    }
    public void eliminarCaso(){
        Abogado abogado = conseguirAbogado();
        if (abogado != null){
            System.out.println("Dime que caso quieres eliminar:");
            Caso caso = casoController.conseguirCaso();
            if (caso != null){
                if (abogadoDAO.buscarCaso(abogado, caso)) {
                    abogadoDAO.eliminarCaso(abogado, caso);
                    casoDAO.eliminarAbogado(abogado, caso);
                }
                else
                    System.out.println("El caso no está relacionado al abogado");
            }
        }
    }
}
